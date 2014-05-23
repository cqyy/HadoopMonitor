package com.unionbigdata.managementplatform.hm.job;

import com.unionbigdata.managementplatform.hm.conf.ConfAttributes;
import com.unionbigdata.managementplatform.hm.conf.ConfManager;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: yy
 * Date: 14-2-26
 * Time: 下午6:26
 * Not concurrent safe
 */
public class SingletonJobClient {
    private static volatile JobClient client;
    private static final String propertyName = "mapred.job.tracker";

    private SingletonJobClient() {
        throw new RuntimeException("Mustn't make an instance of SingletonJobClient");
    }

    /**
     * Get an instance of JobClient.Not thread safe.
     *
     * @return
     * @throws java.io.IOException
     */
    public static JobClient getInstance() throws IOException {
        if (client == null) {
            synchronized (SingletonJobClient.class) {
                if (client == null) {
                    JobConf jobConf = new JobConf();
                    String host = ConfManager.getAttribute(ConfAttributes.JOB_TRACKER_HOST);
                    String port = ConfManager.getAttribute(ConfAttributes.JOB_TRACKER_PORT);
                    jobConf.set(propertyName, host + ":" + port);
                    client = new JobClient(jobConf);
                }
            }
        }
        return client;

    }
}
