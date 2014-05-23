package com.unionbigdata.managementplatform.ftc.conf;

/**
 * Configuration attributes of the system.
 * These configurations are stored in an configuration file in format of xml.
 * If you want to change these configurations,edit the configuration file.
 * <p>More information,see {@link ConfManager}</p>
 * @author yy
 * @version 1.0
 */
public enum  ConfAttributes {
    /**
     * Host of file receiving server.
     * <p>Key in configuration file : dfs_server_host</p>
     * <p>Default value: localhost</p>
     */
    DFS_SERVER_HOST("dfs_server_host","localhost"),

    /**
     * Port using for file transforming of the file receiving server.
     * If you want to change this value,make sure it stays the same with the server's.
     * <p>Key in configuration file : dfs_server_port </p>
     * <p>Default value: 4096</p>
     */
    DFS_SERVER_PORT("dfs_server_port","4096"),

    /**
     * The max value of the amount of file transform process at the same time.
     * When {@code value <= 0},the amount is unlimited.
     * <p>Key in configuration file : max_process_num</p>
     * <p><Default value: 5</p>
     */
    MAX_PROCESS_NUM("max_process_num","5"),

    /**
     * When get connection to server,if failed ,how much time it will retry.
     * <p>Key in configuration file : max_connect_retry_time </p>
     * <p>Default value : 3</p>
     */
    MAX_CONNECT_RETRY_TIME("max_connect_retry_time","3");

    /**
     * Get the key name of this configuration attribute in the configuration file.
     * @return key name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the value of this configuration attribute.
     * @return value
     */
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
