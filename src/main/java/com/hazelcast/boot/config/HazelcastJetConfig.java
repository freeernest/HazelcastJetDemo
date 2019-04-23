package com.hazelcast.boot.config;

import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.XmlClientConfigBuilder;
import com.hazelcast.config.ClasspathXmlConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastJetConfig {

    @Bean
    public Config config() {
        final ClasspathXmlConfig classpathXmlConfig = new ClasspathXmlConfig("hazelcast.xml");
        return classpathXmlConfig;
    }

    @Bean
    public ClientConfig clientConfig() {
        ClientConfig clientCofig = new XmlClientConfigBuilder().build();
        return clientCofig;
    }

    @Bean
    public JetInstance jetInstance(ClientConfig cc) {
        return Jet.newJetClient(cc);
    }

    @Bean
    public HazelcastInstance hazelcastInstance(JetInstance jetInstance) {
        return jetInstance.getHazelcastInstance();
    }

}
