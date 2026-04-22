package com.sahil.assignment.scheduler;


import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class NotificationScheduler {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Scheduled(fixedRate = 300000)
    public void sweepPendingNotifications() {
        String usersSetKey = "users_with_pending_notifs";
        Set<String> userIds = redisTemplate.opsForSet().members(usersSetKey);

        if (userIds == null || userIds.isEmpty()) {
            return;
        }

        for (String userId : userIds) {
            String pendingKey = "user:" + userId + ":pending_notifs";
            List<String> notifications = redisTemplate.opsForList().range(pendingKey, 0, -1);

            if (notifications == null || notifications.isEmpty()) {
                continue;
            }

            int total = notifications.size();
            String firstNotif = notifications.get(0);

            if (total == 1) {
                System.out.println("Summarized Push Notification for User "
                        + userId + ": " + firstNotif);
            } else {
                System.out.println("Summarized Push Notification for User "
                        + userId + ": " + firstNotif
                        + " and " + (total - 1) + " others interacted with your posts.");
            }

            redisTemplate.delete(pendingKey);
            redisTemplate.opsForSet().remove(usersSetKey, userId);
        }
    }
}