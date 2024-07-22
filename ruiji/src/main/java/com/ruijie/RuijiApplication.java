package com.ruijie;


import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@MapperScan("com.ruijie.mapper")
@ServletComponentScan
@EnableTransactionManagement
public class RuijiApplication {
    public static void main(String[] args) {
        SpringApplication.run(RuijiApplication.class, args);
    }
}
