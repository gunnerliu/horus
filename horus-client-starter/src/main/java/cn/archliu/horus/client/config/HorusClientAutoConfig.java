package cn.archliu.horus.client.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = { "cn.archliu.horus.client" })
@EnableFeignClients(basePackages = { "cn.archliu.horus.client.monitor.feign" })
public class HorusClientAutoConfig {

}
