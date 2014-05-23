package com.unionbigdata.managementplatform.hm.dfs;

/**
 * Created by Administrator on 14-3-5.
 * File item interface,represent items in file system
 */
public interface DFSFileItem {
    /*Check if the file item is a directory.
    @return true if it's directory,or false if not.
    * */
    boolean isDir();

    /*
    check if this file item is file.
    @return true if it's file.
     */
    boolean isFile();

    //get local name of this file.return name of the file item which doesn't contain directory
    String getLocalName();

    //get last modification time.
    //return milliseconds of modification time
    long getModificationTime();

    //get the owner name of this file item
    String owner();

    //get the group of the file item
    String group();

    //get permission of the file.It consist in the format like "rwxr-xr-x"
    String permission();

}
