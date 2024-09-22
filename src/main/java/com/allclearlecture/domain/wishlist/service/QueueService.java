/*package com.allclearlecture.domain.wishlist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueueService {

    private static final String QUEUE_KEY = "queue";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void addItem(String item) {
        redisTemplate.opsForList().leftPush(QUEUE_KEY, item);
    }

    public String removeItem() {
        return redisTemplate.opsForList().rightPop(QUEUE_KEY);
    }

    public long getQueueLength() {
        return redisTemplate.opsForList().size(QUEUE_KEY);
    }

    public List<String> getAllItems() {
        return redisTemplate.opsForList().range(QUEUE_KEY, 0, -1);
    }
}*/
