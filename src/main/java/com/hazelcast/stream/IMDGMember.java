package com.hazelcast.stream;

import com.hazelcast.config.ClasspathXmlConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import static java.util.stream.Collectors.toMap;


public class IMDGMember {
    public static void main(String[] args) {
        System.out.println("Creating Hazelcast IMDG instance");
        HazelcastInstance hzInstance = Hazelcast.newHazelcastInstance(new ClasspathXmlConfig("hazelcast.xml"));

    }
}