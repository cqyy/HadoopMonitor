package com.unionbigdata.managementplatform.crawlermodule.crawlerclient.managelocalapps;

import com.unionbigdata.managementplatform.crawlermodule.crawlerclient.databasemanage.DatabaseManager;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * 管理程序的运行，获取程序状态，杀死进程
 * 为单例类
 * 私有成员变量为list集合，里面存放运行单个程序的线程，线程中可以管理程序的流
 * Created by lwj on 14-3-20.
 */
public class AppManage {

    private List<threadRunApp> appThread;

    public static class NestedAppMange {
        public static final AppManage INSTANCE = new AppManage();
    }

    public static AppManage getInstance() {
        return NestedAppMange.INSTANCE;
    }

    private AppManage() {
        appThread = new LinkedList<threadRunApp>();
    }

    public List<threadRunApp> getAppThread() {
        return appThread;
    }

    /**
     * static function. use the path and name to run the app in the client.
     * call the threadRunApp class to achieve the multi-threading.
     * return the threadRunApp class generated in the function
     * @param path
     * @param name
     * @return
     * @throws IOException
     */
    public static threadRunApp runApp(String path, String name,String parameter) throws IOException {
        threadRunApp td = new threadRunApp();
        td.setPath(path);
        td.setName(name);
        td.setParameter(parameter);
        Thread tdr = new Thread(td);
        tdr.start();
        return td;
    }
    public static AppInfo getAppInfo(String path, String name){
        DatabaseManager dm = new DatabaseManager();
        return dm.getAppInfo(path, name);
    }

    public void killApp(int appPID, int signal){
        String command = "kill -"+signal+" "+appPID;
        Runtime rt = Runtime.getRuntime();
        Process p = null;
        try {
            p = rt.exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * stop app and update the information in database
     * @param pid
     * @return
     */
    public boolean killApp(int pid){
        boolean status = false;
        String command = "kill -15 "+ pid;
        Runtime rt = Runtime.getRuntime();
        Process p = null;
        try {
            p = rt.exec(command);
            System.out.println("stop process "+ pid +" succeed.");
            DatabaseManager dm = new DatabaseManager();
            AppInfo appInfo = dm.getAppInfo(pid);
            appInfo.setPid(0);
            appInfo.setRun(false);
            dm.updateAppInfo(appInfo);
            removeElem(pid);
            status = true;
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("stop process "+ pid +" failed.");
        }
        return status;
    }

    public LinuxProcessInfo getAppState(int pid){
        String[] command = new String[] { "/bin/sh", "-c", "ps aux|grep "+pid };
        Runtime rt = Runtime.getRuntime();
        Process p = null;
        BufferedReader br;
        LinuxProcessInfo linuxProcessInfo = null;
        try {
            p = rt.exec(command);
            br=new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            String[] st;
            while((line = br.readLine())!= null){
                st = line.split(" +");
                String cmd = "";
                for(int i = 10;i<st.length;i++){
                    cmd += st[i];
                }
                if(Integer.parseInt(st[1]) == pid){
                    linuxProcessInfo = new LinuxProcessInfo(st[0],Integer.parseInt(st[1]),Float.parseFloat(st[2]),Float.parseFloat(st[3]),st[4],st[5],st[6],st[7],st[8],st[9],cmd);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("stop process "+ pid +" failed.");
        }
        return linuxProcessInfo;
    }
    private void removeElem(int pid){
        for(int i = 0;i<appThread.size();i++){
            if(appThread.get(i).getPid() == pid){
                appThread.remove(i);
            }
        }
    }
}
