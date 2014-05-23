package com.unionbigdata.managementplatform.crawlermodule.crawlerclient.communicate;

/**
 * Created by Administrator on 14-3-16.
 */

import com.unionbigdata.managementplatform.crawlermodule.crawlerclient.Other.FileAccess;
import com.unionbigdata.managementplatform.crawlermodule.crawlerclient.managelocalapps.AppInfo;
import com.unionbigdata.managementplatform.crawlermodule.crawlerclient.databasemanage.DatabaseManager;
import com.unionbigdata.managementplatform.crawlermodule.crawlerclient.managelocalapps.AppManage;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.web.bind.annotation.RequestParam;

import org.apache.commons.lang.StringEscapeUtils;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.List;

@Path("file")
public class UploadRestService {
    /**
     * upload single file, if the file already exist, then rename the file append the version in the file system and database;
     * then save file
     * 如果下次重传文件时版本和旧版本相同，数据库会报错,但不影响程序运行
     * @param is
     * @param fdcd
     * @param path
     * @param version
     * @return
     */
    @POST
    @Path("uploadsingle")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
//    @Produces({MediaType.TEXT_HTML,MediaType.ACCEPT_CHARSET="UTF-8"})
    public Response upload(@FormDataParam("file") InputStream is,
                           @FormDataParam("file") FormDataContentDisposition fdcd,
                           @FormDataParam("path") String path,@FormDataParam("version") String version) {
        //must get all parameter
        if(is == null || path == null || version == null){
            return Response.status(403).encoding("UTF-8").type(MediaType.TEXT_HTML_TYPE).entity("must provide all parameter!").build();
        }

        DatabaseManager dm = new DatabaseManager();

        createDirectory(path);
        String location = path + fdcd.getFileName();
        //if the upload file is duplicate, handle the old version file
        if (dm.isDuplicate(path,fdcd.getFileName())){
            renameFile(path,fdcd.getFileName());
            AppInfo appInfo = dm.getAppInfo(path,fdcd.getFileName());
            dm.delAppInfo(path,fdcd.getFileName());
            appInfo.setName(addVersionInName(appInfo.getName(),appInfo.getVersion()));
            dm.addAppInfo(appInfo);
        }
        saveFile(is, location);
        AppInfo appInfo = new AppInfo(fdcd.getFileName(),path,version,getFileSize(location));
        dm.addAppInfo(appInfo);
        return Response.status(200).encoding("UTF-8").type(MediaType.TEXT_HTML_TYPE).entity("file "+fdcd.getFileName()+" saved in: " + path).build();
    }

    @POST
    @Path("uploadAutoRun")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
//    @Produces({MediaType.TEXT_HTML,MediaType.ACCEPT_CHARSET="UTF-8"})
    public Response uploadRun(@FormDataParam("file") InputStream is,
                           @FormDataParam("file") FormDataContentDisposition fdcd,
                           @FormDataParam("path") String path,@FormDataParam("version") String version,@FormDataParam("parameter") String parameter) {
        Response response = upload(is, fdcd, path, version);
        AppControlService appControlService = new AppControlService();
        appControlService.runApp(path,fdcd.getFileName(),parameter);
        return response;
    }

    /**
     * the client send file to server. then save the file at the path. the path is the type of UNIX/LINUX and end of "/"
     * the client must notify the server about the version of the file.
     * @param form
     * @param path
     * @param version
     * @return
     */
    @POST
    @Path("upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_XML)
    public Response upload(@RequestParam("file") FormDataMultiPart form,
                                @FormDataParam("path") String path,@FormDataParam("version") String version) {
        if(form == null || path == null || version == null){
            return Response.status(403).encoding("UTF-8").type(MediaType.TEXT_HTML_TYPE).entity("must provide all parameter!").build();
        }
        String location;
        createDirectory(path);
        List<FormDataBodyPart> l = form.getFields("file");
        DatabaseManager dm = new DatabaseManager();
        for (FormDataBodyPart p : l) {
            InputStream ist = p.getValueAs(InputStream.class);
            FormDataContentDisposition detail = p.getFormDataContentDisposition();
            String strDecode = StringEscapeUtils.unescapeHtml(detail.getFileName());
            location = path + strDecode;
            saveFile(ist, location);
            AppInfo appInfo = new AppInfo(detail.getFileName(), path,version,getFileSize(location));
            dm.addAppInfo(appInfo);
        }
        return Response.status(200).encoding("UTF-8").type(MediaType.TEXT_HTML_TYPE).entity("file saved in: " + path).build();
    }

    /**
     * private function. the upload function will call it to implement the way to save files in local system.
     * @param is
     * @param dir
     * @return
     */
    private boolean saveFile(InputStream is, String dir) {
        boolean status = false;
        File file = new File(dir);
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            byte[] b = new byte[1024];
            int readLen = -1;
            while((readLen = is.read(b)) != -1) {
                os.write(b, 0, readLen);
            }
            os.flush();
            status = true;
        } catch (IOException e) {
            e.printStackTrace();
            status = false;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return status;
    }

    /**
     * To test is the directory exist or not.If do not exist return false.then create the directory.
     * @param dir
     * @return
     */
    private boolean createDirectory(String dir){
        File f = new File(dir);
        return f.mkdirs();
    }

    private long getFileSize(String location){
        File file = new File(location);
        long size = 0;
        try {
            InputStream is = new FileInputStream(file);
            size = is.available();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * stop app, copy file, delete file,save new version file
     * @param path
     * @param name
     * @return
     */
    private boolean renameFile(String path,String name){
        boolean status = false;
        DatabaseManager dm = new DatabaseManager();
        AppInfo appInfo = dm.getAppInfo(path, name);
        String nameWithVersion = addVersionInName(name, appInfo.getVersion());
        if(appInfo.isRun()){
            AppManage appManage = AppManage.getInstance();
            appManage.killApp(appInfo.getPid());
        }
        try {
            FileAccess.Copy(path+name,path+nameWithVersion);
            status = FileAccess.delFile(path+name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return status;
    }
    private String addVersionInName(String name, String version){
        String[] st = name.split("\\.");
        String s1 = null;
        for(int i = 0;i<st.length-2;i++){
            if(s1 != null){
                s1 = s1+st[i]+".";
            }else{
                s1 = st[i]+".";
            }
        }
        if (s1 != null){
            s1 = s1+st[st.length-2]+"-"+version+"."+st[st.length-1];
        }else{
            s1 =st[st.length-2]+"-"+version+"."+st[st.length-1];
        }
        return s1;
    }
}