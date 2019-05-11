package com.hazelcast.stream;

import com.hazelcast.config.ClasspathXmlConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.util.WordUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static com.hazelcast.util.WordUtil.EXCLUDES;
import static com.hazelcast.util.WordUtil.PATTERN;
import static java.util.stream.Collectors.toMap;


public class IMDGMember {
    public static void main(String[] args) {
        System.out.println("Creating Hazelcast IMDG instance");
        HazelcastInstance hzInstance = Hazelcast.newHazelcastInstance(new ClasspathXmlConfig("hazelcast.xml"));

    }
}