package com.calypso.binar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class ExampleApp {

    public static void main(String... args) throws NoSuchAlgorithmException, KeyManagementException {
        SpringApplication.run(ExampleApp.class, args);
    }

}
