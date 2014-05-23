package com.unionbigdata.managementplatform.hm.dfs;

/**
 * Created by kali on 14-3-3.
 * Immutable
 */
public class DataNode {
    public static enum State{
        NORMAL,
        DECOMMISSION_INPROGRESS,
        DECOMMISSIONED;
    }

    //required
    private final String hostname;
    private final State state;

    //optional
    private final long dfs_capacity_bytes;
    private final long dfs_used_bytes;
    private final long dfs_remaining_bytes;

    private final long last_updated_milliseconds;


    public static class Builder{
        //required
        public  String hostname;
        public  State state;

        //optional
        public  long dfs_capacity_bytes = 0;
        public  long dfs_used_bytes = 0;
        public  long dfs_remaining_bytes = 0;

        public  long last_updated_milliseconds = 0;

        public Builder(String hostname,State state){
            this.hostname = hostname;
            this.state = state;
        }


        public DataNode newInstance(){
            return new DataNode(this);
        }

        public Builder setDfsCapacity(long dfs_capacity_bytes){
            this.dfs_capacity_bytes = dfs_capacity_bytes;
            return this;
        }

        public Builder setDfsUsed(long dfs_used_bytes){
            this.dfs_used_bytes = dfs_used_bytes;
            return this;
        }

        public Builder setDfdRemaining(long dfs_remaining_bytes){
            this.dfs_remaining_bytes = dfs_remaining_bytes;
            return this;
        }

        public Builder setLastUpdated(long last_updated_milliseconds){
            this.last_updated_milliseconds = last_updated_milliseconds;
            return this;
        }
    }

    private DataNode(Builder builder){
        this.hostname = builder.hostname;
        this.state = builder.state;
        this.dfs_capacity_bytes = builder.dfs_capacity_bytes;
        this.dfs_remaining_bytes = builder.dfs_remaining_bytes;
        this.dfs_used_bytes = builder.dfs_used_bytes;
        this.last_updated_milliseconds = builder.last_updated_milliseconds;
    }

    public String getHostname() {
        return hostname;
    }

    public State getState() {
        return state;
    }

    public long getDfs_capacity_bytes() {
        return dfs_capacity_bytes;
    }

    public long getDfs_used_bytes() {
        return dfs_used_bytes;
    }

    public long getDfs_remaining_bytes() {
        return dfs_remaining_bytes;
    }

    public long getLast_updated_milliseconds() {
        return last_updated_milliseconds;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Hostname: ").append(hostname)
                .append("   State: ").append(state)
                .append("   Capacity:  ").append(dfs_capacity_bytes)
                .append("   Used:   ").append(dfs_used_bytes)
                .append("   Remaining:   ").append(dfs_remaining_bytes)
                .append("   Last Update:   ").append(last_updated_milliseconds);
        return builder.toString();
    }
}
