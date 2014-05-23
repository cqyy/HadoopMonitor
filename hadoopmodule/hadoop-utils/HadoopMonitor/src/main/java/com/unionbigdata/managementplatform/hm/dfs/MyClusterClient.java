package com.unionbigdata.managementplatform.hm.dfs;

import org.apache.hadoop.fs.FsStatus;
import org.apache.hadoop.hdfs.DFSClient;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
import org.apache.hadoop.hdfs.protocol.FSConstants;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by kali on 14-3-3.
 */
public class MyClusterClient {

    private DFSClient client;

    private MyClusterClient() throws IOException {
        client = DFSClientFactory.newInstance();
    }

    public static MyClusterClient newInstance() throws IOException {
        return new MyClusterClient();
    }

    public Cluster getClusterInfo() throws IOException {
        FsStatus fsStatus = client.getDiskStatus();
        List<DataNode> liveNodes = asDataNodeList(
                client.datanodeReport(
                        FSConstants.DatanodeReportType.LIVE));
        List<DataNode> deadNodes = asDataNodeList(
                client.datanodeReport(
                        FSConstants.DatanodeReportType.DEAD));
        Cluster cluster = new Cluster.Builder(
                    fsStatus.getCapacity(),
                    fsStatus.getUsed(),
                    fsStatus.getRemaining())
                .addLiveDataNodes(liveNodes)
                .addDeadDataNodes(deadNodes)
                .newInstance();

        return cluster;
    }

    private List<DataNode> asDataNodeList(DatanodeInfo[] infos) {
        List<DataNode> nodes = new LinkedList<DataNode>();

        if (infos != null) {
            for (DatanodeInfo info : infos) {
                DataNode node = new DataNode
                        .Builder(
                        info.getHostName(),
                        stateTransform(info.getAdminState()))
                        .setDfdRemaining(info.getRemaining())
                        .setDfsCapacity(info.getCapacity())
                        .setDfsUsed(info.getDfsUsed())
                        .setLastUpdated(info.getLastUpdate())
                        .newInstance();
                nodes.add(node);
            }
        }
        return nodes;
    }

    private DataNode.State stateTransform(DatanodeInfo.AdminStates state) {
        switch (state) {
            case NORMAL:
                return DataNode.State.NORMAL;
            case DECOMMISSION_INPROGRESS:
                return DataNode.State.DECOMMISSION_INPROGRESS;
            case DECOMMISSIONED:
                return DataNode.State.DECOMMISSIONED;
            default:
                throw new IllegalArgumentException("state must be one of DatanodeInfo.AdminStates");
        }
    }

}
