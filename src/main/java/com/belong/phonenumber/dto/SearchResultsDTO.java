package com.belong.phonenumber.dto;

import com.belong.phonenumber.dto.PhoneNumberDTO;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsDTO {
    public final List<PhoneNumberDTO> results;

    public SearchResultsDTO(){
        results = new ArrayList<>();
    }

    public SearchResultsDTO(List<PhoneNumberDTO> results) {
        this.results = results;
    }
}
