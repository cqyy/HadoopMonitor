package com.unionbigdata.managementplatform.ftc.io.exception;

/**
 * Created by Administrator on 14-3-17.
 */
public class DFSFileExistException extends Exception {
    public DFSFileExistException(){};

    public DFSFileExistException(String msg){
        super(msg);
    }

}
