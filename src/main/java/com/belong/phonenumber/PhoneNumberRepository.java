package com.belong.phonenumber;

import com.belong.phonenumber.dto.PhoneNumberDTO;
import com.belong.phonenumber.dto.SearchFilterDTO;

import java.util.List;

public interface PhoneNumberRepository {
    List<PhoneNumberDTO> findPhoneNumbers(SearchFilterDTO filter);

    void changeStatus(String phoneNumberId, boolean status);
}
