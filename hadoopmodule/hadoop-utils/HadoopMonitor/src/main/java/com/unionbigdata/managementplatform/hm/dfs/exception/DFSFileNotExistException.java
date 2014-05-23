package com.unionbigdata.managementplatform.hm.dfs.exception;

/**
 * Created by Administrator on 14-3-5.
 */
public class DFSFileNotExistException extends Exception {
    public DFSFileNotExistException(String info){
        super(info);
    }
    public DFSFileNotExistException(){
    }
}
