package com.example.slavedb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@SpringBootApplication
public class SlaveDbApplication {

    private static String dbDir = "/home/boys/IdeaProjects/finalProject/nosql/";

    @Bean
    RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }
    @Bean
    SlaveDB getDatabase(){
        SlaveDB slaveDB = new SlaveDB(dbDir);
        try {
            slaveDB.loadDatabase(dbDir);
        }catch (IOException e) {
            e.printStackTrace();
        }
       return slaveDB;
    }

    public static void main(String[] args) {
        SpringApplication.run(SlaveDbApplication.class, args);
    }

}
