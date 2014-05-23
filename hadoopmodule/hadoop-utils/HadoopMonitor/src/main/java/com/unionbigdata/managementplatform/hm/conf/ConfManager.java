package com.unionbigdata.managementplatform.hm.conf;


import java.io.*;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: yy
 * Date: 14-2-26
 * Time: 下午5:52
 * Manage configuration
 */
public class ConfManager {
    private static String conf_dir = "./hadoopmodule/hadoop-utils/HadoopMonitor/conf/";             //properties file directory
    private static String conf_name = "properties.xml";     //properties file name

    private static Properties conf = new Properties();


    static {
        try{
//            File confFile = new File(conf_dir + conf_name);
//            if(!confFile.exists()){
//                createTemplate();
//            }

            conf.loadFromXML(Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream(conf_name));
        }catch (FileNotFoundException e){
            System.err.println("Properties file not found !");
        } catch (InvalidPropertiesFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getAttribute(ConfAttributes attribute){
        return conf.getProperty(attribute.getName());
    }

    /**
     * Create an template file of properties
     */
    private static void createTemplate() throws IOException {

        File file = new File(conf_dir);
        if(!file.exists()){
            file.mkdirs();
        }
        file = new File(conf_dir + conf_name);
        file.createNewFile();
        System.err.println("Configuration file not found,a template configuration file created at : " +  file.getAbsolutePath());
        Properties pro = new Properties();

        for(ConfAttributes attributes : ConfAttributes.values()){
            pro.setProperty(attributes.getName(),attributes.getValue());
        }

        FileOutputStream outputStream = new FileOutputStream(file);
        pro.storeToXML(outputStream,"default properties");
        outputStream.close();
        System.exit(1);
    }
}
