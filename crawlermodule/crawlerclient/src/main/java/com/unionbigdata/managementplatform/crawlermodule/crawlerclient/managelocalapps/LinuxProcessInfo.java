package com.unionbigdata.managementplatform.crawlermodule.crawlerclient.managelocalapps;

/**
 * 用于进程查询中，与linux下ps aux命令
 * Created by lwj on 14-4-7.
 */
public class LinuxProcessInfo {
    private String USER;
    private int PID;
    private float cpu_percent;
    private float mem_percent;
    private String VSZ;
    private String RSS;
    private String TTY;
    private String STAT;
    private String START;
    private String TIME;
    private String COMMAND;

    public LinuxProcessInfo(String USER, int PID, float cpu_percent, float mem_percent, String VSZ, String RSS, String TTY, String STAT, String START, String TIME, String COMMAND) {
        this.USER = USER;
        this.PID = PID;
        this.cpu_percent = cpu_percent;
        this.mem_percent = mem_percent;
        this.VSZ = VSZ;
        this.RSS = RSS;
        this.TTY = TTY;
        this.STAT = STAT;
        this.START = START;
        this.TIME = TIME;
        this.COMMAND = COMMAND;
    }

    public LinuxProcessInfo() {
    }

    public String getUSER() {
        return USER;
    }

    public void setUSER(String USER) {
        this.USER = USER;
    }

    public int getPID() {
        return PID;
    }

    public void setPID(int PID) {
        this.PID = PID;
    }

    public float getCpu_percent() {
        return cpu_percent;
    }

    public void setCpu_percent(float cpu_percent) {
        this.cpu_percent = cpu_percent;
    }

    public float getMem_percent() {
        return mem_percent;
    }

    public void setMem_percent(float mem_percent) {
        this.mem_percent = mem_percent;
    }

    public String getVSZ() {
        return VSZ;
    }

    public void setVSZ(String VSZ) {
        this.VSZ = VSZ;
    }

    public String getRSS() {
        return RSS;
    }

    public void setRSS(String RSS) {
        this.RSS = RSS;
    }

    public String getTTY() {
        return TTY;
    }

    public void setTTY(String TTY) {
        this.TTY = TTY;
    }

    public String getSTAT() {
        return STAT;
    }

    public void setSTAT(String STAT) {
        this.STAT = STAT;
    }

    public String getSTART() {
        return START;
    }

    public void setSTART(String START) {
        this.START = START;
    }

    public String getTIME() {
        return TIME;
    }

    public void setTIME(String TIME) {
        this.TIME = TIME;
    }

    public String getCOMMAND() {
        return COMMAND;
    }

    public void setCOMMAND(String COMMAND) {
        this.COMMAND = COMMAND;
    }
}
