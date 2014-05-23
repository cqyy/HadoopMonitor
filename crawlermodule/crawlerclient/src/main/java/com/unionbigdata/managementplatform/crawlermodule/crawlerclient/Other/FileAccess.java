package com.unionbigdata.managementplatform.crawlermodule.crawlerclient.Other;

import java.io.*;

/**
 * Created by lwj on 14-4-2.
 */
public class FileAccess {

    public static boolean Move(String srcFile, String destPath) {
        // File (or directory) to be moved
        File file = new File(srcFile);

        // Destination directory
        File dir = new File(destPath);

        // Move file to new directory
        boolean success = file.renameTo(new File(dir, file.getName()));

        return success;
    }

    public static void Copy(String oldPath, String newPath) throws IOException {
        int bytesum = 0;
        int byteread = 0;
        File oldfile = new File(oldPath);
        if (oldfile.exists()) {
            InputStream inStream = new FileInputStream(oldPath);
            FileOutputStream fs = new FileOutputStream(newPath);
            byte[] buffer = new byte[1024];
            int length;
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                fs.write(buffer, 0, byteread);
            }
            inStream.close();
        }
    }

    public static boolean delFile(String filePath){
        File file = new File(filePath);
        return file.delete();
    }
}
