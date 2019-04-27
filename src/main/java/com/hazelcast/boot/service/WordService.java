package com.hazelcast.boot.service;

import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.pipeline.Pipeline;
import com.hazelcast.jet.pipeline.Sinks;
import com.hazelcast.jet.pipeline.Sources;
//import com.hazelcast.jet.stream.IStreamMap;
import com.hazelcast.util.WordUtil;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.hazelcast.jet.Traversers.traverseArray;
import static com.hazelcast.jet.aggregate.AggregateOperations.counting;
import static com.hazelcast.jet.function.Functions.wholeItem;
//import static com.hazelcast.jet.stream.DistributedCollectors.toIMap;
//import static com.hazelcast.jet.stream.DistributedCollectors.toList;
import static com.hazelcast.util.WordUtil.*;

@Service
public class WordService {

    public Pipeline buildPipeline(String fileName) {
        Pattern delimiter = Pattern.compile("\\W+");
        Pipeline p = Pipeline.create();
        p.drawFrom(Sources.<Long, String>map(fileName))
                .flatMap(e -> traverseArray(delimiter.split(e.getValue().toLowerCase())))
                .filter(word -> !word.isEmpty())
                .groupingKey(wholeItem())
                .aggregate(counting())
                .drainTo(Sinks.map(fileName + COUNTS_SOURCE));
        return p;
    }

    public  void loadFile (String fileName, JetInstance jet){
        System.out.println("Loading The Complete Works of William Shakespeare");
        try {
            long[] lineNum = {0};
            Map<Long, String> bookLines = new HashMap<>();
            InputStream stream = getClass().getResourceAsStream("/" + fileName+".txt");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                reader.lines().forEach(line -> bookLines.put(++lineNum[0], line));
            }
            jet.getMap(fileName).putAll(bookLines);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    public IStreamMap<String, Integer> wordCount(IStreamMap<Integer, String> source) {
//        final String artist = source.getName().split("_")[0];
//        return source.stream()
//                .flatMap(m -> Stream.of(PATTERN.split(m.getValue())))
//                .map(String::toLowerCase)
//                .map(WordUtil::cleanWord)
//                .filter(m -> m.length() >= 5)
//                .collect(toIMap(
//                        artist + COUNTS_SOURCE,
//                        m -> m,
//                        m -> 1,
//                        Integer::sum));
//    }

//    public List<String> topXWords(IStreamMap<String, Integer> wordCount, int top) {
//        return wordCount.stream()
//                .filter(e -> Stream
//                        .of(EXCLUDES)
//                        .noneMatch(s -> s.equals(e.getKey())))
//                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
//                //.sorted((o1, o2) -> compare(o1.getValue(), o2.getValue()))
//                .limit(top)
//                .map(e -> e.getKey() + ":" + e.getValue())
//                .collect(toList());
//    }
}