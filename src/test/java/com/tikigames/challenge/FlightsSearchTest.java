package com.tikigames.challenge;

import org.assertj.core.api.Assertions;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import com.datazord.pojos.ResponseDetails;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class FlightsSearchTest {

	@Autowired
	private WebTestClient webClient;

	@BeforeClass
	public static void setup() throws Exception {

	}

	@Test
	public void testFlightsSearch() {

		ResponseSpec exchange = webClient
			// Create a GET request to test search flights endpoint
			.get()
			.uri("/tokigames/api/search")
			.accept(MediaType.APPLICATION_JSON)
			.exchange();

		exchange
			.expectStatus().isOk()
			.expectBody(ResponseDetails.class)
			.consumeWith(response ->
					Assertions.assertThat(response.getResponseBody()).isNotNull());

		exchange
			.expectBody()
			.jsonPath("$.success").isEqualTo(Boolean.TRUE)
			.jsonPath("$.body").isNotEmpty();

//		ResponseDetails<Flux<AvailableFlightsPojo>> response =
//				(ResponseDetails<Flux<AvailableFlightsPojo>>) exchange.expectBody(ResponseDetails.class).returnResult().getResponseBody().getBody();
	}

	@Test
	public void testFlightsSearchSort() {

	}

	@Test
	public void testFlightsSearchFilter() {

	}

	@Test
	public void testFlightsSearchPageable() {

	}
}
