package cn.archliu.horus.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import springfox.documentation.oas.annotations.EnableOpenApi;

@EnableScheduling
@EnableOpenApi
@SpringBootApplication(scanBasePackages = { "cn.archliu.horus" })
@MapperScan(basePackages = { "cn.archliu.horus.infr.domain.*.mapper" })
public class HorusServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(HorusServerApplication.class, args);
	}

}
