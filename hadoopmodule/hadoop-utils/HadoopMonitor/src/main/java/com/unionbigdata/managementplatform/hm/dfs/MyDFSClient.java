package com.unionbigdata.managementplatform.hm.dfs;

import com.unionbigdata.managementplatform.hm.dfs.exception.DFSFileNotExistException;
import org.apache.hadoop.hdfs.DFSClient;
import org.apache.hadoop.hdfs.protocol.DirectoryListing;
import org.apache.hadoop.hdfs.protocol.HdfsFileStatus;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 14-3-5.
 * Client for handling operations on DFS.
 */
public class MyDFSClient {
    DFSClient client ;

    private MyDFSClient() throws IOException {
        client = DFSClientFactory.newInstance();
    };

    public static MyDFSClient newInstance() throws IOException {
        return new MyDFSClient();
    }

    /**
     * List file items of given path
     * @param dir the path to list.it should be an directory
     * @return
     */
    public List<DFSFileItem> listPaths(String dir) throws IOException, DFSFileNotExistException {
        if(!client.exists(dir)){
            throw new DFSFileNotExistException("File or directory not exits : " + dir);
        }
        DirectoryListing listing = client.listPaths(dir, HdfsFileStatus.EMPTY_NAME);
        HdfsFileStatus[] statuses = listing.getPartialListing();
        return asList(statuses);
    }

    /**
     * Get information of a file item.
     * @param src path of directory or file.
     * @return
     * @throws java.io.IOException
     * @throws com.unionbigdata.managementplatform.hm.dfs.exception.DFSFileNotExistException
     */
    public DFSFileItem getFileInfo(String src) throws IOException, DFSFileNotExistException {
        if(!client.exists(src)){
            throw new DFSFileNotExistException("File or directory does not exist : " + src);
        }
        HdfsFileStatus status = client.getFileInfo(src);
        DFSFileItem item = status.isDir()
                ?asDFSDir(status)
                :asDFSFile(status);
        return item;
    }

    /**
     * delete a file or directory.If the src is a path ,it will delete all the files in the directory
     * @param src
     * @return
     * @throws com.unionbigdata.managementplatform.hm.dfs.exception.DFSFileNotExistException
     * @throws java.io.IOException
     */
    public boolean delete(String src) throws DFSFileNotExistException, IOException {
        if(!client.exists(src)){
            throw new DFSFileNotExistException("File or directory does not exist : " + src);
        }
        return client.delete(src,true);
    }

    /**
     * Rename a file or directory.
     * @param src  the file or directory to be renamed
     * @param dst  the new name of path of file or directory to be renamed to
     * @return
     * @throws java.io.IOException
     * @throws com.unionbigdata.managementplatform.hm.dfs.exception.DFSFileNotExistException
     */
    public boolean rename(String src,String dst) throws IOException, DFSFileNotExistException {
        if(!client.exists(src)){
            throw new DFSFileNotExistException("File or directory does not exist : " + src);
        }
        return client.rename(src,dst);
    }


    /**
     * Read files in hdfs.It uses the concept of chunk.If the chunk to read is not the last chunk,this will read
     * content in size of {@code chunkSize}
     * @param src the path of file to read
     * @param chunkSize size of chunk,must be positive
     * @param chunkNum number of chunk to read,start with 0
     * @return content if the chunk of the {@code chunkNum} exists.If the chunk of {@code chunkNum} doesn't exists
     * or the file of {@code src} is not a valid path of file,return empty {@code String} ""
     */
    public String read(String src, int chunkSize,int chunkNum){
        String content = "";
        if (src == null || chunkSize <= 0 || chunkNum <0){
            return content;
        }

        DFSClient.DFSInputStream inputStream = null;
        byte[] buffer = new byte[chunkSize];
        int realSize = 0;
        try {
            inputStream = client.open(src);
            inputStream.seek(chunkSize*chunkNum);
            realSize = inputStream.read(buffer);
        } catch (IOException e) {
            //file doesn't exist or invalid path of file
            return content;
        }finally {
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    //do nothing
                }
                ;
            }
        }

        return new String(buffer,0,realSize);
    }

    /**
     * Fill in a list with given status.
     * @param statuses
     * @return list of DFSFileItems which contains files and directories.
     */
    private List<DFSFileItem> asList(HdfsFileStatus[] statuses){
       List<DFSFileItem> list = new LinkedList<DFSFileItem>();

        for(HdfsFileStatus status : statuses){
            DFSFileItem item = status.isDir()
                    ?asDFSDir(status)
                    :asDFSFile(status);
            list.add(item);
        }
       return list;
    }

    /**
     * Transform a HdfsFileStatus which representing a file to a DFSFile.
     * @param status represent a file
     * @return
     */
    private DFSFile asDFSFile(HdfsFileStatus status){
        if(status.isDir()){
            throw new RuntimeException("status should be a file.");
        }
        DFSFile file = new DFSFile.Builder(status.getLocalName())
                .blockSize(status.getBlockSize())
                .group(status.getGroup())
                .owner(status.getOwner())
                .length(status.getLen())
                .lastModicicationTime(status.getModificationTime())
                .permission(status.getPermission().toString())
                .replication(status.getReplication())
                .newInstance();
        return file;
    }

    /**
     * Transform a HdfsStatus which representing a directory to a DFSDir
     * @param status
     * @return
     */
    private DFSDir asDFSDir(HdfsFileStatus status){
        if(!status.isDir()){
            throw new RuntimeException("status should be a directory");
        }
        DFSDir dir = new DFSDir.Builder(status.getLocalName())
                .group(status.getGroup())
                .lastModificationTime(status.getModificationTime())
                .owner(status.getOwner())
                .permission(status.getPermission().toString())
                .newInstance();
        return dir;
    }




}
