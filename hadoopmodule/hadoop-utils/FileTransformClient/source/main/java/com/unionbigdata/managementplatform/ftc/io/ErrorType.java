package com.unionbigdata.managementplatform.ftc.io;

/**
 * Errors may happens during file transform process.
 */
public enum ErrorType {

    /**
     * A placeholder,represent no error.
     * */
    NONE,

    /**
     * Can't create connection to file receiver,host or port configuration incorrectly or server work abnormally
     * */
    ServerUnreachable,

    /**
     * The file to be uploaded doesn't exist at local.
     * This happens when upload a file, and the input path doesn't exist.
     **/
    LocalFileNotFound,

    /**
     * The file exists at local.
     * This happens when download a file,and the path to store exits.
     */
    LocalFileExist,

    /**
     * The path of file to be uploaded is invalid, it may be a path of directory
     */
    InvalidLocalFileDirectory,

    /**
     * The path to store the file is invalid.it may be a directory.
     */
    InvalidHdfsFileDirectory,

    /**
     * The destination path exists in HDFS, can't overwrite it
     * This happens when upload a file ,and the path to store the file exists.
     */
    FileExistInHDFS,

    /**
     * Can't find the file to download.
     */
    FileNotExistInHDFS,

    /**
     * The server crashed for some reasons.
     * This this happen,the server may encountered some exception which can't recover itself,
     * please check the logs of server,and recover it.
     */
    ServerUnknownError;
}
