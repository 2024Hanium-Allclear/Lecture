package com.allclearlecture.domain.wishlist.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
/*
@Service
public class SQSService {

    private final AmazonSQS sqs;

    @Value("${aws.region}")
    private String region;

    @Value("${aws.sqs.queue-url}")
    private String queueUrl;

    public SQSService(@Value("${aws.region}") String region) {
        this.region = region;
        this.sqs = AmazonSQSClientBuilder.standard()
                .withRegion(region) // 지역 설정
                .build();
    }

    public void sendMessage(String messageBody, String messageDeduplicationId, String messageGroupId) {
        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl(queueUrl) // 큐 URL이 올바른지 확인
                .withMessageBody(messageBody)
                .withMessageGroupId(messageGroupId) // FIFO 큐에서 필요
                .withMessageDeduplicationId(messageDeduplicationId); // 중복 방지 ID 설정
        sqs.sendMessage(sendMessageRequest); // SQS 클라이언트가 제대로 설정되었는지 확인
    }
}
*/