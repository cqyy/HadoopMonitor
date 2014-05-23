package com.unionbigdata.managementplatform.ws;

import org.glassfish.jersey.server.ResourceConfig;

/**
 * Created by Administrator on 14-3-22.
 */
public class MyApplication extends ResourceConfig {
    public MyApplication(){
        register(DFSProxy.class);
        register(JobProxy.class);
        register(Echo.class);
    }

}
