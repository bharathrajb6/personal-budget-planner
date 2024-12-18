package com.example.personal_budget_planner.Service;

public interface RedisService {

    <T> T getData(String key, Class<T> responseClass);

    void setData(String key, Object object, Long ttl);

    void deleteData(String key);
}
