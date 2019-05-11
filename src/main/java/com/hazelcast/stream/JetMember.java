package com.hazelcast.stream;

import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.config.InstanceConfig;
import com.hazelcast.jet.config.JetConfig;

import static java.lang.Runtime.getRuntime;

public class JetMember {
    public static void main(String[] args) throws Exception {
        JetConfig cfg = new JetConfig();
        cfg.setInstanceConfig(new InstanceConfig().setCooperativeThreadCount(
                Math.max(1, getRuntime().availableProcessors() / 2)));
        System.out.println("Creating Jet instance");
        JetInstance jet = Jet.newJetInstance(cfg);
    }
}