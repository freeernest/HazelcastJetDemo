package com.hazelcast.boot.config;

import com.hazelcast.config.ClasspathXmlConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.config.InstanceConfig;
import com.hazelcast.jet.config.JetConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.lang.Runtime.getRuntime;

@Configuration
public class HazelcastJetConfig {

    @Bean
    public Config config() {
        final ClasspathXmlConfig classpathXmlConfig = new ClasspathXmlConfig("hazelcast.xml");
        return classpathXmlConfig;
    }

    @Bean
    public JetConfig jetConfig() {
        final JetConfig cfg = new JetConfig();
        cfg.getMetricsConfig().setMetricsForDataStructuresEnabled(true);
        cfg.setInstanceConfig(new InstanceConfig().setCooperativeThreadCount(
                Math.max(1, getRuntime().availableProcessors() / 2)));
        return cfg;
    }

    @Bean
    public JetInstance jetInstance(JetConfig cc) {
        return Jet.newJetInstance(cc);
    }

    @Bean
    @Qualifier("hz_instance")
    public HazelcastInstance hazelcastInstance(JetInstance jetInstance) {
        return jetInstance.getHazelcastInstance();
    }
}
