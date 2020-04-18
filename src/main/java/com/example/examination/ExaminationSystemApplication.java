package com.example.examination;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan(basePackages = "com.example.examination.dao")
public class ExaminationSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExaminationSystemApplication.class, args);
    }

}
