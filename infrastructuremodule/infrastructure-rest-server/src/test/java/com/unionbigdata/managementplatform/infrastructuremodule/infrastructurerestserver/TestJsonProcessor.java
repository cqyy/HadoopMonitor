package com.unionbigdata.managementplatform.infrastructuremodule.infrastructurerestserver;

import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by aichao on 14-3-17.
 */
public class TestJsonProcessor {

    //@Test
    public void testGetState()  {
        /*
            JSONObject allState = new JSONObject(JsonProcessor.getUrlContent());
            JSONObject contentJson = new JSONObject(allState.get("content").toString());
            System.out.println(contentJson);
            System.out.println(contentJson.keySet());
            */
    }

    @Test
    public void testGetTrimmedState() throws IOException {
        /*
        System.out.println(JsonProcessor.getTrimmedState());
        */
    }

    //@Test
    public void testGetUrlProperty() throws IOException {
        /*
        Properties prop = new Properties();
        prop.load(JsonProcessor.class.getClass().getResourceAsStream("/NagiosAPIServer.properties"));
        System.out.println(prop.getProperty("url"));
        */
    }
}
