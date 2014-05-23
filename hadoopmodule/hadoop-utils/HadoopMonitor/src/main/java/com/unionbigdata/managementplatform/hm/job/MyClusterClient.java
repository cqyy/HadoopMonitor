package com.unionbigdata.managementplatform.hm.job;

import org.apache.hadoop.mapred.ClusterStatus;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobTracker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: yy
 * Date: 14-2-26
 * Time: 下午7:05
 * Monitor the hadoop cluster status.
 */
public class MyClusterClient {
    private JobClient client;

    private MyClusterClient() throws IOException {
        client = SingletonJobClient.getInstance();
    }

    public static MyClusterClient newInstance() throws IOException {
        return new MyClusterClient();
    }

    public Cluster getCluster() throws IOException {
        ClusterStatus clusterStatus = client.getClusterStatus(true);
        Cluster.JobTrackerState state =
                (clusterStatus.getJobTrackerState() == JobTracker.State.RUNNING)
                ? Cluster.JobTrackerState.RUNNING
                : Cluster.JobTrackerState.INITIALIZING ;

        List<String> activeTrackers = new ArrayList<String>(clusterStatus.getActiveTrackerNames());
        List<String> blacklistedTrackers = new ArrayList<String>(clusterStatus.getBlacklistedTrackerNames());

        Cluster cluster = new Cluster.Builder(state)
                .mapTasks(clusterStatus.getMapTasks())
                .reduceTasks(clusterStatus.getReduceTasks())
                .maxMapTasks(clusterStatus.getMaxMapTasks())
                .maxReduceTasks(clusterStatus.getMaxReduceTasks())
                .trackers(clusterStatus.getTaskTrackers())
                .maxMemory(clusterStatus.getMaxMemory())
                .usedMemory(clusterStatus.getUsedMemory())
                .addActiveTrackers(activeTrackers)
                .addBlackListedTrackers(blacklistedTrackers)
                .newInstance();
        return cluster;
    }
}
