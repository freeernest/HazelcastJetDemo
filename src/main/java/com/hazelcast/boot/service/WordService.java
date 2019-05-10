package com.hazelcast.boot.service;

import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.pipeline.Pipeline;
import com.hazelcast.jet.pipeline.Sinks;
import com.hazelcast.jet.pipeline.Sources;
import com.hazelcast.util.WordUtil;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static com.hazelcast.jet.Traversers.traverseArray;
import static com.hazelcast.jet.aggregate.AggregateOperations.counting;
import static com.hazelcast.jet.function.Functions.wholeItem;
import static com.hazelcast.util.WordUtil.*;

@Service
public class WordService {

    public Pipeline buildPipeline(String fileName) {
        Pipeline p = Pipeline.create();
        p.drawFrom(Sources.<Long, String>map(fileName))
                .flatMap(e -> traverseArray(PATTERN.split(e.getValue().toLowerCase())))
                .map(WordUtil::cleanWord)
                .filter(m -> m.length() >= 4)
                .filter(e -> Stream.of(EXCLUDES).noneMatch(s -> s.equals(e)))
                .groupingKey(wholeItem())
                .aggregate(counting())
                .drainTo(Sinks.map(fileName + COUNTS_SOURCE));
        return p;
    }

    public  void loadFile (String fileName, JetInstance jet){
        System.out.println("Loading the file \"" + fileName + "\"");
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
}