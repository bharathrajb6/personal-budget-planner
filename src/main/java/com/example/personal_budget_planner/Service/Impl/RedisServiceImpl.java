package com.example.personal_budget_planner.Service.Impl;

import com.example.personal_budget_planner.Exceptions.CacheException;
import com.example.personal_budget_planner.Service.RedisService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate redisTemplate;
    public ObjectMapper objectMapper = null;

    @Autowired
    public RedisServiceImpl(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            return new ObjectMapper();
        }
        return objectMapper;
    }

    /**
     * Get data from Redis Cache
     *
     * @param key
     * @param responseClass
     * @param <T>
     * @return
     */
    @Override
    public <T> T getData(String key, Class<T> responseClass) {
        try {
            Object data = redisTemplate.opsForValue().get(key);
            if (data != null) {
                ObjectMapper mapper = getObjectMapper();
                if (responseClass.equals(List.class)) {
                    return (T) mapper.readValue(data.toString(), new TypeReference<List<T>>() {
                    });
                } else {
                    return mapper.readValue(data.toString(), responseClass);
                }
            }
        } catch (Exception exception) {
            throw new CacheException(exception.getMessage());
        }
        return null;
    }

    /**
     * Set data in Redis Cache
     *
     * @param key
     * @param object
     * @param ttl
     */
    @Override
    public void setData(String key, Object object, Long ttl) {
        try {
            ObjectMapper mapper = getObjectMapper();
            String jsonValue = mapper.writeValueAsString(object);
            redisTemplate.opsForValue().set(key, jsonValue, ttl, TimeUnit.SECONDS);
        } catch (Exception exception) {
            throw new CacheException(exception.getMessage());
        }
    }

    /**
     * Delete data from Redis Cache
     *
     * @param key
     */
    @Override
    public void deleteData(String key) {
        try {
            redisTemplate.opsForValue().getAndDelete(key);
        } catch (Exception exception) {
            throw new CacheException(exception.getMessage());
        }
    }
}
