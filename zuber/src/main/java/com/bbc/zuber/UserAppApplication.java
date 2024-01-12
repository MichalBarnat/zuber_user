package com.bbc.zuber;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class UserAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserAppApplication.class, args);
	}

	//30.11.2023
	// DESERIALIZACJA JSONA Z KAFKI (JACKSON, MAPSTRUCT)
	// platnosci
	// napiwki <optional>
	// liqubase testowe dane
	// prometheus
	// kafdrop

	// 14.12.2023
	// czat
	// piginacja, softdelete,
	// wireMock
	// widoki bazodanowe
}
