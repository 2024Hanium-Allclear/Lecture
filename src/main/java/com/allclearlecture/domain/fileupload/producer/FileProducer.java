package com.allclearlecture.domain.fileupload.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class FileProducer {
    public static final Logger logger = LoggerFactory.getLogger(FileProducer.class.getName());
    public static void main(String[] args) {
        //토픽 이름
        String topicName = "file-topic";

        Properties props  = new Properties();

        props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "34.207.225.250:9092");
        props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());


        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(props);

        //KafkaProducer객체 생성 -> ProducerRecord생성 -> send() 비동기 방식 전송

        //파일 위치
        String filePath = "D:\\4th_grade\\Hanium\\Backend\\Lecture\\src\\main\\resources\\excelFiles\\courses.txt";
        sendFileMessage(kafkaProducer, topicName,filePath);
        kafkaProducer.close();

    }

    private static void sendFileMessage(KafkaProducer<String, String> kafkaProducer, String topicName, String filePath) {
        String line = " ";
        // 패턴 정의: 쉼표로 구분되지만, 큰따옴표로 감싸인 쉼표는 무시
        final String regex = "(?<=^|,)(\"(?:[^\"]|\"\")*\"|[^,]*)";

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {

            while ((line = bufferedReader.readLine()) != null) {
                // 정규식에 맞춰 라인을 토큰화
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(line);

                String key = null;
                List<String> tokens = new ArrayList<>();

                while (matcher.find()) {
                    String token = matcher.group().replaceAll("^\"|\"$", ""); // 큰따옴표 제거
                    tokens.add(token); // tokens 리스트에 추가
                }
                System.out.println("ss테스트 문자열");

                // 토큰 값을 로그로 출력
                for (int i = 0; i < tokens.size(); i++) {
                    logger.info("Token " + i + ": " + tokens.get(i));
                }
                if (!tokens.isEmpty()) {
                    key = tokens.get(0); // 첫 번째 토큰을 key로 사용
                    StringBuffer value = new StringBuffer();

                    // 기존 for 루프를 사용하여 value 구성
                    for (int i = 1; i < tokens.size(); i++) {
                        if (i != (tokens.size() - 1)) {
                            value.append(tokens.get(i)).append(",");
                        } else {
                            value.append(tokens.get(i));
                        }
                    }

                    sendMessage(kafkaProducer, topicName, key, value.toString());
                }
            }
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
    }

    private static void sendMessage(KafkaProducer<String, String> kafkaProducer, String topicName, String key, String value) {
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topicName,key, value);
        logger.info("key:{}, value:{}" , key, value);

        kafkaProducer.send(producerRecord, (metadata, exception) -> {
            if (exception == null) {
                logger.info("\n ###### record metadata received ##### \n" +
                        "partition:" + metadata.partition() + "\n" +
                        "offset:" + metadata.offset() + "\n" +
                        "timestamp:" + metadata.timestamp());
            } else {
                logger.error("exception error from broker " + exception.getMessage());
            }
        });
    }

}
