package com.cloud.photo.trans;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@MapperScan(basePackages = {"com.cloud.photo.trans.mapper"})
@EnableDiscoveryClient
public class CloudPhotoTransApplication
{
    public static void main( String[] args )
    {
       SpringApplication.run(CloudPhotoTransApplication.class,args);
    }
}
