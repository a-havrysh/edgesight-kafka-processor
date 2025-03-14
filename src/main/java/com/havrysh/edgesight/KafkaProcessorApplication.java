package com.havrysh.edgesight;

import static org.springframework.boot.SpringApplication.run;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableJpaRepositories("com.havrysh.edgesight.repository.jpa")
@EnableElasticsearchRepositories("com.havrysh.edgesight.repository.elasticsearch")
public class KafkaProcessorApplication {

    public static void main(String[] args) {
        run(KafkaProcessorApplication.class, args);
    }
}