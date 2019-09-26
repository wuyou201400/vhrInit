package org.sang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("org.sang.mapper")
@EnableCaching
@EnableScheduling//开启定时任务支持
public class HrserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(HrserverApplication.class, args);
	}
}
