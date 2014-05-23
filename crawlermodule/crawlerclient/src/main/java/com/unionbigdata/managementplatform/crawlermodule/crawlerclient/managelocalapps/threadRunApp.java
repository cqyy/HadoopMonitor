package com.unionbigdata.managementplatform.crawlermodule.crawlerclient.managelocalapps;

import com.unionbigdata.managementplatform.crawlermodule.crawlerclient.databasemanage.DatabaseManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

/**
 * Created by lwj on 14-3-20.
 */
public class threadRunApp implements Runnable {
    private String path;
    private String name;
    private String parameter;
    private int pid;
    private boolean isRun;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParameter() {return parameter;}

    public void setParameter(String parameter) {this.parameter = parameter;}

    public int getPid() {return pid;}

    public boolean isRun() {return isRun;}

    public void run() {
        BufferedReader br = null;
        isRun = true;
        //get command to run the application;
        String command;
        if(parameter != null){
            command = "java -jar " + path+name+" "+parameter;
        }else{
            command = "java -jar " + path+name;
        }
        Runtime rt = Runtime.getRuntime();
        Process p = null;
        try {
            //execute and get pid
            p = rt.exec(command);
            Class clazz = Class.forName("java.lang.UNIXProcess");
            Field pidField = clazz.getDeclaredField("pid");
            pidField.setAccessible(true);
            Object value = pidField.get(p);
            System.out.println(value);
            pid = Integer.parseInt(value.toString());
            isRun = true;
            //database update
            DatabaseManager dm = new DatabaseManager();
            AppInfo appInfo = dm.getAppInfo(path,name);
            appInfo.setRun(isRun);
            appInfo.setPid(pid);
            dm.updateAppInfo(appInfo);
            //print the jar massage;
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String msg = null;
            while ((msg = br.readLine()) != null) {
                System.out.println(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
