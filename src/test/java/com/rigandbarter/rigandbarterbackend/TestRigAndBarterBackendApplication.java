package com.rigandbarter.rigandbarterbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestRigAndBarterBackendApplication {

	public static void main(String[] args) {
		SpringApplication.from(RigAndBarterBackendApplication::main).with(TestRigAndBarterBackendApplication.class).run(args);
	}

}
