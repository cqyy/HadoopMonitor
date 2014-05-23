package com.unionbigdata.managementplatform.ftc.io;

/**
 * Process reporter which would not be blocked.
 */
class UnblockingProcessReporter implements MyProcessReporter {

    private long totalBytes ;                        //total bytes needed to be transformed
    private long transformedBytes ;                  //bytes transformed
    private volatile boolean  isCompleted = false;
    private volatile boolean isSucceed = false;
    private ErrorType error = ErrorType.NONE;

    /**
     * Constructor.
     * @param totalBytes total bytes need to be transformed.It usual be size of the file.
     * @throws IllegalArgumentException if {@code totalBytes} {@literal <= 0}
     */
    public UnblockingProcessReporter(long totalBytes){
        if(totalBytes <= 0){
            throw new IllegalArgumentException("totalBytes should be positive : " + totalBytes);
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
    public void setTransformedBytes(long transformedBytes){
        if(transformedBytes < 0){
            throw new IllegalArgumentException("transformedBytes should not be negative : " + transformedBytes);
        }
        this.transformedBytes = transformedBytes;
    }

    @Override
    public void increaseTransformedBytes(long incre){
        if(incre <= 0){
            throw new IllegalArgumentException("increase should be positive : " + incre);
        }
        this.transformedBytes += incre;
    }

    @Override
    public void setCompleted(){
        if (transformedBytes == totalBytes){
            isSucceed = true;
        }
        this.isCompleted = true;
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

    @Override
    public float process() {
        return (float)transformedBytes/totalBytes;
    }

    @Override
    public long transformedBytes() {
        return transformedBytes;
    }
}
