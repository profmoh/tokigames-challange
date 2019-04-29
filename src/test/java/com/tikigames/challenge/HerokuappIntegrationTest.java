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

import reactor.core.publisher.Flux;

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

	@Test
	public void testHerokuappCheapIntegration() {

		WebTestClient.bindToServer().baseUrl(baseUrl + "/" + cheapUrl).build()
			// Create a GET request to test search flights endpoint
			.get()
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectBody(Flux.class);
	}

	@Test
	public void testHerokuappBusinessIntegration() {

		WebTestClient.bindToServer().baseUrl(baseUrl + "/" + businessUrl).build()
			// Create a GET request to test search flights endpoint
			.get()
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectBody(Flux.class);
	}
}
