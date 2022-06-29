package com.belong.phonenumber;

import com.belong.phonenumber.dto.PhoneNumberDTO;
import com.belong.phonenumber.dto.SearchFilterDTO;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FlatFilePhoneNumberRepositoryTest {

    private PhoneNumberRepository phoneNumberRepository;


    @BeforeEach
    public void givenPhoneNumberRepositoryInitiateWithStaticDataSet() {
        FlatFilePhoneNumberRepository repository = new FlatFilePhoneNumberRepository();
        assertThat(repository.phoneNumbers().size(), CoreMatchers.equalTo(11));
        this.phoneNumberRepository = repository;
    }

    @AfterEach
    public void clearRepository() {
        this.phoneNumberRepository = null;
    }

    @Test
    public void shouldRequireValidSearchFilter() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            phoneNumberRepository.findPhoneNumbers(null);
        });
        assertThat(exception.getMessage(), is("Invalid search filter"));
        assertThat(exception.getClass(), is(IllegalArgumentException.class));
    }

    @Test
    public void shouldAbleToGetFirstNResults() {
        List<PhoneNumberDTO> results = phoneNumberRepository.findPhoneNumbers(new SearchFilterDTO(0, 5, null));

        assertThat(results.size(), is(5));
        assertThat(results.stream().map(p-> p.id).collect(Collectors.toList()),
                contains(
                        "0a523c9b-89bd-4d40-b325-b2ab3da13264",
                        "0a84a0e0-5c82-4265-9007-f4edce2a2a10",
                        "26abe914-628b-4744-ad7c-430503c1ed2c",
                        "3118bb5d-a638-4405-b152-b32c07e85c13",
                        "3541bedb-baa6-46e0-88bf-d7efe4faf56d") );
    }

    @Test
    public void shouldAbleToGetNextNResults() {
        List<PhoneNumberDTO> results = phoneNumberRepository.findPhoneNumbers(new SearchFilterDTO(2, 2, null));

        assertThat(results.size(), is(2));
        assertThat(results.stream().map(p-> p.id).collect(Collectors.toList()),
                contains(
                        "26abe914-628b-4744-ad7c-430503c1ed2c",
                        "3118bb5d-a638-4405-b152-b32c07e85c13"));
    }

    @Test
    public void shouldAbleToFilterByCustomer() {
        List<PhoneNumberDTO> results = phoneNumberRepository
                .findPhoneNumbers(new SearchFilterDTO(0, 3, "da04db53-e4fb-4c56-aa23-e03ddd7820dc"));

        assertThat(results.size(), is(3));
        assertThat(results.stream().map(p-> p.id).collect(Collectors.toList()),
                contains(
                        "0a523c9b-89bd-4d40-b325-b2ab3da13264",
                        "26abe914-628b-4744-ad7c-430503c1ed2c",
                        "590f3398-b52b-4a39-aaa4-a80af81ad0ee"));
    }

    @Test
    public void shouldReturnAvailableResultsWhenRequestedMoreThanWhatIsThere() {
        List<PhoneNumberDTO> results = phoneNumberRepository
                .findPhoneNumbers(new SearchFilterDTO(0, 10, "da04db53-e4fb-4c56-aa23-e03ddd7820dc"));

        assertThat(results.size(), is(3));
        assertThat(results.stream().map(p-> p.id).collect(Collectors.toList()),
                contains(
                        "0a523c9b-89bd-4d40-b325-b2ab3da13264",
                        "26abe914-628b-4744-ad7c-430503c1ed2c",
                        "590f3398-b52b-4a39-aaa4-a80af81ad0ee"));
    }

    @Test
    public void shouldReturnAvailableResultsAfterSkipWhenRequestedMoreThanWhatIsThere() {
        List<PhoneNumberDTO> results = phoneNumberRepository
                .findPhoneNumbers(new SearchFilterDTO(2, 10, "da04db53-e4fb-4c56-aa23-e03ddd7820dc"));

        assertThat(results.size(), is(1));
        assertThat(results.stream().map(p-> p.id).collect(Collectors.toList()),
                contains("590f3398-b52b-4a39-aaa4-a80af81ad0ee"));
    }

    @Test
    public void shouldReturnEmptyListWhenSkipGreaterThanExistingResults() {
        List<PhoneNumberDTO> results = phoneNumberRepository
                .findPhoneNumbers(new SearchFilterDTO(10, 1, "da04db53-e4fb-4c56-aa23-e03ddd7820dc"));

        assertThat(results.size(), is(0));
        assertThat(results.stream().map(p-> p.id).collect(Collectors.toList()),
                empty());
    }

    @Test
    public void shouldStatusUpdateOnInvalidPhoneNumberShouldFail() {
        Exception exception = assertThrows(PhoneNumberNotFound.class, () -> {
            phoneNumberRepository.changeStatus("id-that-does-not-exist", true);

        });
        assertThat(exception.getMessage(), is("Invalid phone number"));
        assertThat(exception.getClass(), is(PhoneNumberNotFound.class));
    }

    @Test
    public void shouldAbleToChangeStatusOnValidPhoneNumbers() {
        List<PhoneNumberDTO> results = phoneNumberRepository
                .findPhoneNumbers(new SearchFilterDTO(0, 1, "da04db53-e4fb-4c56-aa23-e03ddd7820dc"));
        PhoneNumberDTO oldPhoneNumber = results.get(0);

        phoneNumberRepository.changeStatus(oldPhoneNumber.id, !oldPhoneNumber.active);

        results = phoneNumberRepository
                .findPhoneNumbers(new SearchFilterDTO(0, 1, "da04db53-e4fb-4c56-aa23-e03ddd7820dc"));

        PhoneNumberDTO UpdatePhoneNumber = results.get(0);
        assertThat(oldPhoneNumber.id, equalTo(UpdatePhoneNumber.id));
        assertThat(oldPhoneNumber.phoneNumber, equalTo(UpdatePhoneNumber.phoneNumber));
        assertThat(oldPhoneNumber.customerId, equalTo(UpdatePhoneNumber.customerId));
        assertThat(oldPhoneNumber.active, not(equalTo(UpdatePhoneNumber.active)));
    }
}