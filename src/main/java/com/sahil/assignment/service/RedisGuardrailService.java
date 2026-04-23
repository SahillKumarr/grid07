package com.sahil.assignment.service;

import com.sahil.assignment.exception.BotCapExceededException;
import com.sahil.assignment.exception.CooldownActiveException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisGuardrailService {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisGuardrailService(RedisTemplate<String, String> redisTemplate){
        this.redisTemplate=redisTemplate;
    }

    public void checkHorizontalCap(Long postId) {
        String key = "post:" + postId + ":bot_count";
        Long count = redisTemplate.opsForValue().increment(key);
        if (count > 100) {
            redisTemplate.opsForValue().decrement(key);
            throw new BotCapExceededException("This post has reached the maximum of 100 bot replies");
        }
    }

    public void checkVerticalCap(int depthLevel) {
        if (depthLevel > 20) {
            throw new IllegalArgumentException("Comment thread cannot go deeper than 20 levels");
        }
    }

    public void checkCooldown(Long botId, Long humanId) {
        String key = "cooldown:bot_" + botId + ":human_" + humanId;
        try {
            Boolean exists = redisTemplate.hasKey(key);

            if (Boolean.TRUE.equals(exists)) {
                throw new CooldownActiveException("Bot " + botId + " is on cooldown with human " + humanId);
            }
            redisTemplate.opsForValue().set(key, "1", 600, TimeUnit.SECONDS);

        } catch (CooldownActiveException e) {
            throw e;
        } catch (Exception e) {
            System.out.println("REDIS ERROR: " + e.getMessage());
        }
    }


    public void updateViralityScore(Long postId, String interactionType) {
        String key = "post:" + postId + ":virality_score";
        switch (interactionType) {
            case "BOT_REPLY" -> redisTemplate.opsForValue().increment(key, 1);
            case "HUMAN_LIKE" -> redisTemplate.opsForValue().increment(key, 20);
            case "HUMAN_COMMENT" -> redisTemplate.opsForValue().increment(key, 50);
        }
    }

    public Long getViralityScore(Long postId) {
        String key = "post:" + postId + ":virality_score";
        String value = redisTemplate.opsForValue().get(key);
        return value != null ? Long.parseLong(value) : 0L;
    }
}
