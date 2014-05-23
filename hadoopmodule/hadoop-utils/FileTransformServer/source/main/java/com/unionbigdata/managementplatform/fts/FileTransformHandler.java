package com.unionbigdata.managementplatform.fts;

import java.io.*;

/**
 * Created by Administrator on 14-3-6.
 * File transform handler.
 */
public interface FileTransformHandler {

    /**
     * Upload file to destination.
     * @param inputStream file stream to be uploaded
     * @param destination absolute destination path to store the file data
     * @throws java.io.IOException if the destination path exits or is invalidate,or if {@code inputStream } is closed.
     * @throws NullPointerException if one or more argument is null
     */
    void upload(DataInputStream inputStream, String destination) throws IOException;


    /**
     * Download the {@code source} file.
     * @param source the absolute file path to be download
     * @param outputStream the output stream.
     * @throws java.io.IOException if the source path doesn't exists or the {@code outputStream}is closed
     * @throws NullPointerException if one or more argument is null
     */
    void download(String source, DataOutputStream outputStream) throws IOException;
}
