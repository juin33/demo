package com.example.demo;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication()
@ImportResource("spring/appContext.xml")
public class DemoApplication {
	private static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);
	public static void main(String[] args) {
		logger.debug("begin to start-up [test]");
		SpringApplication.run(DemoApplication.class, args);
		logger.debug("start-up [test] success !!!");
	}
}
