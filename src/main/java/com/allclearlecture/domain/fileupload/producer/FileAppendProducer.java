package com.allclearlecture.domain.fileupload.producer;

import com.allclearlecture.domain.fileupload.event.EventHandler;
import com.allclearlecture.domain.fileupload.event.FileEventHandler;
import com.allclearlecture.domain.fileupload.event.FileEventSource;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Properties;

public class FileAppendProducer {
    public static final Logger logger = LoggerFactory.getLogger(FileAppendProducer.class.getName());

    public void startFileMonitoring(String filePath) {
        String topicName = "file-topic";

        Properties props = new Properties();
        props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "34.207.225.250:9092");
        props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(props);

        boolean sync = false;
        File file = new File(filePath);  // 업로드된 파일 경로 사용
        EventHandler eventHandler = new FileEventHandler(kafkaProducer, topicName, sync);

        FileEventSource fileEventSource = new FileEventSource(10000, file, eventHandler);
        Thread fileEventSourceThread = new Thread(fileEventSource);

        fileEventSourceThread.start(); // 모니터링 시작

        try {
            fileEventSourceThread.join(); // 파일 모니터링이 끝날 때까지 대기
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        } finally {
            kafkaProducer.close();
        }
    }
}

