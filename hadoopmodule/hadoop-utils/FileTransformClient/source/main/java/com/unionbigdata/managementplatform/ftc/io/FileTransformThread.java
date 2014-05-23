package com.unionbigdata.managementplatform.ftc.io;

import com.unionbigdata.managementplatform.ftc.io.exception.DFSFileExistException;
import com.unionbigdata.managementplatform.ftc.io.exception.DFSFileNotExistException;
import com.unionbigdata.managementplatform.ftc.io.exception.DFSInvalidPathException;
import com.unionbigdata.managementplatform.ftc.io.exception.ServerUnknownException;

import java.io.*;
import java.net.Socket;

/**
 * Created by Administrator on 14-3-17.
 */
class FileTransformThread {

    //message code for communicating with client.
    private static final int MSG_REQUEST_TRANSFORM_TYPE = 0;
    private static final int MSG_REQUEST_SOURCE = 1;
    private static final int MSG_REQUEST_DESTINATION = 2;

    private static final int MSG_SINGNAL_START_UPLOAD = 21;
    private static final int MSG_SINGNAL_START_DOWNLOAD = 22;
    private static final int MSG_SINGNAL_EXIT = 23;

    private static final int MSG_RESPONSE_FILE_LENGTH = 3;

    private static final int MSG_TRANSFORM_TYPE_UPLOAD = 10;     //the transform request is upload file
    private static final int MSG_TRANSFORM_TYPE_DOWNLOAD = 11;   //the transform request is download file

    private static final int MSG_ERROR_FILE_EXISTS = 31;
    private static final int MSG_ERROR_FILE_NOT_FOUND = 32;
    private static final int MSG_ERROR_INVALID_PATH = 33;
    private static final int MSG_ERROR_SERVER_UNKNOWN_ERROR = 34;

    private static final int BUFFER_SIZE = 4096;

    private FileTransformThread() {
    }


    public static class UploadThread implements Runnable {
        private final Socket con;
        private final String destination;
        private final MyProcessReporter reporter;
        private final InputStream uploadStream;

        public UploadThread(Socket connection,
                            InputStream inputStream,
                            String destination,
                            MyProcessReporter reporter) {
            if (connection == null) {
                throw new NullPointerException();
            }
            this.con = connection;
            this.destination = destination;
            this.reporter = reporter;
            this.uploadStream = inputStream;
        }

        public void startUpload() {
            try {
                this.listenAndResponse();
            } catch (DFSFileExistException e) {
                reporter.setError(ErrorType.FileExistInHDFS);
            } catch (DFSInvalidPathException e) {
                reporter.setError(ErrorType.InvalidHdfsFileDirectory);
            } catch (ServerUnknownException e) {
                reporter.setError(ErrorType.ServerUnknownError);
            } finally {
                reporter.setCompleted();
            }
        }

        private void transform(InputStream inputStream, DataOutputStream outputStream) throws IOException {
            byte[] buffer = new byte[BUFFER_SIZE];
            int i;
            while ((i = inputStream.read(buffer)) != -1) {
                if (i > 0) {
                    reporter.increaseTransformedBytes(i);
                }
                outputStream.write(buffer, 0, i);
            }
            try {
                inputStream.close();
            } catch (IOException nouse) {
                //do nothing
            }
            try {
                outputStream.close();
            } catch (IOException nouse) {
                //do nothing
            }
        }

        private void listenAndResponse() throws DFSFileExistException, DFSInvalidPathException, ServerUnknownException {
            DataInputStream inputStream = null;
            DataOutputStream outputStream = null;

            try {
                inputStream = new DataInputStream(con.getInputStream());
                outputStream = new DataOutputStream(con.getOutputStream());

                int order = -1;
                while (order != MSG_SINGNAL_EXIT) {
                    order = inputStream.readInt();
                    switch (order) {
                        case MSG_REQUEST_TRANSFORM_TYPE:
                            outputStream.writeInt(MSG_TRANSFORM_TYPE_UPLOAD);
                            break;
                        case MSG_REQUEST_DESTINATION:
                            outputStream.writeUTF(destination);
                            break;
                        case MSG_SINGNAL_START_UPLOAD:
                            transform(uploadStream, outputStream);
                            break;
                        //exception
                        case MSG_ERROR_FILE_EXISTS:
                            throw new DFSFileExistException(destination);
                        case MSG_ERROR_INVALID_PATH:
                            throw new DFSInvalidPathException(destination);
                        case MSG_ERROR_SERVER_UNKNOWN_ERROR:
                            throw new ServerUnknownException();
                    }
                }
            } catch (IOException e) {
               /*do nothing*/
            }
        }

        @Override
        public void run() {
            this.startUpload();
        }
    }

    public static class DownloadThread implements Runnable {
        private final Socket con;
        private final String source;
        private final MyProcessReporter reporter;
        private final OutputStream downloadStream;

        public DownloadThread(Socket con,
                              String source,
                              MyProcessReporter reporter,
                              OutputStream downloadStream) {
            this.con = con;
            this.source = source;
            this.reporter = reporter;
            this.downloadStream = downloadStream;
        }

        public void startDownload() {
            try {
                this.listenAndResponse();
            } catch (DFSInvalidPathException e) {
               reporter.setError(ErrorType.InvalidHdfsFileDirectory);
            } catch (DFSFileNotExistException e) {
               reporter.setError(ErrorType.FileNotExistInHDFS);
            } catch (ServerUnknownException e) {
                reporter.setError(ErrorType.ServerUnknownError);
            } finally {
                reporter.setCompleted();
            }
        }
        private void transform(InputStream inputStream, OutputStream outputStream) throws IOException {
            byte[] buffer = new byte[BUFFER_SIZE];
            int i;
            while ((i = inputStream.read(buffer)) != -1) {
                if (i > 0) {
                    reporter.increaseTransformedBytes(i);
                }
                outputStream.write(buffer, 0, i);
            }
            try {
                inputStream.close();
            } catch (IOException nouse) {
                //do nothing
            }
            try {
                outputStream.close();
            } catch (IOException nouse) {
                //do nothing
            }
        }

        private void listenAndResponse() throws DFSInvalidPathException, DFSFileNotExistException, ServerUnknownException {
            DataInputStream inputStream = null;
            DataOutputStream outputStream = null;

            try {
                inputStream = new DataInputStream(con.getInputStream());
                outputStream = new DataOutputStream(con.getOutputStream());

                int order = -1;
                while (order != MSG_SINGNAL_EXIT) {
                    order = inputStream.readInt();
                    switch (order) {
                        case MSG_REQUEST_TRANSFORM_TYPE:
                            outputStream.writeInt(MSG_TRANSFORM_TYPE_DOWNLOAD);
                            break;
                        case MSG_REQUEST_SOURCE:
                            outputStream.writeUTF(source);
                            break;
                        case MSG_SINGNAL_START_DOWNLOAD:
                            transform(inputStream, downloadStream);
                            break;
                        case MSG_RESPONSE_FILE_LENGTH:
                            reporter.setTotalBytes(inputStream.readLong());
                            break;
                        //exception
                        case MSG_ERROR_FILE_NOT_FOUND:
                            throw new DFSFileNotExistException(source);
                        case MSG_ERROR_INVALID_PATH:
                            throw new DFSInvalidPathException(source);
                        case MSG_ERROR_SERVER_UNKNOWN_ERROR:
                            throw new ServerUnknownException();
                    }
                }
            } catch (IOException e) {
               throw new ServerUnknownException();
            }
        }
        @Override
        public void run() {
            startDownload();
        }
    }
}
