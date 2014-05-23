package com.unionbigdata.managementplatform.hm.dfs;

import java.util.*;

/**
 * Created by kali on 14-3-3.
 * Store HDFS Information Of Cluster.
 * Immutable.
 */
public class Cluster {

    //required
    private final long fs_capacity ;
    private final long fs_used;
    private final long fs_remaining;

    //optional
    private final List<DataNode> liveDataNodes;
    private final List<DataNode> deadDataNodes;



    public static class  Builder{
        private long fs_capacity ;
        private long fs_used;
        private long fs_remaining;

        private List<DataNode> liveDataNodes = new LinkedList<DataNode>();
        private List<DataNode> deadDataNodes = new LinkedList<DataNode>();

        public Builder(long fs_capacity,
                       long fs_used,
                       long fs_remaining){
            this.fs_capacity = fs_capacity;
            this.fs_remaining = fs_remaining;
            this.fs_used = fs_used;
        }

        public Cluster newInstance(){
            return new Cluster(this);
        }

        public Builder addLiveDataNodes(Collection<DataNode> nodes){
            if(nodes == null){
                throw new NullPointerException("nodes should not be null");
            }
            liveDataNodes.addAll(nodes);
            return this;
        }

        public Builder addLiveDataNodes(DataNode[] nodes){
            if(nodes == null){
                throw new NullPointerException("nodes should not be null");
            }
            liveDataNodes.addAll(Arrays.asList(nodes));
            return this;
        }

        public Builder addDeadDataNodes(Collection<DataNode> nodes){
              if(nodes == null){
                  throw new NullPointerException("nodes should not be null");
              }
            this.deadDataNodes.addAll(nodes);
            return this;
        }

        public Builder addDeadDataNodes(DataNode[] nodes){
            if(nodes == null){
                throw new NullPointerException("nodes should not be null");
            }
            this.deadDataNodes.addAll(Arrays.asList(nodes));
            return this;
        }

    }

    private Cluster(Builder builder){
        this.deadDataNodes = Collections.unmodifiableList(builder.deadDataNodes);
        this.liveDataNodes = Collections.unmodifiableList(builder.liveDataNodes);
        this.fs_capacity = builder.fs_capacity;
        this.fs_remaining = builder.fs_remaining;
        this.fs_used = builder.fs_used;
    }

    public long getFs_capacity() {
        return fs_capacity;
    }

    public long getFs_used() {
        return fs_used;
    }

    public long getFs_remaining() {
        return fs_remaining;
    }

    public List<DataNode> getLiveDataNodes() {
        return liveDataNodes;
    }

    public List<DataNode> getDeadDataNodes() {
        return deadDataNodes;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Capacity: ").append(fs_capacity)
                .append("  Used: ").append(fs_used)
                .append("  Remaining: ").append(fs_remaining)
                .append("  Live Nodes: ").append(liveDataNodes.size())
                .append("  Dead Nodes: ").append(deadDataNodes.size());
        return builder.toString();
    }
}
