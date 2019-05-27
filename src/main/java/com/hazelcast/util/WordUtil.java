package com.hazelcast.util;

import com.hazelcast.core.IMap;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static java.util.Comparator.comparingLong;

/**
 * Utility class contains common methods for loading / cleaning data
 */
public class WordUtil {
    public static final Pattern PATTERN = Pattern.compile("\\W+");
    public static final String[] EXCLUDES =
            {"which", "would", "could", "that", "with", "were", "this", "what", "there",
                    "from", "their", "those", "chorus", "have", "them", "when", "been",
                    "they", "will", "more", "about", "into", "then", "your"};

    public static final String SOURCE_SUFFIX = "_source";
    public static final String COUNTS_SOURCE = "_counts";

    private WordUtil() {
    }

    public static String cleanWord(String word) {
        return word.replaceAll("[^A-Za-zA-Яа-я]", "");
    }

    public static void fillMapWithData(String fileName, Map<Integer, String> map)
            throws Exception {


        InputStream is = WordUtil.class.getClassLoader().getResourceAsStream(fileName);
        LineNumberReader reader = new LineNumberReader(new InputStreamReader(is));

        String line;
        Integer lineNum = 0;
        Map<Integer, String> localMap = new HashMap<>();
        while ((line = reader.readLine()) != null) {
            lineNum++;
            localMap.put(lineNum, line);
        }
        map.putAll(localMap);

        is.close();
        reader.close();
    }

    public static String printResults(IMap<String, Long> resultsMap, int limit) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append(" Top " + limit + " entries are:\n");
        sb.append("\n");
        sb.append("/----------------+----------------\\\n");
        sb.append("|      Count     |      Word      |\n");
        sb.append("|----------------+----------------|\n");
        resultsMap.entrySet().stream()
                .sorted(comparingLong(Map.Entry<String, Long>::getValue).reversed())
                .limit(limit)
                .forEach(e -> sb.append(String.format("|%15d | %-15s|%n", e.getValue(), e.getKey())));
        sb.append("\\----------------+----------------/\n");
        return sb.toString();
    }
}
