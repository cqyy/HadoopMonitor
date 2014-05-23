package com.unionbigdata.managementplatform.infrastructuremodule.infrastructurerestserver;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Kafka Client sending monitoring message to front-end.
 * Created by aichao on 14-3-17.
 */
public class KafkaClient {
    Timer timer;
    Properties props;
    ProducerConfig config;
    Producer producer;
    Logger logger = LogManager.getLogger(KafkaClient.class.getName());
    private KafkaClient() {

        props = new Properties();

        try {
            props.load(this.getClass().getResourceAsStream("/KafkaClient.properties"));

        } catch (IOException e) {
            logger.error(e.getCause());
        }

        config = new ProducerConfig(props);

        producer = new Producer<String, String>(config);

        timer = new Timer();
        //schedule(Task, delay, interval)
        timer.schedule(new PushMessageTask(), 0L, Long.parseLong(props.getProperty("monitor.transmit.interval")));
    }

    public static final KafkaClient getInstance() {
        return KafkaClientHolder.INSTANCE;
    }

    //使用内部类实现单例lazy-load
    public static class KafkaClientHolder {
        private static final KafkaClient INSTANCE = new KafkaClient();
    }

    private class PushMessageTask extends TimerTask {
        @Override
        public void run() {
            try {
                KeyedMessage<String, String> data = new KeyedMessage<String, String>
                        (props.getProperty("monitor.topic.name"), JsonProcessor.getTrimmedState().toString());
                producer.send(data);
            } catch (IOException e) {
                logger.error(e.getCause());
            } finally {
                producer.close();
            }
        }
    }
}
