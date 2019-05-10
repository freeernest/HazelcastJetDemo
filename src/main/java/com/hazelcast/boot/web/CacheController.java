package com.hazelcast.boot.web;

import com.hazelcast.boot.service.CacheService;
import com.hazelcast.boot.service.WordService;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.pipeline.Pipeline;
import com.hazelcast.util.WordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.hazelcast.util.WordUtil.COUNTS_SOURCE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("cache")
public class CacheController {

    private HazelcastInstance hazelcastInstance;
    private CacheService cacheService;


    @Autowired
    public CacheController( @Qualifier("hz_instance") HazelcastInstance hazelcastInstance, CacheService cacheService) {
        this.hazelcastInstance = hazelcastInstance;
        this.cacheService = cacheService;
    }

    @RequestMapping(
            method = GET,
            path = "/distributed/{propertyName}")
    public String getFromDistributedCache(@PathVariable("propertyName") String propertyName) {
        return (String) hazelcastInstance.getMap("distributed_cache").get(propertyName);
    }

    @RequestMapping(
            method = POST,
            path = "/distributed/{propertyName}/{value}")
    public String setToDistributedCache(@PathVariable("propertyName") String propertyName, @PathVariable("value") String value) {
        hazelcastInstance.getMap("distributed_cache").put(propertyName, value);
        return "Succeeded";
    }

    @RequestMapping(
            method = GET,
            path = "/local/{propertyName}")
    public String getFromLocalCache(@PathVariable("propertyName") String propertyName) {
        return cacheService.getLocalMap().get(propertyName);
    }

    @RequestMapping(
            method = POST,
            path = "/local/{propertyName}/{value}")
    public String setToLocalCache(@PathVariable("propertyName") String propertyName, @PathVariable("value") String value) {
        cacheService.getLocalMap().put(propertyName, value);
        return "Succeeded";
    }


}