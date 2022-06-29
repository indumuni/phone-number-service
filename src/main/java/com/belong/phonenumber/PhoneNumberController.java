package com.belong.phonenumber;

import com.belong.phonenumber.dto.PhoneNumberStatusDTO;
import com.belong.phonenumber.dto.SearchFilterDTO;
import com.belong.phonenumber.dto.SearchResultsDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNumeric;

@RestController
public class PhoneNumberController {

    private final PhoneNumberService phoneNumberService;

    public PhoneNumberController(PhoneNumberService phoneNumberService) {
        this.phoneNumberService = phoneNumberService;
    }

    @GetMapping("/PhoneNumbers")
    public SearchResultsDTO searchPhoneNumbers
            (@RequestParam(required = false) String limit,
             @RequestParam(required = false) String skip,
             @RequestParam(required = false) String customerId) {


        checkArgument(limit == null || isNumeric(limit), "Invalid limit");
        checkArgument(skip == null || isNumeric(skip), "Invalid skip");

        return phoneNumberService.allPhoneNumbers(new SearchFilterDTO(getValidSkip(skip), getValidLimit(limit), customerId));
    }

    @PutMapping( path = "/PhoneNumbers/{id}/status", consumes= MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void changeActiveStatus(@PathVariable String id, @RequestBody PhoneNumberStatusDTO body) {
        phoneNumberService.changeStatus(id , body.active);
    }

    private int getValidLimit(String limit) {
        return limit == null ? 10 : Integer.valueOf(limit);
    }

    private int getValidSkip(String skip) {
        return skip == null ? 0 : Integer.valueOf(skip);
    }
}
