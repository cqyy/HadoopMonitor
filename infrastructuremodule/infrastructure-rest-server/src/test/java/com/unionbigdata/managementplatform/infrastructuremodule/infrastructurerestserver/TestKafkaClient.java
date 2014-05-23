package com.unionbigdata.managementplatform.infrastructuremodule.infrastructurerestserver;
import org.junit.Test;

/**
 * Test Kafka Client.
 */
public class TestKafkaClient {
    @Test
    public void testKafkaClient() {
        KafkaClient client = KafkaClient.getInstance();
    }
}
