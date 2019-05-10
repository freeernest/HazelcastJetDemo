package com.hazelcast.boot.service;

import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.pipeline.Pipeline;
import com.hazelcast.util.WordUtil;
import org.junit.Test;



import static com.hazelcast.util.WordUtil.COUNTS_SOURCE;

public class WordServiceTest {


    String testFile = "war_and_peace_eng";


    @Test
    public void wordCountTest() {
        JetInstance jet = Jet.newJetInstance();
        WordService ws = new WordService();
        ws.loadFile(testFile, jet);
        Pipeline p = ws.buildPipeline(testFile);
        jet.newJob(p).join();

        System.out.println(WordUtil.printResults(jet.getMap(testFile + COUNTS_SOURCE), 20));
    }

}