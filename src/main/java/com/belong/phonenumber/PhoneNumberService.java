package com.belong.phonenumber;

import com.belong.phonenumber.dto.SearchFilterDTO;
import com.belong.phonenumber.dto.SearchResultsDTO;
import com.google.common.base.Preconditions;
import org.springframework.stereotype.Service;

import static com.google.common.base.Preconditions.checkArgument;

@Service
public class PhoneNumberService {
    private final PhoneNumberRepository phoneNumberRepository;

    public PhoneNumberService(PhoneNumberRepository phoneNumberRepository) {

        this.phoneNumberRepository = phoneNumberRepository;
    }

    public SearchResultsDTO allPhoneNumbers(SearchFilterDTO searchFilter) {
        checkArgument(searchFilter.skip >= 0, "Invalid skip");
        checkArgument(searchFilter.limit >= 0 && searchFilter.limit <= 50, "Invalid limit");

        return new SearchResultsDTO(phoneNumberRepository.findPhoneNumbers(searchFilter));
    }

    public void changeStatus(String phoneNumberId, boolean status) {

        this.phoneNumberRepository.changeStatus(phoneNumberId, status);
    }
}
