package com.unionbigdata.managementplatform.ftc.io;

/**
 * Created by Administrator on 14-3-13.
 * The reporter notifying the process of files transform.
 */
public interface ProcessReporter {
    /**
     * Test if the file transform process is completed.
     * Completed include situation of succeeded and failed.
     * <p>This method would not block</p>
     * @return true if completed or false
     */
    boolean isCompleted();

    /**
     * Test if the file transform process is succeeded
     * <p>This method would not block</p>
     * @return true if succeeded
     */
    boolean isSucceed();

    /**
     * Get the information of error.
     * <p>This method would not block</p>
     * @return errorInf if the process failed or empty string
     */
    ErrorType getError();

    /**
     * Get the process of file transform.
     * @return process value in [0,1]
     */
    float process();

    /**
     * Get the bytes transformed.
     * @return bytes of transformed,the return value will not be negative
     */
    long transformedBytes();
}
