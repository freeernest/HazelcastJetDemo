package com.hazelcast.boot.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CacheService {

    Map<String, String> localMap = new HashMap<>();

    public Map<String, String> getLocalMap() {
        return localMap;
    }
}