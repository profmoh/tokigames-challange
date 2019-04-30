package com.tikigames.challenge;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.tikigames.TokiApplication;
import com.tikigames.pojos.AvailableFlightsPojo;

import reactor.core.publisher.Flux;

/**
 * 
 * @author mohamed
 *
 * Test project Controller, flightsSearch end point
 * 
 * also test sorting, filtering and paginating results
 *
 */
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK, classes = TokiApplication.class)
public class FlightsSearchTest {

	@Autowired
	private MockMvc mockMvc;

	@BeforeClass
	public static void setup() throws Exception {

	}

	/**
	 * test basic flights search with out filtering or sorting
	 */
	@Test
	public void testFlightsSearch() {
		try {
			MvcResult mvcResult = mockMvc
					.perform(get("/tokigames/api/flights/search").accept(MediaType.APPLICATION_JSON))
					.andExpect(request().asyncStarted())
					.andDo(print())
					.andDo(MockMvcResultHandlers.log())
					.andReturn();

			mockMvc.perform(asyncDispatch(mvcResult))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$").exists())
					.andExpect(jsonPath("$[0]", isA(AvailableFlightsPojo.class)));
		} catch (Exception e) {
			e.printStackTrace();
			assertFalse(true);
		}
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
