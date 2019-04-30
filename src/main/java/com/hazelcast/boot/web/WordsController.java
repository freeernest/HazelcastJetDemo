package com.hazelcast.boot.web;

import com.hazelcast.boot.service.WordService;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.pipeline.Pipeline;
import com.hazelcast.util.WordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.hazelcast.util.WordUtil.COUNTS_SOURCE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("wordcount")
public class WordsController {

    private WordService wordService;
    private JetInstance jetInstance;


    @Autowired
    public WordsController(WordService wordService, JetInstance jetInstance) {
        this.wordService = wordService;
        this.jetInstance = jetInstance;
    }

    @RequestMapping(
            method = GET,
            path = "/{artist}")
    public String get(@PathVariable("artist") String artist) {
        wordService.loadFile(artist, jetInstance);
        Pipeline p = wordService.buildPipeline(artist);
        jetInstance.newJob(p).join();
        return WordUtil.printResults(jetInstance.getMap(artist + COUNTS_SOURCE), 100);
    }

    @RequestMapping(
            method = GET,
            path = "/{artist}/top{x}")
    public String getTopX(@PathVariable("artist") String artist, @PathVariable("x") int limit) {
        if (jetInstance.getMap(artist + COUNTS_SOURCE).isEmpty()) {
            wordService.loadFile(artist, jetInstance);
            Pipeline p = wordService.buildPipeline(artist);
            jetInstance.newJob(p).join();
        }
        return WordUtil.printResults(jetInstance.getMap(artist + COUNTS_SOURCE), limit);
    }

    @RequestMapping(
            method = GET,
            path = "/distributed_cache/{propertyName}")
    public String getFromDistributedCache(@PathVariable("propertyName") String propertyName) {

        return "";
    }

    @RequestMapping(
            method = POST,
            path = "/distributed_cache/{propertyName}/{value}")
    public String setToDistributedCache(@PathVariable("propertyName") String propertyName, @PathVariable("value") String artist) {

        return "";
    }


}