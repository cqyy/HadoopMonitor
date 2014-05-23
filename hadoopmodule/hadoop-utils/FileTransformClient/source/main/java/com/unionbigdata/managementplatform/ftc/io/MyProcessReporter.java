package com.unionbigdata.managementplatform.ftc.io;

/**
 * Created by Administrator on 14-3-13.
 */
interface MyProcessReporter extends ProcessReporter {

    /**
     * Set total bytes to be transformed.It is usually the length of the file
     * <p>This method would not block</p>
     * @param bytes
     */
    void setTotalBytes(long bytes);

    /**
     * Set the bytes transformed.
     * <p>This method would not block</p>
     * @param transformedBytes bytes transformed
     * @throws IllegalArgumentException if the {@code transformedBytes} {@literal < 0}
     */
     void setTransformedBytes(long transformedBytes);

    /**
     * Increase transformed bytes by {@code incre}
     * <p>This method would not block</p>
     * @param incre increased bytes
     * @throws IllegalArgumentException if {@code incre } {@literal <= 0}
     */
     void increaseTransformedBytes(long incre);

    /**
     * Set this process to be completed.
     * This will decide if this process is succeeded.If transformed bytes equals to total
     * bytes need to be transformed,this process is regarded succeeded.
     * <p>This method would not block</p>
     */
     void setCompleted();

    /**
     * Set the error information.
     * <p>This method would not block</p>
     * @param errorInf error information
     * @throws NullPointerException if {@code errorInf == null }
     */
    void setError(ErrorType errorInf);

}
