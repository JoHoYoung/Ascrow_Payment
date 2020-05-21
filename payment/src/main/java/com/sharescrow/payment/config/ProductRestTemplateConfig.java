package com.sharescrow.payment.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class ProductRestTemplateConfig {

	final private String PRODUCT_API_URI = "http://localhost:8080";

	@Bean
	public RestTemplate productApiRestTemplate(
		@Autowired HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory) {
		RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
		return restTemplateBuilder.rootUri(this.PRODUCT_API_URI)
			.requestFactory(() -> new BufferingClientHttpRequestFactory(httpComponentsClientHttpRequestFactory))
			.setConnectTimeout(Duration.ofMinutes(3))
			.build();
	}

}
