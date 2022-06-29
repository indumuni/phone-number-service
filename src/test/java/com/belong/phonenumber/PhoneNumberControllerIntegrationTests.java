package com.belong.phonenumber;

import com.belong.phonenumber.dto.PhoneNumberDTO;
import com.belong.phonenumber.dto.PhoneNumberStatusDTO;
import com.belong.phonenumber.dto.SearchResultsDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PhoneNumberControllerIntegrationTests {

	@Autowired
	private TestRestTemplate template;

	@Test
	void shouldRetrievePhoneNumbersWithDefaultSkipAndLimit() {
		ResponseEntity<SearchResultsDTO> response = template.getForEntity("/PhoneNumbers", SearchResultsDTO.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
		assertThat(response.getBody().results.size(), equalTo(10));
	}

	@Test
	void shouldFilterByCustomer() {
		String customerId = "da04db53-e4fb-4c56-aa23-e03ddd7820dc";
		String url = "/PhoneNumbers?limit=1&skip=0&customerId=" + customerId;
		ResponseEntity<SearchResultsDTO> response = template.getForEntity(url, SearchResultsDTO.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
		assertThat(response.getBody().results.size(), equalTo(1));
		assertThat(response.getBody().results.get(0).customerId, equalTo(customerId));
	}

    @Test
	public void shouldAbleToChangeTheActiveStatusForGivenPhoneNumber() {
		ResponseEntity<SearchResultsDTO> response = template.getForEntity("/PhoneNumbers", SearchResultsDTO.class);
		PhoneNumberDTO before = response.getBody().results.get(0);

		PhoneNumberStatusDTO body = new PhoneNumberStatusDTO();
		body.active = false;

		template.put("/PhoneNumbers/"+before.id+"/status", body, Void.class);

		response = template.getForEntity("/PhoneNumbers", SearchResultsDTO.class);
		PhoneNumberDTO after = response.getBody().results.get(0);

		assertThat(before.id, equalTo(after.id));
		assertThat(before.active, not(equalTo(after.active)));
	}

}
