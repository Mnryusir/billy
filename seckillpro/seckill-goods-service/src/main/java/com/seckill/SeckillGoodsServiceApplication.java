package com.seckill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class SeckillGoodsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeckillGoodsServiceApplication.class, args);
    }

}
