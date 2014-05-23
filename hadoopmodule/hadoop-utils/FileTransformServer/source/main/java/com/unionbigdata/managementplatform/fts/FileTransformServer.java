package com.unionbigdata.managementplatform.fts;

import com.unionbigdata.managementplatform.fts.conf.ConfAttributes;
import com.unionbigdata.managementplatform.fts.conf.ConfManager;
import com.unionbigdata.managementplatform.fts.hdfs.HDFSFileTransformer;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 14-3-7.
 */
public class FileTransformServer extends Thread{

    private final static int MAX_FILE_RECEIVER_THREAD = 5;
    private final static Logger logger = Logger.getLogger(FileTransformServer.class);

    ServerSocket serverSocket = null;
    ExecutorService executor;

    private static FileTransformServer server = new FileTransformServer();

    public static FileTransformServer getInstance(){
        return server;
    }

    private FileTransformServer() {
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getByName(
                    ConfManager.getAttribute(
                            ConfAttributes.DFS_FILERECEIVER_HOST));
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }

        int port = Integer.parseInt(
                ConfManager.getAttribute(
                        ConfAttributes.DFS_FILERECEIVER_PORT));
        try {
            serverSocket = new ServerSocket(port,MAX_FILE_RECEIVER_THREAD * 2,inetAddress);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        executor = Executors.newFixedThreadPool(MAX_FILE_RECEIVER_THREAD);
    }

    public void close(){
        if(serverSocket != null){
            try {
                serverSocket.close();
            } catch (IOException e) { }
        }
        logger.info("Server closed");
    }

    @Override
    public void run() {
        while (true){
            try {
                Socket socket = serverSocket.accept();
                FileTransformHandler handler = new HDFSFileTransformer();
                TransformHandler transformerThread = new TransformHandler(handler,socket);
                executor.submit(transformerThread);
                logger.info("received request from " + socket.getInetAddress());
            } catch (IOException e) {
               logger.error(e.getStackTrace());
            }
        }
    }

    public static void main(String[] args) {
        Thread fileThread = FileTransformServer.getInstance();
        fileThread.start();
        logger.info("Server started");
        System.out.println("service started");
    }
}
