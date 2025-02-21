package com.freenet.openimdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class OpenimDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenimDemoApplication.class, args);
    }
}
