package com.monitor.example;

import com.monitor.spring.MonitorConfiguration;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author WuQinglong
 * @since 2023/3/3 17:35
 */
@SpringBootApplication
public class ExampleApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

    }

    @Bean
    public MonitorConfiguration monitorConfiguration() {
        return MonitorConfiguration.newBuilder()
            .interval(1)
            .enabledTomcatSampler(true)
//            .enabledLogReporter()
//            .enabledThread()
            .build();
    }

}
