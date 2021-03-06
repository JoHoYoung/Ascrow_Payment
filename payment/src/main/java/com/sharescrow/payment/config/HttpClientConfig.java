package com.sharescrow.payment.config;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

@Configuration
public class HttpClientConfig {

	@Bean
	public HttpComponentsClientHttpRequestFactory gethttpComponentsClientHttpRequestFactory() {
		HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		httpRequestFactory.setConnectTimeout(2000);
		httpRequestFactory.setReadTimeout(3000);
		HttpClient httpClient = HttpClientBuilder.create()
			.setMaxConnTotal(250)
			.setMaxConnPerRoute(50)
			.build();
		httpRequestFactory.setHttpClient(httpClient);
		return httpRequestFactory;
	}

}
