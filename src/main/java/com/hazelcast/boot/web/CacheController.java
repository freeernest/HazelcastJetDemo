package com.hazelcast.boot.web;

import com.hazelcast.boot.service.CacheService;
import com.hazelcast.jet.JetInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("cache")
public class CacheController {

    private JetInstance jetInstance;
    private CacheService cacheService;


    @Autowired
    public CacheController(CacheService cacheService, JetInstance jetInstance) { //@Qualifier("hz_instance") HazelcastInstance hazelcastInstance,
        //this.hazelcastInstance = hazelcastInstance;
        this.jetInstance = jetInstance;
        this.cacheService = cacheService;
    }

    @RequestMapping(
            method = GET,
            path = "/distributed/{propertyName}")
    public String getFromDistributedCache(@PathVariable("propertyName") String propertyName) {
        return (String) jetInstance.getHazelcastInstance().getMap("distributed_cache").get(propertyName);
    }

    @RequestMapping(
            method = POST,
            path = "/distributed/{propertyName}/{value}")
    public String setToDistributedCache(@PathVariable("propertyName") String propertyName, @PathVariable("value") String value) {
        jetInstance.getHazelcastInstance().getMap("distributed_cache").put(propertyName, value);
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