//package com.hazelcast.stream;
//
//import com.hazelcast.client.config.ClientConfig;
//import com.hazelcast.core.IMap;
//import com.hazelcast.jet.Jet;
//import com.hazelcast.jet.JetInstance;
//import com.hazelcast.jet.config.JetConfig;
//import com.hazelcast.jet.stream.IStreamMap;
//import com.hazelcast.util.WordUtil;
//
//import java.util.Map;
//import java.util.stream.Stream;
//
//import static com.hazelcast.jet.stream.DistributedCollectors.toIMap;
//import static com.hazelcast.util.WordUtil.EXCLUDES;
//import static com.hazelcast.util.WordUtil.PATTERN;
//
///**
// * Word count distributed version
// */
//public class WordCountWithDistributedStreams {
//
//    public static void main(String[] args) throws Exception {
//
//        //region init Jet Engine
//        JetConfig c = new JetConfig();
//        ClientConfig cc = new ClientConfig();
//        final JetInstance jetInstance = Jet.newJetClient(cc);
//        final IStreamMap<Integer, String> streamMap = jetInstance.getMap("source");
//        //endregion
//
//        //region word count
//        IStreamMap<String, Integer> counts = streamMap.stream()
//                .flatMap(m -> Stream.of(PATTERN.split(m.getValue())))
//                .map(String::toLowerCase)
//                .map(WordUtil::cleanWord)
//                .filter(m -> m.length() >= 5)
//                .collect(toIMap(
//                        "counts",
//                        m -> m,
//                        m -> 1,
//                        Integer::sum));
//        //endregion
//
//        System.out.println(counts.getName());
//
//        // region top20
//        final IMap<String, Integer> top20Map = counts.stream()
//                .filter(e -> Stream
//                        .of(EXCLUDES)
//                        .noneMatch(s -> s.equals(e.getKey())))
//                .sorted()
//                .limit(20)
//                .collect(toIMap(
//                        "top20",
//                        Map.Entry::getKey,
//                        Map.Entry::getValue,
//                        (left, right) -> left));
//
//        System.out.println("Counts=" + top20Map.entrySet());
//        //endregion
//
//        Jet.shutdownAll();
//    }
//
//
//}