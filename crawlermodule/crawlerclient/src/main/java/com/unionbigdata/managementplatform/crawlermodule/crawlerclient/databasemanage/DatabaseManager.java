package com.unionbigdata.managementplatform.crawlermodule.crawlerclient.databasemanage;

import com.unionbigdata.managementplatform.crawlermodule.crawlerclient.managelocalapps.AppInfo;

import java.sql.*;

/**
 * Created by lwj on 14-3-24.
 */
public class DatabaseManager {

    /**
     * get file form service then log the massage in database.
     * type is | name  | path       | pid  | version | isRun | command | size | creationdate | permissions | cpu  | time | mem  |
     * @param appInfo
     */
    public void addAppInfo(AppInfo appInfo){
        Statement statement = null;
        ResultSet rs = null;

        DBConnectionManager dbConnectionManager = DBConnectionManager.getInstance();
        Connection conn = dbConnectionManager.getConnection("mypool");
        try {
            String insert = "insert into file values ('"+ appInfo.getName()+"','"+ appInfo.getPath()+"',"+appInfo.getPid()+",'"+ appInfo.getVersion()+"',"+appInfo.isRun()+","+appInfo.getCommand()+","+ appInfo.getSize()+","+appInfo.getCreationDate()+","+appInfo.getPermissions()+","+appInfo.getCpu()+","+appInfo.getTime()+","+appInfo.getMem()+");";
            System.out.println(insert);
            statement = conn.createStatement();
            statement.executeUpdate(insert);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("add App successfully in database.");
    }
    public void updateAppInfo(AppInfo appInfo){
        delAppInfo(appInfo.getPath(),appInfo.getName());
        addAppInfo(appInfo);
        System.out.println("update App successfully in database.");
    }

//    public void updateAppVersion(String path,String name){
//        Statement statement = null;
//        ResultSet rs = null;
//
//        DBConnectionManager dbConnectionManager = DBConnectionManager.getInstance();
//        Connection conn = dbConnectionManager.getConnection("mypool");
//
//        //处理重名文件,kill app then rename app then
//        //get old version of the file
//        String sql = "select version from file where name = '"+name+"' and path = '"+path+"';";
//        System.out.println(sql);
//        try {
//            statement = conn.createStatement();
//            rs = statement.executeQuery(sql);
//            rs.next();
//            String nameAndOldVersion = addVersionInName(rs.getString("version"),name);
//            sql =  "update file set name = "+nameAndOldVersion+" where name = '"+name+"' and path = '"+path+"';";
//            System.out.println(sql);
//            //rename the old file to new name(use the nameAndOldVersion)
//            statement.executeUpdate(sql);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }finally {
//            if (rs != null) {
//                try {
//                    rs.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (statement != null) {
//                try {
//                    statement.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
    /**
     * get the file massage from the data base.getConnection need the name of the DBConnectionPool name.
     * then generate a AppInfo class from the result which query from the Database
     * return the AppInfo class
     * @param name
     * @param path
     * @return
     */
    public AppInfo getAppInfo(String path, String name){
        ResultSet rs = null;
        Statement statement = null;
        AppInfo ai = null;
        DBConnectionManager dbConnectionManager = DBConnectionManager.getInstance();
        Connection conn = dbConnectionManager.getConnection("mypool");
        try {
            statement = conn.createStatement();
            String sql = "select * from file where name = '"+name+"' and path = '"+path+"';";
            System.out.println(sql);
            rs= statement.executeQuery(sql);
            rs.next();
            ai = new AppInfo(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getString(4),rs.getBoolean(5),rs.getString(6),rs.getLong(7),rs.getTime(8),rs.getShort(9),rs.getFloat(10),rs.getTime(11),rs.getLong(12));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return ai;
    }
    public AppInfo getAppInfo(int pid){
        ResultSet rs = null;
        Statement statement = null;
        AppInfo ai = null;
        DBConnectionManager dbConnectionManager = DBConnectionManager.getInstance();
        Connection conn = dbConnectionManager.getConnection("mypool");
        try {
            statement = conn.createStatement();
            String sql = "select * from file where pid = "+pid+";";
            System.out.println(sql);
            rs= statement.executeQuery(sql);
            rs.next();
            ai = new AppInfo(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getString(4),rs.getBoolean(5),rs.getString(6),rs.getLong(7),rs.getDate(8),rs.getShort(9),rs.getFloat(10),rs.getTime(11),rs.getLong(12));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return ai;
    }

    /**
     * when the app is started, use this function to set the app PID and isRun to true.
     * location the app use the app name and path.
     * select
     * @param path
     * @param name
     * @param pid
     * @return
     */
    public boolean setAppPID(String path, String name, int pid){
        Statement statement = null;
        boolean status = false;
        DBConnectionManager dbConnectionManager = DBConnectionManager.getInstance();
        Connection conn = dbConnectionManager.getConnection("mypool");
        try {
            statement = conn.createStatement();
            String sql = "update file set pid = "+pid+", isRun = true where name = '"+name+"' and path = '"+path+"';";
            System.out.println(sql);
            int count= statement.executeUpdate(sql);
            System.out.println(count+"file(s) be modified. ");
            status = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return status;
    }

    public boolean delAppInfo(String path, String name){
        Statement statement = null;
        boolean status = false;

        DBConnectionManager dbConnectionManager = DBConnectionManager.getInstance();
        Connection conn = dbConnectionManager.getConnection("mypool");
        try {
            statement = conn.createStatement();
            String sql = "delete from file where name = '"+name+"' and path = '"+path+"';";
            System.out.println(sql);
            int count = statement.executeUpdate(sql);
            System.out.println(count+"file(s) be deleted. ");
            status = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return status;
    }

    /**
     * whether the file already exist
     * @param path
     * @param name
     * @return
     */
    public boolean isDuplicate(String path,String name){
        Statement statement = null;
        ResultSet rs = null;
        boolean status = false;

        DBConnectionManager dbConnectionManager = DBConnectionManager.getInstance();
        Connection conn = dbConnectionManager.getConnection("mypool");
        try {
            statement = conn.createStatement();
            String sql = "select * from file where name = '"+name+"' and path = '"+path+"';";
            System.out.println(sql);
            rs = statement.executeQuery(sql);
            rs.next();
            if(rs.getString("path").compareTo(path) == 0 && rs.getString("name").compareTo(name) == 0){
                status = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return status;
    }

}
