package com.unionbigdata.managementplatform.ftc.io;

/**
 * Process reporter with synchronization.
 * <p>Example:</p>
 * <blockquote><pre>
 *     FileTransformClient client = FileTransformClient.getInstance();
 *     ProcessReporter reporter = client.uploadFile("", "", true);
 *     while (!reporter.isCompleted()){
 *           float process = reporter.process();
 *           //doSomething(process);
 *        }
 *     if(!reporter.isSucceed()){
 *           ErrorType error = reporter.getError();
 *           //doSomething(error)
 *       }
 * </pre></blockquote>
 */
class BlockingProcessReporter implements MyProcessReporter {

    private long totalBytes ;                        //total bytes needed to be transformed
    private long transformedBytes ;                        //bytes transformed
    private volatile boolean isCompleted = false;
    private volatile boolean isSucceed = false;
    private ErrorType error = ErrorType.NONE;

    //if the information is updated since the last time the client get the information
    private volatile boolean processUpdated = false;

    /**
     * Constructor.
     * @param totalBytes total bytes need to be transformed.
     * @throws IllegalArgumentException if {@code totalBytes <= 0}
     */
     BlockingProcessReporter(long totalBytes) {
        if (totalBytes <= 0){
            throw  new IllegalArgumentException("total bytes should be positive : " + totalBytes);
        }
        this.totalBytes = totalBytes;
    }

    @Override
    public void setTotalBytes(long bytes) {
        if(totalBytes <= 0){
            throw new IllegalArgumentException("totalBytes should be positive : " + totalBytes);
        }
        this.totalBytes = bytes;
    }

    @Override
    public synchronized void setTransformedBytes(long transformedBytes){
        if(transformedBytes < 0){
            throw new IllegalArgumentException("transformedBytes should not be negative : " + transformedBytes);
        }
        this.transformedBytes = transformedBytes;
        processUpdated = true;
        this.notifyAll();
    }

    @Override
    public synchronized void increaseTransformedBytes(long incre){
        if(incre <= 0){
            throw new IllegalArgumentException("increase should be positive : " + incre);
        }
        this.transformedBytes += incre;
        processUpdated = true;
        this.notifyAll();
    }


    @Override
    public synchronized void setCompleted(){
        if (transformedBytes == totalBytes){
            isSucceed = true;
        }
        this.isCompleted = true;
        this.notifyAll();
    }

    @Override
    public void setError(ErrorType errorInf){
        if(errorInf == null){
            throw  new NullPointerException("errorInf should not be null");
        }
        this.error = errorInf;
    }

    @Override
    public boolean isCompleted() {
        return isCompleted;
    }

    @Override
    public boolean isSucceed() {
        return isSucceed;
    }

    @Override
    public ErrorType getError() {
        return error;
    }

    /**
     * <p>Get the file transform process in percentage format.</p>
     * <p>This method will block until the process information updated or the process is competed.</p>
     * <p>When the process is not completed,it will block again next time until the process is updated
     * again or completed.</p>
     * <p>When the process is completed,it won't block any more.</p>
     * @return process value in [0,1]
     */
    @Override
    public synchronized float process() {
        while (!isCompleted() && !processUpdated){
            try {
                wait();
            } catch (InterruptedException e) {
                //do nothing .
            }
        }
        processUpdated = false;
        return (float)transformedBytes/totalBytes;
    }

    /**
     * <p>Get the file transformed bytes.</p>
     * <p>This method will block until the process information updated or the process is competed.</p>
     * <p>When the process is not completed,it will block again next time until the process is updated
     * again or completed.</p>
     * <p>When the process is completed,it won't block any more.</p>
     * @return bytes transformed.
     */
    @Override
    public  synchronized long transformedBytes() {
        while (!isCompleted() && !processUpdated){
            try {
                wait();
            } catch (InterruptedException e) {
                //do nothing.
            }
        }
        processUpdated = false;
        return transformedBytes;
    }
}
