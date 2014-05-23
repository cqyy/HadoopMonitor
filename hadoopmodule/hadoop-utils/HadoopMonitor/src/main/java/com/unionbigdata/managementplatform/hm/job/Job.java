package com.unionbigdata.managementplatform.hm.job;


import org.apache.hadoop.mapred.JobPriority;
import org.apache.hadoop.mapred.JobStatus;

/**
 * Created with IntelliJ IDEA.
 * User: yy
 * Date: 14-2-27
 * Time: 下午12:58
 * Job entity.Immutable
 */
public class Job {


    public static enum State {
        RUNNING,
        PREP,
        KILLED,
        FAILED,
        SUCCEEDED;
    }

    public static enum Priority {
        VERY_LOW,
        LOW,
        NORMAL,
        HIGH,
        VERY_HIGH;
    }

    //required,immutable
    private final String jobId;
    private final String user;
    private final String name;
    private final long startTime;

    //optional , immutable
    private final State state;
    private final float mapProcess;
    private final float reduceProcess;
    private final Priority priority;


    public static State JobStatusStateToState(int jobState) {
        switch (jobState) {
            case JobStatus.SUCCEEDED:
                return State.SUCCEEDED;
            case JobStatus.PREP:
                return State.PREP;
            case JobStatus.KILLED:
                return State.KILLED;
            case JobStatus.RUNNING:
                return State.RUNNING;
            case JobStatus.FAILED:
                return State.FAILED;
            default:
                throw new IllegalArgumentException("Jobstate : " + jobState);
        }
    }

    public static Priority JobStatusPriorityToPriority(JobPriority priority) {
        switch (priority) {
            case VERY_HIGH:
                return Priority.VERY_HIGH;
            case HIGH:
                return Priority.HIGH;
            case NORMAL:
                return Priority.NORMAL;
            case LOW:
                return Priority.LOW;
            case VERY_LOW:
                return Priority.VERY_LOW;
            default:
                throw new IllegalArgumentException("JobPriority: " + priority);
        }
    }

    public State getState() {
        return state;
    }

    public long getStartTime() {
        return startTime;
    }

    public float getMapProcess() {
        return mapProcess;
    }

    public float getReduceProcess() {
        return reduceProcess;
    }

    public String getJobId() {
        return jobId;
    }

    public Priority getPriority() {
        return priority;
    }

    public String getUser() {
        return user;
    }

    public String getName() {
        return name;
    }

    /*
    * Builder of Job .
    * */
    public static class Builder {
        //required,immutable
        private final String jobId;
        private final String user;
        private final String name;
        private final long startTime;

        //optional , mutable
        private State state = State.FAILED;
        private float mapProcess = 0f;
        private float reduceProcess = 0f;
        private Priority priority = Priority.NORMAL;

        public Builder(String jobId,
                       String jobName,
                       String user,
                       long startTime) {
            this.jobId = jobId;
            this.name = jobName;
            this.user = user;
            this.startTime = startTime;
        }

        public Builder state(State state) {
            this.state = state;
            return this;
        }

        public Builder mapProcess(float mapProcess) {
            if (mapProcess < 0 || mapProcess > 1) {
                throw new IllegalArgumentException(" mapProcess : " + mapProcess + " should be in [0,1]");
            }
            this.mapProcess = mapProcess;
            return this;
        }

        public Builder reduceProcess(float reduceProcess) {
            if (reduceProcess < 0 || reduceProcess > 1) {
                throw new IllegalArgumentException(" reduceProcess : " + reduceProcess + " should be in [0,1]");
            }
            this.reduceProcess = reduceProcess;
            return this;
        }

        public Builder jobPriority(Priority priority) {
            this.priority = priority;
            return this;
        }

        public Job build() {
            return new Job(this);
        }
    }

    private Job(Builder builder) {
        this.jobId = builder.jobId;
        this.name = builder.name;
        this.user = builder.user;
        this.startTime = builder.startTime;

        this.state = builder.state;
        this.mapProcess = builder.mapProcess;
        this.reduceProcess = builder.reduceProcess;
        this.priority = builder.priority;
    }
}
