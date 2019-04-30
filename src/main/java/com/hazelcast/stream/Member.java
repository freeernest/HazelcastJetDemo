package com.hazelcast.stream;

import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.config.InstanceConfig;
import com.hazelcast.jet.config.JetConfig;

import static java.lang.Runtime.getRuntime;

public class Member {
    public static void main(String[] args) throws Exception {
        JetConfig cfg = new JetConfig();
        cfg.setInstanceConfig(new InstanceConfig().setCooperativeThreadCount(
                Math.max(1, getRuntime().availableProcessors() / 2)));
        System.out.println("Creating Jet instance");
        JetInstance jet = Jet.newJetInstance(cfg);
        //System.out.println("Creating Jet instance 2");
        //Jet.newJetInstance(cfg);

        //IMap<Integer, String> source = instance.getMap("source");

//        final Map<Integer, String> disturbedMap = jet.getMap("disturbed" + SOURCE_SUFFIX);
//        final Map<Integer, String> gagaMap = jet.getMap("gaga" + SOURCE_SUFFIX);
//        fillMapWithData("disturbed.txt", disturbedMap);
//        fillMapWithData("lady_gaga.txt", gagaMap);

        //region loading war and peace
        //System.out.println("Loading War and Peace...");
        //fillMapWithData("war_and_peace_eng.txt", source);
        //System.out.println("Done War and Peace...");
        //endregion

    }
}