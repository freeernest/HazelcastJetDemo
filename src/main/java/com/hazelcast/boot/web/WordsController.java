package com.hazelcast.boot.web;

import com.hazelcast.boot.service.WordService;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.pipeline.Pipeline;
import com.hazelcast.util.WordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import static com.hazelcast.util.WordUtil.COUNTS_SOURCE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;


@RestController
@RequestMapping("wordcount")
public class WordsController {

    private WordService wordService;
    private JetInstance jetInstance;
    private HazelcastInstance hazelcastInstance;


    @Autowired
    public WordsController(WordService wordService, JetInstance jetInstance, @Qualifier("hz_instance") HazelcastInstance hazelcastInstance) {
        this.wordService = wordService;
        this.jetInstance = jetInstance;
        this.hazelcastInstance = hazelcastInstance;
    }

    @RequestMapping(
            method = GET,
            path = "/{artist}")
    public String get(@PathVariable("artist") String artist, @RequestParam(required = false, defaultValue = "100") int limit) {
        if (jetInstance.getMap(artist + COUNTS_SOURCE).isEmpty()) {
            wordService.loadFile(artist, jetInstance);
            Pipeline p = wordService.buildPipeline(artist);
            jetInstance.newJob(p).join();
            IMap<Object, Object> map = hazelcastInstance.getMap(artist);
        }

        return WordUtil.printResults(jetInstance.getMap(artist + COUNTS_SOURCE), limit);

    }

    @RequestMapping(
            method = GET,
            path = "/{artist}/top{limit}")
    public String getTopX(@PathVariable("artist") String artist,  @PathVariable("limit") int limit) {
        if (jetInstance.getMap(artist + COUNTS_SOURCE).isEmpty()) {
            wordService.loadFile(artist, jetInstance);
            Pipeline p = wordService.buildPipeline(artist);
            jetInstance.newJob(p).join();
            hazelcastInstance.getMap(artist);
        }
        return WordUtil.printResults(jetInstance.getMap(artist + COUNTS_SOURCE), limit);
    }
}