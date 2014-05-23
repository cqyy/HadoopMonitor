package com.unionbigdata.managementplatform.ftc.io;

import com.unionbigdata.managementplatform.ftc.conf.ConfAttributes;
import com.unionbigdata.managementplatform.ftc.conf.ConfManager;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * FileTransformClient is the primary interface for user to transform file with the server.
 * It is a singleton.
 * <p>Example:</p>
 * <blockquote><pre>
 *     FileTransformClient client = FileTransformClient.getInstance();
 *     ProcessReporter reporter = client.uploadFile("", "", true);
 *     while (!reporter.isCompleted()){
 *           float process = reporter.process();
 *           //doSomething(process);
 *        }
 *     if(!reporter.isSucceed()){
 *           ErrorType error = reporter.getError();
 *           //doSomething(error)
 *       }
 * </pre></blockquote>
 */
public class FileTransformClient {
    private static final String SERVER_HOST = ConfManager.getAttribute(
            ConfAttributes.DFS_SERVER_HOST);

    private static final int SERVER_PORT = Integer.valueOf(
            ConfManager.getAttribute(
                    ConfAttributes.DFS_SERVER_PORT));

    private static final int MAX_RECONNECTION_TIME = Integer.parseInt(
            ConfManager.getAttribute(
                    ConfAttributes.MAX_CONNECT_RETRY_TIME));       //retry times if socket connection failed to build

    private static final int MAX_TRANSFORM_THREAD = Integer.valueOf(
            ConfManager.getAttribute(
                    ConfAttributes.MAX_PROCESS_NUM));

    private static ExecutorService executor;

    private static FileTransformClient transformer = new FileTransformClient();

    private FileTransformClient() {
        if (MAX_TRANSFORM_THREAD > 0) {
            executor = Executors.newFixedThreadPool(MAX_TRANSFORM_THREAD);
        } else {
            executor = Executors.newCachedThreadPool();
        }

    }

    /**
     * Get instance of the FileTransformClient.
     * @return instance
     */
    public static FileTransformClient getInstance() {
        return transformer;
    }

    private Socket createConnection() {
        Socket socket;
        int times = 0;
        do {
            try {
                socket = new Socket(SERVER_HOST, SERVER_PORT);
                if (socket.isConnected()) {
                    //connection created successful
                    return socket;
                }
            } catch (IOException e) {
                //do noting
            }
            times++;
        } while (times < MAX_RECONNECTION_TIME);

        System.err.println("Can't not create connection to server : " + SERVER_HOST + ":" + SERVER_PORT);
        return null;
    }

    /**
     * <p>Upload file to server.</p>
     * <p>Thread safe</p>
     * <p>The {@link FileTransformClient} will use one thread for each transform process to handle it.
     * If the amount of current  running transform process  reaches or exceeds the {@code MAX_PROCESS_NUM}
     * defined in the configuration file,
     * new transform process will wait until formers to finish.</p>
     * <p>If the process failed,the error information will be stored in the {@link ProcessReporter}</p>
     * @param dir
     *        path of file to upload.It should be a valid path of a existing file
     * @param destination
     *        path of file to store the file in server.It should be a valid path of a nonexistent file at server.
     * @param syn
     *        whether the ProcessReporter of this process is synchronous.
     * @return {@link BlockingProcessReporter} if {@code syn == true}
     * or {@link UnblockingProcessReporter} if {@code syn == flase}
     */
    public synchronized ProcessReporter uploadFile(String dir, String destination, boolean syn) {
        File file = new File(dir);
        MyProcessReporter reporter = (syn)
                ? new BlockingProcessReporter(Long.MAX_VALUE)
                : new UnblockingProcessReporter(Long.MAX_VALUE);
        if (destination.trim().length() == 0) {
            reporter.setError(ErrorType.InvalidHdfsFileDirectory);
            reporter.setCompleted();
            return reporter;
        }

        if (!file.exists()) {
            reporter.setError(ErrorType.LocalFileNotFound);
            reporter.setCompleted();
            return reporter;
        }

        if (!file.isFile()) {
            reporter.setError(ErrorType.InvalidLocalFileDirectory);
            reporter.setCompleted();
            return reporter;
        }
        reporter.setTotalBytes(file.length());
        Socket connection = createConnection();
        if (connection == null) {
            reporter.setError(ErrorType.ServerUnreachable);
            reporter.setCompleted();
            return reporter;
        }

        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException nouse) {
            //would not get in there
        }

        //chang the '\' to '/' in destination path,the hdfs uses the '/' to separate the directory
        destination = destination.replaceAll("\\\\","/");

        FileTransformThread.UploadThread uploadThread = new FileTransformThread.UploadThread(
                connection,
                inputStream,
                destination,
                reporter);
        executor.submit(uploadThread);
        return reporter;
    }

    /**
     * <p>Download a file from server</p>
     * <p>Thread safe</p>
     * <p>The {@link FileTransformClient} will use one thread for each transform process to handle it.
     * If the amount of current  running transform process  reaches or exceeds the {@code MAX_PROCESS_NUM}
     * defined in the configuration file,
     * new transform process will wait until formers to finish.</p>
     * <p>If the process failed,the error information will be stored in the {@link ProcessReporter}</p>
     * @param source
     *        path of file at server to download.It should be a valid path of a existing file at server
     * @param destination
     *        path to store the file.It should be a valid path of a nonexistent file at local.
     * @param syn
     *       whether the ProcessReporter of this process is synchronous.
     * @return
     *       {@link BlockingProcessReporter} if {@code syn == true}
     *       or {@link UnblockingProcessReporter} if {@code syn == flase}
     */
    public synchronized ProcessReporter downloadFile(String source, String destination, boolean syn) {
        File file = new File(destination);
        MyProcessReporter reporter = (syn)
                ? new BlockingProcessReporter(Long.MAX_VALUE)
                : new UnblockingProcessReporter(Long.MAX_VALUE);

        if (file.exists()) {
            reporter.setError(ErrorType.LocalFileExist);
            reporter.setCompleted();
            return reporter;
        }

        if (destination.trim().length() == 0 || destination.endsWith("\\")) {
            reporter.setError(ErrorType.InvalidLocalFileDirectory);
            reporter.setCompleted();
            return reporter;
        }

        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Socket connection = createConnection();
        if (connection == null) {
            reporter.setError(ErrorType.ServerUnreachable);
            reporter.setCompleted();
            return reporter;
        }
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            //could get here
        }
        FileTransformThread.DownloadThread downloadThread = new FileTransformThread.DownloadThread(connection,
                source,
                reporter,
                outputStream);
        executor.submit(downloadThread);
        return reporter;
    }

    /**
     * Shut down the transform service.
     * <p>Thread safe</p>
     * <p>This will reject all transform processes not running currently and wait for
     * all running processes  to finish.</p>
     * <p>Do this only when you want to exit the system.</p>
     */
    public synchronized void shutdownAfterComplete() {
        executor.shutdown();
    }
}
