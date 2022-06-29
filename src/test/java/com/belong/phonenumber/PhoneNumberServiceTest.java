package com.belong.phonenumber;


import com.belong.phonenumber.dto.PhoneNumberDTO;
import com.belong.phonenumber.dto.SearchFilterDTO;
import com.belong.phonenumber.dto.SearchResultsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PhoneNumberServiceTest {

    @MockBean
    private PhoneNumberRepository mockPhoneNumberRepository;

    @Captor
    ArgumentCaptor<SearchFilterDTO> captureSearchFilter;

    @Captor
    ArgumentCaptor<String> capturePhoneNumber;

    @Captor
    ArgumentCaptor<Boolean> capturePhoneNumberStatus;

    private PhoneNumberService phoneNumberService;

    @BeforeEach
    public void givenPhoneNumberService() {
        phoneNumberService = new PhoneNumberService(mockPhoneNumberRepository);
    }

    @Test
    public void shouldPassFilterValuesToRepository() {

        phoneNumberService.allPhoneNumbers(new SearchFilterDTO(100, 50 , "c122"));

        verify(mockPhoneNumberRepository).findPhoneNumbers(captureSearchFilter.capture());
        assertThat(captureSearchFilter.getValue().skip, equalTo(100));
        assertThat(captureSearchFilter.getValue().limit, equalTo(50));
        assertThat(captureSearchFilter.getValue().customerId, equalTo("c122"));
    }

    @Test
    public void shouldCreateSearchResultFromPhoneNumbers() {

        when(mockPhoneNumberRepository.findPhoneNumbers(any())).thenReturn(List.of(new PhoneNumberDTO("eee", "04", false, "c1112")));

        SearchResultsDTO searchResults = phoneNumberService.allPhoneNumbers(any());
        assertThat(searchResults.results.size(), equalTo(1));
        assertThat(searchResults.results.get(0).customerId, equalTo("c1112"));
    }


    @Test
    public void shouldCallRepositoryToUpdateStatusFlag() {

        phoneNumberService.changeStatus("some-phone-number", false);

        verify(mockPhoneNumberRepository).changeStatus(capturePhoneNumber.capture(), capturePhoneNumberStatus.capture());
        assertThat(capturePhoneNumber.getValue(), equalTo("some-phone-number"));
        assertThat(capturePhoneNumberStatus.getValue(), equalTo(false));

    }
}