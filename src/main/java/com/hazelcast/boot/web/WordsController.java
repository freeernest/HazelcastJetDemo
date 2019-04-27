package com.hazelcast.boot.web;

import com.hazelcast.boot.service.WordService;
import com.hazelcast.core.IMap;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.pipeline.Pipeline;
//import com.hazelcast.jet.stream.IStreamMap;
import com.hazelcast.util.WordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.hazelcast.util.WordUtil.COUNTS_SOURCE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

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
        //final IStreamMap<Integer, String> artistMap = jetInstance.getMap(artist + WordUtil.SOURCE_SUFFIX);
        //final IStreamMap<String, Integer> wordCount = wordService.wordCount(artistMap);
        Pipeline p = wordService.buildPipeline(artist);
        jetInstance.newJob(p).join();
        IMap<String, Long> counts = jetInstance.getMap(artist + COUNTS_SOURCE);
        return WordUtil.printResults(artist, jetInstance);
    }

//    @RequestMapping(
//            method = GET,
//            path = "/{artist}/top{x}")
//    public List<String> getTopX(@PathVariable("artist") String artist, @PathVariable("x") int top) {
//        final IStreamMap<String, Integer> map = jetInstance.getMap(artist + WordUtil.COUNTS_SOURCE);
//        return wordService.topXWords(map, top);
//    }
}