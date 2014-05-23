package com.unionbigdata.managementplatform.hm.job;

import org.apache.hadoop.mapred.*;

import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: yy
 * Date: 14-2-26
 * Time: 下午5:5 1
 */
public class JobMonitor {

    interface JobFilter {
        boolean accept(JobStatus jobStatus);
    }

    private JobClient client;

    //forbid instance
    private JobMonitor() throws IOException {
        client = SingletonJobClient.getInstance();
    }

    public static JobMonitor getInstance() throws IOException {
        return new JobMonitor();
    }

    /**
     * Fill jobs filted by filter into list of Job
     * @param jobStatuses
     * @param filter
     * @return
     * @throws java.io.IOException
     */
    private List<Job> fillInList(JobStatus[] jobStatuses, JobFilter filter) throws IOException {
        List<Job> jobs = new ArrayList<Job>();

        for (JobStatus jobStatus : jobStatuses) {

            if (!filter.accept(jobStatus)) {
                continue;
            }

            RunningJob runningJob = client.getJob(jobStatus.getJobID());

            //build a job instance
            Job job = new Job.Builder(
                    jobStatus.getJobID().toString(),
                    runningJob.getJobName(),
                    jobStatus.getUsername(),
                    jobStatus.getStartTime())
                    .state(
                            Job.JobStatusStateToState(
                                    jobStatus.getRunState()))
                    .mapProcess(jobStatus.mapProgress())
                    .reduceProcess(jobStatus.reduceProgress())
                    .jobPriority(
                            Job.JobStatusPriorityToPriority(
                            jobStatus.getJobPriority()))
                    .build();

            jobs.add(job);
        }
        return jobs;
    }

    /**
     * Get all jobs
     *
     * @return
     * @throws java.io.IOException
     */
    public List<Job> allJobs() throws IOException {
        JobStatus[] jobStatuses = client.getAllJobs();
        return fillInList(jobStatuses, new JobFilter() {
            @Override
            public boolean accept(JobStatus jobStatus) {
                //accept all jobs
                return true;
            }
        });
    }
    /**
     * Get running jobs
     *
     * @return
     */
    public List<Job> runningJobs() throws IOException {
        JobStatus[] jobStatuses = client.getAllJobs();
        return fillInList(jobStatuses, new JobFilter() {
            @Override
            public boolean accept(JobStatus jobStatus) {
                //accept jobs which state is RUNNING
                if (jobStatus.getRunState() == JobStatus.RUNNING) {
                    return true;
                }
                return false;
            }
        });
    }

    public List<Job> prepJobs() throws IOException {
        JobStatus[] jobStatuses = client.getAllJobs();
        return fillInList(jobStatuses,new JobFilter() {
            @Override
            public boolean accept(JobStatus jobStatus) {
                if(jobStatus.getRunState() == JobStatus.PREP){
                    return true;
                }
                return false;
            }
        });
    }

    public List<Job> succeededJobs() throws IOException {
        JobStatus[] jobStatuses = client.getAllJobs();
        return fillInList(jobStatuses,new JobFilter() {
            @Override
            public boolean accept(JobStatus jobStatus) {
                if(jobStatus.getRunState()  == JobStatus.SUCCEEDED){
                    return true;
                }
                return false;
            }
        });
    }

    public List<Job> failedJobs() throws IOException {
        JobStatus[] jobStatuses = client.getAllJobs();
        return fillInList(jobStatuses,new JobFilter() {
            @Override
            public boolean accept(JobStatus jobStatus) {
                if(jobStatus.getRunState() == JobStatus.FAILED){
                    return true;
                }
                return false;
            }
        });
    }

    public List<Job> killedJobs() throws IOException {
        JobStatus[] jobStatuses = client.getAllJobs();
        return fillInList(jobStatuses,new JobFilter() {
            @Override
            public boolean accept(JobStatus jobStatus) {
                if(jobStatus.getRunState() == JobStatus.KILLED){
                    return true;
                }
                return false;
            }
        });
    }

}
