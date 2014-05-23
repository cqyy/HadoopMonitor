
package com.unionbigdata.managementplatform.ws;

import com.google.gson.Gson;
import com.unionbigdata.managementplatform.hm.dfs.Cluster;
import com.unionbigdata.managementplatform.hm.dfs.DFSFileItem;
import com.unionbigdata.managementplatform.hm.dfs.MyClusterClient;
import com.unionbigdata.managementplatform.hm.dfs.MyDFSClient;
import com.unionbigdata.managementplatform.hm.dfs.exception.DFSFileNotExistException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 14-3-10.
 * Proxy providing web service about DFS operation
 */
@Path("dfs")
public class DFSProxy {
    MyClusterClient clusterClient = null;
    MyDFSClient dfsClient = null;

    public DFSProxy(){
        try {
            clusterClient = MyClusterClient.newInstance();
            dfsClient = MyDFSClient.newInstance();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GET
    @Path("getClusterInfo")
    @Produces(MediaType.TEXT_PLAIN)
    public String getClusterInfo(){
        Gson gson = new Gson();
        Cluster cluster = null;
        try {
            cluster = clusterClient.getClusterInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gson.toJson(cluster);
    }

    @GET
    @Path("listPath")
    @Produces(MediaType.TEXT_PLAIN)
    public String listPath( @QueryParam("path") String path){
        Gson gson = new Gson();
        List<DFSFileItem> files = null;
        try {
            files = dfsClient.listPaths(path);
            return gson.toJson(files);
        } catch (IOException e) {
        } catch (DFSFileNotExistException e) {
        }finally {
            return gson.toJson(files);
        }
    }

    @GET
    @Path("getFileInfo")
    @Produces(MediaType.TEXT_PLAIN)
    public String getFileInfo( @QueryParam("src") String src){
        DFSFileItem fileItem = null;
        Gson gson = new Gson();
        try {
            fileItem = dfsClient.getFileInfo(src);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DFSFileNotExistException e) {
            e.printStackTrace();
        }finally {
            return gson.toJson(fileItem);
        }
    }
    @GET
    @Path("rename")
    @Produces(MediaType.TEXT_PLAIN)
    public boolean rename(@QueryParam("old") String old, @QueryParam("dst") String dst){
        try {
            return dfsClient.rename(old,dst);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DFSFileNotExistException e) {
            e.printStackTrace();
        }
        return false;
    }
    @GET
    @Path("delete")
    @Produces(MediaType.TEXT_PLAIN)
    public boolean delete( @QueryParam("src") String src){
        try {
            return dfsClient.delete(src);
        } catch (DFSFileNotExistException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @GET
    @Path("read")
    @Produces(MediaType.TEXT_PLAIN)
    public String read(@QueryParam("src") String src,
                       @QueryParam("chunkSize") int chunkSize,
                       @QueryParam("chunkNum") int chunkNum){
        return dfsClient.read(src,chunkSize,chunkNum);
    }
}
