package com.unionbigdata.managementplatform.ws;


import com.unionbigdata.managementplatform.hm.job.Job;
import com.unionbigdata.managementplatform.hm.job.JobCounter;
import com.unionbigdata.managementplatform.hm.job.MyClusterClient;
import com.unionbigdata.managementplatform.hm.job.MyJobClient;
import com.google.gson.Gson;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 14-3-12.
 * Proxy for job managing
 */
@Path("/job")
public class JobProxy {
    MyJobClient client = null;
    MyClusterClient clusterClient = null;


    public JobProxy() {
        try {
            this.client = MyJobClient.getInstance();
            this.clusterClient = MyClusterClient.newInstance();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GET
    @Path("getClusterInfo")
    @Produces(MediaType.TEXT_PLAIN)
    public String getClusterInfo() {
        Gson gson = new Gson();
        try {
            return gson.toJson(clusterClient.getCluster());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @GET
    @Path("allJobs")
    @Produces(MediaType.TEXT_PLAIN)
    public String allJobs() {
        List<Job> jobs = null;
        Gson gson = new Gson();
        try {
            jobs = client.allJobs();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gson.toJson(jobs);
    }

    @GET
    @Path("runningJobs")
    @Produces(MediaType.TEXT_PLAIN)
    public String runningJobs(){
        List<Job> jobs = null;
        Gson gson = new Gson();
        try {
            jobs = client.runningJobs();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gson.toJson(jobs);
    }

    @GET
    @Path("prepJobs")
    @Produces(MediaType.TEXT_PLAIN)
    public String prepJobs(){
        List<Job> jobs = null;
        Gson gson = new Gson();
        try {
            jobs = client.prepJobs();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gson.toJson(jobs);
    }

    @GET
    @Path("succeededJobs")
    @Produces(MediaType.TEXT_PLAIN)
    public String succeededJobs(){
        List<Job> jobs = null;
        Gson gson = new Gson();
        try {
            jobs = client.succeededJobs();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gson.toJson(jobs);
    }

    @GET
    @Path("failedJobs")
    @Produces(MediaType.TEXT_PLAIN)
    public String failedJobs(){
        List<Job> jobs = null;
        Gson gson = new Gson();
        try {
            jobs = client.failedJobs();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gson.toJson(jobs);
    }


    @GET
    @Path("killedJobs")
    @Produces(MediaType.TEXT_PLAIN)
    public String killedJobs(){
        List<Job> jobs = null;
        Gson gson = new Gson();
        try {
            jobs = client.killedJobs();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gson.toJson(jobs);
    }

    @GET
    @Path("getJobCounters")
    @Produces(MediaType.TEXT_PLAIN)
    public String getJobCounters(@QueryParam("jobId") String jobId){
        if (jobId== null){
            return "null";
        }
        JobCounter counters = client.getJobCounter(jobId);
        Gson gson = new Gson();
        return gson.toJson(counters);
    }


}
