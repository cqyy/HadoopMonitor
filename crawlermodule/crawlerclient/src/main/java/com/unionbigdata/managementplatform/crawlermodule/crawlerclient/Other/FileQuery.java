package com.unionbigdata.managementplatform.crawlermodule.crawlerclient.Other;

import javax.ws.rs.FormParam;
import javax.ws.rs.PathParam;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "user")
/**
 * Created by lwj on 14-3-19.
 */
public class FileQuery {
    @PathParam("userName")
    private String userName;
    @FormParam("fileName")
    private String fileName;

    private String filePath;
    private boolean isRun;

    public FileQuery(String fileName, String filePath, boolean isRun) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.isRun = isRun;
    }

    public FileQuery() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isRun() {
        return isRun;
    }

    public void setRun(boolean isRun) {
        this.isRun = isRun;
    }
}
