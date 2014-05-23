package com.unionbigdata.managementplatform.fts.hdfs;

import com.unionbigdata.managementplatform.fts.FileTransformHandler;
import org.apache.hadoop.hdfs.DFSClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 14-3-7.
 * Save file to HDFS.
 */
public class HDFSFileTransformer implements FileTransformHandler {

    private static final int BUFFER_SIZE = 4096;

    @Override
    public void upload(DataInputStream inputStream, String destination) throws IOException {

        if (inputStream == null || destination ==null){
            throw new NullPointerException();
        }

        DFSClient client = null;
        DataOutputStream outputStream = null;

        try {
            client = DFSClientFactory.newInstance();
            outputStream = new DataOutputStream(client.create(destination,false));
            byte[] buffer = new byte[BUFFER_SIZE];
            int i = 0;
            while ( (i = inputStream.read(buffer))!= -1 ){
                outputStream.write(buffer,0,i);
            }
        } catch (IOException e) {
            if (client == null) {
                System.err.println("could not create DFSClient");
                System.err.println(e.getMessage());
                System.exit(1);
            }
            throw new IOException("the filename is invalid or the file exists : " + destination);
        } finally {
            if ( client!= null){
                client.close();
            }

            if (outputStream != null){
                try {
                    outputStream.close();
                }catch (IOException nouse){
                    //do nothing
                }
            }

            try {
                inputStream.close();
            }catch (IOException nouse){
                //do nothing
            }
        }
    }

    @Override
    public void download(String source, DataOutputStream outputStream) throws IOException {
        if(source == null || outputStream == null){
            throw new NullPointerException();
        }

        DFSClient client = null;
        DataInputStream inputStream = null;
        try{
            client = DFSClientFactory.newInstance();


            inputStream = new DataInputStream(client.open(source));

            byte[] buffer = new byte[BUFFER_SIZE];
            int i = 0;
            while ( (i = inputStream.read(buffer)) != -1 ){
                outputStream.write(buffer,0,i);
            }
        }catch (IOException e){
            if (client == null){
                System.err.println("could not create DFSClient");
                System.err.println(e.getMessage());
                System.exit(1);
            }
            throw new IOException("the filename doesn't exist or is invalid");
        }finally {
            if (client != null){
                client.close();
            }

            if ( inputStream!= null){
                try{
                    inputStream.close();
                }catch (IOException nouse){
                    //do nothing
                }
            }

            try{
                outputStream.close();
            }catch (IOException nouse){
                //do nothing
            }
        }
    }
}
