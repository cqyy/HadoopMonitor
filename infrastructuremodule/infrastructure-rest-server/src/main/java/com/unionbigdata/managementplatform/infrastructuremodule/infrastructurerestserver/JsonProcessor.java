package com.unionbigdata.managementplatform.infrastructuremodule.infrastructurerestserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * A utility class used to get and trim monitoring info from Nagios-API.
 * Created by aichao on 14-3-17.
 */
public class JsonProcessor {

    private static Logger logger = LogManager.getLogger(JsonProcessor.class.getName());

    static String getUrlContent() {
        String result = "";
        try {
            InputStream is = new URL(getUrlProperty()).openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = br.read()) != -1) {
                sb.append((char) cp);
            }
            br.close();
            logger.info("获取 " + sb.length() + " 字节信息");
            result = sb.toString();
        } catch (IOException e) {
            logger.error("读取URL内容出错");
        }
        return result;
    }

    static JSONObject getTrimmedState() throws IOException {
        //API内容为空
        String urlContent = getUrlContent();
        //输出结果
        JSONObject trimmedState = new JSONObject();
        if (urlContent.trim().equals("")) {
            logger.error("监控服务器没有正常运行");
            trimmedState.put("success", false);
            trimmedState.put("error", "Infrastructure api server is down.");
        } else {
            //API内容不为空
            JSONObject allState = new JSONObject(urlContent);
            if (!allState.getBoolean("success")) {//API Usage -- RARE STATE
                logger.error("监控服务器内部错误");
                trimmedState.put("success", false);
                trimmedState.put("error", "Infrastructure api server internal error.");
                trimmedState.put("timestamp", System.currentTimeMillis() / 1000);
            } else {
                trimmedState.put("success", true);//API Usage -- NORMAL
                JSONObject contentJson = new JSONObject(allState.get("content").toString());
                for (Object hostnameObj : contentJson.keySet()) {
                    if (!hostnameObj.toString().equals("localhost")) {
                        //定义新的单个节点
                        JSONObject singleNode = new JSONObject();
                        //获取节点
                        JSONObject node = new JSONObject(contentJson.get(hostnameObj.toString()).toString());
                        //获取该节点的服务
                        JSONObject services = new JSONObject(node.get("services").toString());
                        //将所有服务的数据加入“服务数组”
                        for (Object service : services.keySet()) {
                            JSONObject oldComplexService = new JSONObject(services.get(service.toString()).toString());
                            JSONObject newSimpleService = new JSONObject();
                            newSimpleService.put("performance_data", oldComplexService.get("performance_data"));
                            newSimpleService.put("current_state", oldComplexService.get("current_state"));
                            newSimpleService.put("last_state_change", oldComplexService.get("last_state_change"));
                            newSimpleService.put("last_check", oldComplexService.get("last_check"));
                            singleNode.put(service.toString(), newSimpleService);
                        }
                        trimmedState.put(hostnameObj.toString(), singleNode);
                    }
                }
            }
        }
        logger.info("输出 " + trimmedState.toString().length() + " 字节信息");
        return trimmedState;
    }

    static String getUrlProperty() throws IOException {
        Properties prop = new Properties();
        prop.load(JsonProcessor.class.getClass().getResourceAsStream("/NagiosAPIServer.properties"));
        String url = prop.getProperty("nagios-api-url");
        logger.info("获取 URL= " + url);
        return url;
    }


}
