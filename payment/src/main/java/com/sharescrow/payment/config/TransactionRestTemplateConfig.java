package com.sharescrow.payment.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.*;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Configuration
public class TransactionRestTemplateConfig {

	private final Log logger = LogFactory.getLog(this.getClass());

	final private String KAKAO_PAY_API_URI = "https://kapi.kakao.com/v1/payment";
	final private String NAVER_PAY_API_URI = "https://dev.apis.naver.com/naverpay-partner";
	final private String OPEN_BANK_WITHDRAW_ACCOUNT_URI = "https://testapi.open-platform.or.kr";

	@Value("${kakao.key}")
	private String KAKAO_KEY;
	@Value("${naver.key}")
	private String NAVER_KEY;
	@Value("${naver.secret.key}")
	private String NAVER_SECRET_KEY;
	@Value("${openbank.auth.key}")
	private String OPEN_BANK_KEY;

	public class RestTemplateClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

		@Override
		public ClientHttpResponse intercept(HttpRequest request,
			byte[] body, ClientHttpRequestExecution execution) throws IOException {
			URI uri = request.getURI();
			traceRequest(request, body);
			ClientHttpResponse response = execution.execute(request, body);
			traceResponse(response, uri);
			return response;
		}

		private void traceRequest(HttpRequest request, byte[] body) {
			StringBuilder reqLog = new StringBuilder();
			reqLog.append("[REQUEST] ")
				.append("Uri : ")
				.append(request.getURI())
				.append(", Method : ")
				.append(request.getMethod())
				.append(", Request Body : ")
				.append(new String(body, StandardCharsets.UTF_8));
			logger.info(reqLog.toString());
		}

		private void traceResponse(ClientHttpResponse response, URI uri) throws IOException {
			StringBuilder resLog = new StringBuilder();
			resLog.append("[RESPONSE] ")
				.append("Uri : ")
				.append(uri)
				.append(", Status code : ")
				.append(response.getStatusCode())
				.append(", Response Body : ")
				.append(StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8));
			logger.info(resLog.toString());
		}
	}

	@Bean
	public RestTemplate kakaoPayAPIRestTemplate(
		@Autowired HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory) {
		RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
		return restTemplateBuilder.rootUri(this.KAKAO_PAY_API_URI)
			.additionalInterceptors(new RestTemplateClientHttpRequestInterceptor())
			.requestFactory(() -> new BufferingClientHttpRequestFactory(httpComponentsClientHttpRequestFactory))
			.defaultHeader("Authorization", "KakaoAK ".concat(this.KAKAO_KEY))
			.setConnectTimeout(Duration.ofMinutes(3))
			.build();
	}

	@Bean
	public RestTemplate naverPayAPIRestTemplate(
		@Autowired HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory) {
		RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
		return restTemplateBuilder.rootUri(this.NAVER_PAY_API_URI)
			.additionalInterceptors(new RestTemplateClientHttpRequestInterceptor())
			.requestFactory(() -> new BufferingClientHttpRequestFactory(httpComponentsClientHttpRequestFactory))
			.defaultHeader("X-Naver-Client-Id", this.NAVER_KEY)
			.defaultHeader("X-Naver-Client-Secret", this.NAVER_SECRET_KEY)
			.setConnectTimeout(Duration.ofMinutes(3))
			.build();
	}

	@Bean
	public RestTemplate openBankAPIRestTemplate(
		@Autowired HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory) {
		RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
		return restTemplateBuilder.rootUri(this.OPEN_BANK_WITHDRAW_ACCOUNT_URI)
			.additionalInterceptors(new RestTemplateClientHttpRequestInterceptor())
			.defaultHeader("Authorization", "Bearer ".concat(this.OPEN_BANK_KEY))
			.requestFactory(() -> new BufferingClientHttpRequestFactory(httpComponentsClientHttpRequestFactory))
			.setConnectTimeout(Duration.ofMinutes(3))
			.build();
	}

}