package com.unionbigdata.managementplatform.hm.job;

import java.util.*;

/**
 * Created by kali on 14-3-1.
 * Store information of cluster.
 * Immutable.
 */
public class Cluster {

    public static enum JobTrackerState {                               //states of job tracker
        INITIALIZING,
        RUNNING;
    };


    //required
    private final JobTrackerState jobtrackerState;                     //current state of job tracker

    //optional
    private final int reduceTasks ;                                    //running reduce tasks number
    private final int maxReduceTasks ;                                 //maximum of reduce tasks number
    private final int mapTasks ;                                       //running map tasks number
    private final int maxMapTasks;                                     //maximum of map tasks number
    private final List<String> activeTrackers;                         //active task trackers' names ,immutable.
    private final List<String> blackListedTrackers;                    //blacklisted task trackers' names,immutable
    private final int trackers;                                        //task trackers number

    private final long maxMemory;                                      /* maximum configured heap memory
                                                                         that can be used by the JobTracker */
    private final long usedMemory;                                     //total heap memory used by the JobTracker


    //the builder of CLuster
    public static class Builder{
        private  int reduceTasks = -1;
        private  int maxReduceTasks = -1;
        private  int mapTasks = -1;
        private  int maxMapTasks = -1;
        private  List<String> activeTrackers = new LinkedList<String>();
        private  List<String> blackListedTrackers = new LinkedList<String>();
        private  int trackers = -1;
        private  long maxMemory = -1;
        private  long usedMemory = -1;
        private  JobTrackerState jobtrackerState;

        public Builder(JobTrackerState state){
            this.jobtrackerState = state;
        }

        public Cluster newInstance(){
            return new Cluster(this);
        }

        public Builder reduceTasks(int tasks){
            this.reduceTasks = tasks;
            return this;
        }

        public Builder maxReduceTasks(int maxReduceTasks){
            this.maxReduceTasks = maxReduceTasks;
            return this;
        }

        public Builder mapTasks(int tasks){
            this.mapTasks = tasks;
            return this;
        }

        public Builder maxMapTasks(int maxTasks){
            this.maxMapTasks = maxTasks;
            return this;
        }

        public Builder addActiveTrackers(Collection<String> trackers){
            if(trackers == null){
                throw new NullPointerException("ActiveTrackers should not be null");
            }
            this.activeTrackers.addAll(trackers);
            return this;
        }

        public Builder addBlackListedTrackers(Collection<String> trackers){
            if(trackers == null){
                throw new NullPointerException("BlacklistedList should not be null");
            }
            this.blackListedTrackers.addAll(trackers);
            return this;
        }

        public Builder trackers(int trackers){
            this.trackers = trackers;
            return this;
        }

        public Builder maxMemory(long maxMemory){
            this.maxMemory = maxMemory;
            return this;
        }

        public Builder usedMemory(long usedMemory){
            this.usedMemory = usedMemory;
            return this;
        }

        }

    private Cluster(Builder builder){
        this.mapTasks = builder.mapTasks;
        this.reduceTasks = builder.reduceTasks;
        this.maxMapTasks = builder.maxMapTasks;
        this.maxReduceTasks = builder.maxReduceTasks;
        this.trackers = builder.trackers;
        this.jobtrackerState = builder.jobtrackerState;
        this.maxMemory = builder.maxMemory;
        this.usedMemory = builder.usedMemory;

        activeTrackers = Collections.unmodifiableList(builder.activeTrackers);
        blackListedTrackers = Collections.unmodifiableList(builder.blackListedTrackers);
    }

    public JobTrackerState getJobtrackerState() {
        return jobtrackerState;
    }

    public int getReduceTasks() {
        return reduceTasks;
    }

    public int getMaxReduceTasks() {
        return maxReduceTasks;
    }

    public int getMapTasks() {
        return mapTasks;
    }

    public int getMaxMapTasks() {
        return maxMapTasks;
    }

    public List<String> getActiveTrackers() {
        return activeTrackers;
    }

    public List<String> getBlackListedTrackers() {
        return blackListedTrackers;
    }

    public int getTrackers() {
        return trackers;
    }

    public long getMaxMemory() {
        return maxMemory;
    }

    public long getUsedMemory() {
        return usedMemory;
    }

}
