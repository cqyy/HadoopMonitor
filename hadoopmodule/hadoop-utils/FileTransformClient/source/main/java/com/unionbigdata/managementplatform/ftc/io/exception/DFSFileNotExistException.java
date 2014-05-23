package com.unionbigdata.managementplatform.ftc.io.exception;

/**
 * Created by Administrator on 14-3-17.
 */
public class DFSFileNotExistException extends Exception{
    public DFSFileNotExistException() {
    }

    public DFSFileNotExistException(String message) {
        super(message);
    }
}
