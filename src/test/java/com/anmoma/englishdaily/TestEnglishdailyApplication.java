package com.anmoma.englishdaily;

import org.springframework.boot.SpringApplication;

public class TestEnglishdailyApplication {

    public static void main(String[] args) {
        SpringApplication.from(EnglishdailyApplication::main)
                         .with(TestEnvironmentConfiguration.class)
                         .run(args);
    }

}
