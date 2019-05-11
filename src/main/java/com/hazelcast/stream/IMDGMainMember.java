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


public class IMDGMainMember {
    public static void main(String[] args) throws Exception {
        System.out.println("Creating Hazelcast IMDG instance");
        HazelcastInstance hzInstance = Hazelcast.newHazelcastInstance(new ClasspathXmlConfig("hazelcast.xml"));


        Map<Integer, String> source = new HashMap<>();

        WordUtil.fillMapWithData("war_and_peace_eng.txt", source);

        final Set<Map.Entry<Integer, String>> streamMap = source.entrySet();

        Map<String, Integer> counts = streamMap.stream()
                .flatMap(m -> Stream.of(PATTERN.split(m.getValue())))
                .filter(e -> Stream.of(EXCLUDES).noneMatch(s -> s.equals(e)))
                .map(String::toLowerCase)
                .map(WordUtil::cleanWord)
                .filter(m -> m.length() >= 4)
                .collect(toMap(
                        key -> key,
                        value -> 1,
                        Integer::sum));

        IMap<String, Integer> demoMap = hzInstance.getMap("demo_map");
        demoMap.putAll(counts);
    }
}