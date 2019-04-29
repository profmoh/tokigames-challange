package com.tikigames.challenge;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import com.tikigames.pojos.integration.HerokuappBusinessPojo;
import com.tikigames.pojos.integration.HerokuappCheapPojo;

import reactor.core.publisher.Mono;
/**
 * 
 * @author mohamed
 *
 * Test Integration with herokuapp APIs
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HerokuappIntegrationTest {

	@Value("${integration.herokuapp.base-url}")
	private String baseUrl;

	@Value("${integration.herokuapp.cheap.url}")
	private String cheapUrl;

	@Value("${integration.herokuapp.business.url}")
	private String businessUrl;

	@BeforeClass
	public static void setup() throws Exception {
	}

	/**
	 * test retrieving cheap flights and returning list of HerokuappCheapPojo
	 */
	@Test
	public void testHerokuappCheapIntegration() {

		WebTestClient
			.bindToServer()
			.baseUrl(baseUrl + "/" + cheapUrl)
			.filter(logRequest())
			.build()
			// Create a GET request to test search flights endpoint
			.get()
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectBodyList(HerokuappCheapPojo.class);
	}

	/**
	 * test retrieving business flights and returning list of HerokuappBusinessPojo
	 */
	@Test
	public void testHerokuappBusinessIntegration() {

		WebTestClient
			.bindToServer()
			.baseUrl(baseUrl + "/" + businessUrl)
			.filter(logRequest())
			.build()
			// Create a GET request to test search flights endpoint
			.get()
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectBodyList(HerokuappBusinessPojo.class);
	}

	private static ExchangeFilterFunction logRequest() {
		return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
			System.out.println(clientRequest.method() + "    " + clientRequest.url());

			clientRequest.headers().forEach((name, values) -> values
					.forEach(value -> System.out.println(name + "    " + value)));

			return Mono.just(clientRequest);
		});
	}
}
