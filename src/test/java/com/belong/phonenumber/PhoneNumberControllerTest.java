package com.belong.phonenumber;

import com.belong.phonenumber.dto.PhoneNumberDTO;
import com.belong.phonenumber.dto.SearchFilterDTO;
import com.belong.phonenumber.dto.SearchResultsDTO;
import com.belong.phonenumber.exception.PhoneNumberNotFound;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PhoneNumberControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PhoneNumberService mockPhoneNumberService;

    @Captor
    ArgumentCaptor<SearchFilterDTO> captureSearchFilter;

    @Captor
    ArgumentCaptor<String> capturePhoneNumber;

    @Captor
    ArgumentCaptor<Boolean> capturePhoneNumberStatus;

    @Test
    public void shouldReturnResultPhoneNumbersGivenNoFilter() throws Exception {

        when(mockPhoneNumberService.allPhoneNumbers(any())).thenReturn(mockResult());

        mvc.perform(get("/PhoneNumbers").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results[*].id").value(containsInAnyOrder("p14423", "p24423")))
                .andExpect(jsonPath("$.results.length()").value(2));
    }

    @Test
    public void shouldUseDefaultValuesGivenNoPassedInFilterParameters() throws Exception {
        mvc.perform(get("/PhoneNumbers")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(mockPhoneNumberService).allPhoneNumbers(captureSearchFilter.capture());
        assertThat(captureSearchFilter.getValue().limit, equalTo(10));
        assertThat(captureSearchFilter.getValue().skip, equalTo(0));
        assertThat(captureSearchFilter.getValue().customerId, nullValue());
    }

    @Test
    public void shouldUsePassedInValuesGivenValidFilterParameters() throws Exception {
        mvc.perform(get("/PhoneNumbers").accept(MediaType.APPLICATION_JSON)
                        .queryParam("limit", "3")
                        .queryParam("skip", "100")
                        .queryParam("customerId", "c122")
                )
                .andExpect(status().isOk());

        verify(mockPhoneNumberService).allPhoneNumbers(captureSearchFilter.capture());
        assertThat(captureSearchFilter.getValue().limit, equalTo(3));
        assertThat(captureSearchFilter.getValue().skip, equalTo(100));
        assertThat(captureSearchFilter.getValue().customerId, equalTo("c122"));
    }

    @Test
    public void shouldReturnBadRequestGivenInvalidSkip() throws Exception {

        mvc.perform(get("/PhoneNumbers").accept(MediaType.APPLICATION_JSON)
                        .queryParam("skip", "100a")
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string(equalTo("Invalid skip")));
    }

    @Test
    public void shouldReturnBadRequestGivenInvalidLimit() throws Exception {

        mvc.perform(get("/PhoneNumbers").accept(MediaType.APPLICATION_JSON)
                        .queryParam("limit", "3a")
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string(equalTo("Invalid limit")));
    }

    @Test
    public void shouldReturnNoContentStatusWhenPatchingActiveFlag() throws Exception {

        mvc.perform(put("/PhoneNumbers/1111/status").contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"active\":true }")
                )
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldRequestServiceToChangeTheStatusOfThePhoneNumber() throws Exception {

        mvc.perform(put("/PhoneNumbers/1111/status").contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"active\":true }")
                )
                .andExpect(status().isNoContent());

        verify(mockPhoneNumberService).changeStatus(capturePhoneNumber.capture(), capturePhoneNumberStatus.capture());
        assertThat(capturePhoneNumber.getValue(), equalTo("1111"));
        assertThat(capturePhoneNumberStatus.getValue(), equalTo(true));
    }

    @Test
    public void shouldReturnBadRequestGivenInvalidPhoneNumber() throws Exception {

        doThrow(new PhoneNumberNotFound("Invalid phone number")).when(mockPhoneNumberService).changeStatus(anyString(), anyBoolean());

        mvc.perform(put("/PhoneNumbers/1111AAA/status").contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"active\":true }")
                )
                .andExpect(status().isNotFound())
                .andExpect(content().string(equalTo("Invalid phone number")));
    }

    private SearchResultsDTO mockResult() {
        return new SearchResultsDTO(List.of(
                new PhoneNumberDTO("p14423", "045556666", true, "c111"),
                new PhoneNumberDTO("p24423", "04555666t", true, "c111")
        ));
    }
}