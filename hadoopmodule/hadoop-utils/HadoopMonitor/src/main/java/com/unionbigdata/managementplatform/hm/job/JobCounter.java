package com.unionbigdata.managementplatform.hm.job;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by kali on 14-2-28.
 * Record count information of Job.
 */
public class JobCounter implements Iterable<String>{

    private ConcurrentHashMap<String,Long> values = new ConcurrentHashMap<String,Long>();                 //store values of keys
    //keys of values
    public static final String TOTAL_LAUNCHED_REDUCES            = "TOTAL_LAUNCHED_REDUCES";
    public static final String SLOTS_MILLIS_MAPS                 = "SLOTS_MILLIS_MAPS";
    public static final String FALLOW_SLOTS_MILLIS_REDUCES       = "FALLOW_SLOTS_MILLIS_REDUCES";
    public static final String FALLOW_SLOTS_MILLIS_MAPS          = "FALLOW_SLOTS_MILLIS_MAPS";
    public static final String RACK_LOCAL_MAPS                   = "RACK_LOCAL_MAPS";
    public static final String TOTAL_LAUNCHED_MAPS               = "TOTAL_LAUNCHED_MAPS";
    public static final String DATA_LOCAL_MAPS                   = "DATA_LOCAL_MAPS";
    public static final String SLOTS_MILLIS_REDUCES              = "SLOTS_MILLIS_REDUCES";
    public static final String FILE_BYTES_READ                   = "FILE_BYTES_READ";
    public static final String HDFS_BYTES_READ                   = "HDFS_BYTES_READ";
    public static final String FILE_BYTES_WRITTEN                = "FILE_BYTES_WRITTEN";
    public static final String MAP_INPUT_RECORDS                 = "MAP_INPUT_RECORDS";
    public static final String REDUCE_SHUFFLE_BYTES              = "REDUCE_SHUFFLE_BYTES";
    public static final String SPILLED_RECORDS                   = "SPILLED_RECORDS";
    public static final String MAP_OUTPUT_BYTES                  = "MAP_OUTPUT_BYTES";
    public static final String CPU_MILLISECONDS                  = "CPU_MILLISECONDS";
    public static final String COMMITTED_HEAP_BYTES              = "COMMITTED_HEAP_BYTES";
    public static final String COMBINE_INPUT_RECORDS             = "COMBINE_INPUT_RECORDS";
    public static final String SPLIT_RAW_BYTES                   = "SPLIT_RAW_BYTES";
    public static final String REDUCE_INPUT_RECORDS              = "REDUCE_INPUT_RECORDS";
    public static final String REDUCE_INPUT_GROUPS               = "REDUCE_INPUT_GROUPS";
    public static final String COMBINE_OUTPUT_RECORDS            = "COMBINE_OUTPUT_RECORDS";
    public static final String PHYSICAL_MEMORY_BYTES             = "PHYSICAL_MEMORY_BYTES";
    public static final String REDUCE_OUTPUT_RECORDS             = "REDUCE_OUTPUT_RECORDS";
    public static final String VIRTUAL_MEMORY_BYTES              = "VIRTUAL_MEMORY_BYTES";
    public static final String MAP_OUTPUT_RECORDS                = "MAP_OUTPUT_RECORDS";

    public void setValues(String key , long   value){
        values.put(key,value);
    }

    public long getValue(String key){
        return values.get(key);
    }

    @Override
    public Iterator iterator() {
        return values.keySet().iterator();
    }
}
