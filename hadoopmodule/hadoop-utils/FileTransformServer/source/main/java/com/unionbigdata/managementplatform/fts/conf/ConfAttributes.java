package com.unionbigdata.managementplatform.fts.conf;
/**
 * Created by Administrator on 14-3-12.
 * System configuration attributes
 */
public enum  ConfAttributes {

    DFS_HOST("dfs.name.host","localhost"),
    DFS_PORT("dfs_name_port","9001"),

    DFS_FILERECEIVER_HOST("dfs_file_receiver_host","localhost"),
    DFS_FILERECEIVER_PORT("dfs_file_receiver_port","4096");


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
