package com.unionbigdata.managementplatform.hm.dfs;

import com.unionbigdata.managementplatform.hm.conf.ConfAttributes;
import com.unionbigdata.managementplatform.hm.conf.ConfManager;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.DFSClient;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by kali on 14-3-3.
 * Factory for DFSClient , ThreadSafe.
 */
public class DFSClientFactory {
    private static final Configuration conf = new Configuration();
    private static final InetSocketAddress inetSocketAddress = new InetSocketAddress(
            ConfManager.getAttribute(
                    ConfAttributes.DFS_HOST),
            Integer.parseInt(
                    ConfManager.getAttribute(
                            ConfAttributes.DFS_PORT)));

    //Thread safe
    public static synchronized DFSClient newInstance() throws IOException {
        DFSClient client = new DFSClient(inetSocketAddress,conf);
        return client;
    }
}