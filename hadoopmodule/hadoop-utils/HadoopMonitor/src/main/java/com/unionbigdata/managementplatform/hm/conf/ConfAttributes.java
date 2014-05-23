package com.unionbigdata.managementplatform.hm.conf;

/**
 * Created by Administrator on 14-3-12.
 * System configuration attributes
 */
public enum  ConfAttributes {
    JOB_TRACKER_HOST("mapred.job.tracker.host","192.168.1.130"),
    JOB_TRACKER_PORT("mapred.job.tracker.port","9000"),
    DFS_HOST("dfs.name.host","192.168.1.130"),
    DFS_PORT("dfs_name_port","9001"),
    WS_PUBLISH_HOST("ws_publish_host","localhost"),
    WS_PUBLISH_PORT("ws_publish_port","8081");


    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    private String name;
    private String value;

    ConfAttributes(String name, String value){
        this.name = name;
        this.value = value;
    }
}
