package com.unionbigdata.managementplatform.crawlermodule.crawlerclient.managelocalapps;

import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Time;
import java.util.Date;

/**
 * Created by lwj on 14-3-20.
 */
@XmlRootElement
public class AppInfo {
    private String name;
    private String path;
    private int pid;
    private String version;
    private boolean isRun;
    private String command;
    private long size;
    private Date creationDate;
    private short permissions;
    private float cpu;
    private Time time;
    private long mem;

    public AppInfo() {
    }

    public AppInfo(String name, String path, String version, long size) {
        this.name = name;
        this.path = path;
        this.version = version;
        this.size = size;
    }

    public AppInfo(String name, String path, int pid, String version, boolean isRun, String command, long size, Date creationDate, short permissions, float cpu, Time time, long mem) {
        this.name = name;
        this.path = path;
        this.pid = pid;
        this.version = version;
        this.isRun = isRun;
        this.command = command;
        this.size = size;
        this.creationDate = creationDate;
        this.permissions = permissions;
        this.cpu = cpu;
        this.time = time;
        this.mem = mem;
    }

    public AppInfo(String name, String path, int pid, String version, boolean isRun, String command, long size, short permissions, float cpu, long mem) {
        this.name = name;
        this.path = path;
        this.pid = pid;
        this.version = version;
        this.isRun = isRun;
        this.command = command;
        this.size = size;
        this.permissions = permissions;
        this.cpu = cpu;
        this.mem = mem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isRun() {
        return isRun;
    }

    public void setRun(boolean isRun) {
        this.isRun = isRun;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public short getPermissions() {
        return permissions;
    }

    public void setPermissions(short permissions) {
        this.permissions = permissions;
    }

    public float getCpu() {
        return cpu;
    }

    public void setCpu(float cpu) {
        this.cpu = cpu;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public long getMem() {
        return mem;
    }

    public void setMem(long mem) {
        this.mem = mem;
    }
}
