package com.unionbigdata.managementplatform.crawlermodule.crawlerclient.communicate;

import com.unionbigdata.managementplatform.crawlermodule.crawlerclient.managelocalapps.LinuxProcessInfo;
import com.unionbigdata.managementplatform.crawlermodule.crawlerclient.managelocalapps.AppInfo;
import com.unionbigdata.managementplatform.crawlermodule.crawlerclient.managelocalapps.AppManage;
import com.unionbigdata.managementplatform.crawlermodule.crawlerclient.managelocalapps.threadRunApp;
import org.glassfish.jersey.media.multipart.FormDataParam;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("control")
/**
 * Created by lwj on 14-3-26.
 */
public class AppControlService {
    /**
     * 启动程序，所需参数路径名path,文件名name,运行参数parameter(可选)
     * 如果参数是管道之类都命令，会报错，暂时不提供此功能
     * @param path
     * @param name
     * @param parameter
     * @return
     */
    @POST
    @Path("start")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public String runApp(@FormDataParam("path") String path, @FormDataParam("name") String name, @FormDataParam("parameter") String parameter){
        if(path == null||name == null){
            return "You must give me the path and the name.";
        }
        System.out.println("start app :"+path+name+"parameter is "+parameter);
        try {
            threadRunApp th1 = AppManage.runApp(path,name,parameter);
            AppManage am = AppManage.getInstance();
            am.getAppThread().add(th1);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("start app failed!");
            return "run app failed!";
        }
        return "run app ok";
    }

    /**
     * 从数据库查询程序运行的消息，所需参数有路径名path,文件名name
     * @param path
     * @param name
     * @return
     */
    @POST
    @Path("query")
    @Produces(MediaType.APPLICATION_XML)
    public AppInfo getAppInfo(@FormDataParam("path") String path, @FormDataParam("name") String name) {
        AppInfo appInfo = AppManage.getAppInfo(path, name);
        if(appInfo == null){
  //          return "there is no this app!";
            appInfo = new AppInfo();
        }
        return appInfo;
    }

    /**
     * 从数据库查询程序运行的消息，所需参数有进程pid
     * @param pid
     * @return
     */
    @POST
    @Path("queryRunningApp")
    @Produces(MediaType.APPLICATION_XML)
    public LinuxProcessInfo getAppRunningInfo(@FormDataParam("pid") int pid) {
        AppManage appManage = AppManage.getInstance();
        LinuxProcessInfo linuxProcessInfo = appManage.getAppState(pid);
        if(linuxProcessInfo != null){
            return linuxProcessInfo;
        }else {
            linuxProcessInfo = new LinuxProcessInfo();
            return linuxProcessInfo;
        }
    }

    /**
     * 杀死进程，所需参数有进程pid
     * @param pid
     * @return
     */
    @POST
    @Path("stop")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public String stopApp(@FormDataParam("pid") int pid){
        AppManage appManage = AppManage.getInstance();
        appManage.killApp(pid);
        return "stop app successfully!";
    }
}
