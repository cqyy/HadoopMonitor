package com.unionbigdata.managementplatform.fts;

import com.unionbigdata.managementplatform.fts.hdfs.DFSClientFactory;
import org.apache.hadoop.hdfs.DFSClient;
import org.apache.hadoop.hdfs.protocol.HdfsFileStatus;
import org.apache.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Administrator on 14-3-17.
 */
public class TransformHandler implements Runnable {

    private static Logger logger = Logger.getLogger(TransformHandler.class);

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


    private FileTransformHandler transformer;
    private Socket connection;
    private DataInputStream inputStream = null;
    private DataOutputStream outputStream = null;

    protected TransformHandler(FileTransformHandler transformHandler, Socket connection) throws IOException {
        if (transformHandler == null || connection == null) {
            throw new NullPointerException();
        }
        this.transformer = transformHandler;
        this.connection = connection;
        inputStream = new DataInputStream(connection.getInputStream());
        outputStream = new DataOutputStream(connection.getOutputStream());
    }


    /**
     * Communicate with the client to get the transform type.
     *
     * @return {@code MSG_TRANSFORM_TYPE_UPLOAD} or {@code MSG_TRANSFORM_TYPE_DOWNLOAD}
     */
    protected int getTransformType() throws IOException {
        outputStream.writeInt(MSG_REQUEST_TRANSFORM_TYPE);
        outputStream.flush();
        int response = inputStream.readInt();
        return response;

    }

    protected String getDestination() throws IOException {
        outputStream.writeInt(MSG_REQUEST_DESTINATION);
        outputStream.flush();
        String dst = inputStream.readUTF();
        if (dst == null){
            dst = "";
        }
        return dst;
    }

    protected String getResource() throws IOException {
        outputStream.writeInt(MSG_REQUEST_SOURCE);
        outputStream.flush();
        String source = inputStream.readUTF();
        return source;
    }

    protected void sendFileLength(long length) throws IOException {
        outputStream.writeInt(MSG_RESPONSE_FILE_LENGTH);
        outputStream.writeLong(length);
        outputStream.flush();
    }

    protected void endTransform() throws IOException {
        outputStream.writeInt(MSG_SINGNAL_EXIT);
        outputStream.flush();

        if (inputStream != null){
            try{
                inputStream.close();
            }catch (IOException nouse){/*do nothing*/}
        }

        if (outputStream != null){
            try{
                outputStream.close();
            }catch (IOException nouse){/*do nothing*/}
        }
        if (connection != null){
            try{
                connection.close();
            }catch (IOException nouse){/*do nothing*/}
        }

    }

    protected void transform() throws IOException {
        int type = getTransformType();
        if (type == MSG_TRANSFORM_TYPE_UPLOAD) {
            handleUpload();
        } else if (type == MSG_TRANSFORM_TYPE_DOWNLOAD) {
            handleDownload();
        }
    }

    protected void handleUpload() {
        DFSClient client = null;
        try {
            String dst = getDestination();
            client = DFSClientFactory.newInstance();
            if (client.exists(dst)) {
                outputStream.writeInt(MSG_ERROR_FILE_EXISTS);
                outputStream.flush();
            } else {
                outputStream.writeInt(MSG_SINGNAL_START_UPLOAD);
                outputStream.flush();
                DataInputStream in = new DataInputStream(connection.getInputStream());
                transformer.upload(in, dst);
            }
            client.close();
        } catch (IOException e) {
            logger.error(e.getStackTrace());
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    //do nothing
                }
            }
            try {
                endTransform();
            } catch (IOException e) {
                //do nothing
            }
        }
    }

    protected void handleDownload() {
        DFSClient client = null;

        try {
            String source = getResource();
            client = DFSClientFactory.newInstance();
            long length = 0;
            if (!client.exists(source)) {
                outputStream.writeInt(MSG_ERROR_FILE_NOT_FOUND);
                outputStream.flush();
            } else {
                HdfsFileStatus status = client.getFileInfo(source);
                if (status.isDir()) {
                    outputStream.writeInt(MSG_ERROR_INVALID_PATH);
                    outputStream.flush();
                } else {
                    length = status.getLen();
                    sendFileLength(length);
                    outputStream.writeInt(MSG_SINGNAL_START_DOWNLOAD);
                    transformer.download(source, outputStream);
                }
            }
        } catch (IOException e) {
           logger.error(e.getStackTrace());
        } catch (RuntimeException re){
            logger.error(re.getStackTrace());
        }finally {

            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    //do nothing
                }
            }
            try {
                endTransform();
            } catch (IOException e) {
                //do nothing
            }
        }
    }

    @Override
    public void run() {
        try {
            transform();
        } catch (IOException e) {
            logger.error(e.getStackTrace());
        }
    }
}
