package com.allclearlecture.domain.fileupload.controller;
;
import com.allclearlecture.domain.fileupload.consumer.FileToDBConsumer;
import com.allclearlecture.domain.fileupload.consumer.LecturesDBHandler;
import com.allclearlecture.domain.fileupload.enums.FileUploadErrorCode;
import com.allclearlecture.domain.fileupload.exception.EmptyFileExceptionHandler;
import com.allclearlecture.domain.fileupload.exception.FileMonitoringExceptionHandler;
import com.allclearlecture.domain.fileupload.exception.FileUploadExceptionHandler;
import com.allclearlecture.domain.fileupload.exception.KafkaConsumerExceptionHandler;
import com.allclearlecture.domain.fileupload.producer.FileAppendProducer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.allclearlecture.domain.fileupload.enums.FileUploadErrorCode.FILE_EMPTY;

@Controller
@RequestMapping("/api/fileupload")
@CrossOrigin(origins = "*")
public class FileUploadController {

    private final FileAppendProducer fileAppendProducer;
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);  // 2개의 스레드를 사용

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;

    public FileUploadController() {
        this.fileAppendProducer = new FileAppendProducer();
    }

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new EmptyFileExceptionHandler(FILE_EMPTY);
        }

        try {
            // 파일을 서버의 특정 경로에 저장
            File dest = new File("D:\\4th_grade\\Hanium\\Backend\\Lecture\\src\\main\\resources\\excelFiles\\" + file.getOriginalFilename());
            file.transferTo(dest);

            // 1. 파일 모니터링 시작
            executorService.submit(() -> {
                try {
                    fileAppendProducer.startFileMonitoring(dest.getAbsolutePath());
                } catch (Exception e) {
                    // 파일 모니터링 중 예외 처리
                    throw new FileMonitoringExceptionHandler(FileUploadErrorCode.FILE_MONITORING_ERROR);
                }
            });
            // 2. Kafka Consumer 시작
            executorService.submit(this::startKafkaConsumer);

            return ResponseEntity.ok("파일 업로드 성공!");

        } catch (IOException e) {
            e.printStackTrace();
            throw new FileUploadExceptionHandler(FileUploadErrorCode.FILE_UPLOAD_ERROR);
        }
    }

    private void startKafkaConsumer() {
        try {
            String topicName = "file-topic";

            Properties props = new Properties();
            props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "34.207.225.250:9092");
            props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "file-group");
            props.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");


            LecturesDBHandler lecturesDBHandler = new LecturesDBHandler(url, user, password);

            FileToDBConsumer<String, String> fileToDBConsumer = new FileToDBConsumer<>(props, List.of(topicName), lecturesDBHandler);
            fileToDBConsumer.startConsuming("async", 3000);  // Kafka Consumer 시작
        } catch (Exception e) {
            // Kafka 소비자 시작 중 예외 처리
            throw new KafkaConsumerExceptionHandler(FileUploadErrorCode.KAFKA_CONSUMER_ERROR);
        }
    }
}
