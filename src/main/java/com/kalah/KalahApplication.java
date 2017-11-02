package com.kalah;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class KalahApplication extends WebMvcConfigurerAdapter {

    public static void main(String[] args) {
        SpringApplication.run(KalahApplication.class, args);
    }
}
