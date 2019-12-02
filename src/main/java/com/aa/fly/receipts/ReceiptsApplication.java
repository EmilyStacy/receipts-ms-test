package com.aa.fly.receipts;

/**
 * Created by 629874 on 4/25/2019.
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ReceiptsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReceiptsApplication.class, args);
    }

}
