package com.sahil.assignment.service;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class NotificationService {

    private RedisTemplate<String, String> redisTemplate;

    public NotificationService(RedisTemplate<String, String> redisTemplate){
        this.redisTemplate= redisTemplate;
    }

    public void handleBotInteraction(Long userId, String message) {
        String cooldownKey = "notif_cooldown:" + userId;
        String pendingKey = "user:" + userId + ":pending_notifs";
        String usersSetKey = "users_with_pending_notifs";

        Boolean onCooldown = redisTemplate.hasKey(cooldownKey);
        if (Boolean.TRUE.equals(onCooldown)) {
            redisTemplate.opsForList().rightPush(pendingKey, message);
            redisTemplate.opsForSet().add(usersSetKey, String.valueOf(userId));
        } else {
            System.out.println("Push Notification Sent to User " + userId + ": " + message);
            redisTemplate.opsForValue().set(cooldownKey, "1", 900, TimeUnit.SECONDS);
        }
    }
}
