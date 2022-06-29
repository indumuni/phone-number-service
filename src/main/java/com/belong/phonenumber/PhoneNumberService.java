package com.belong.phonenumber;

import com.belong.phonenumber.dto.SearchFilterDTO;
import com.belong.phonenumber.dto.SearchResultsDTO;
import org.springframework.stereotype.Service;

@Service
public class PhoneNumberService {
    private final PhoneNumberRepository phoneNumberRepository;

    public PhoneNumberService(PhoneNumberRepository phoneNumberRepository) {

        this.phoneNumberRepository = phoneNumberRepository;
    }

    public SearchResultsDTO allPhoneNumbers(SearchFilterDTO searchFilter) {

        return new SearchResultsDTO(phoneNumberRepository.findPhoneNumbers(searchFilter));
    }

    public void changeStatus(String phoneNumberId, boolean status) {

        this.phoneNumberRepository.changeStatus(phoneNumberId, status);
    }
}
