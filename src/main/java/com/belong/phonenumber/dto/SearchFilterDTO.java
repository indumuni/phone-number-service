package com.belong.phonenumber.dto;

public class SearchFilterDTO {
    public final int skip;
    public final int limit;
    public final String customerId;

    public SearchFilterDTO(int skip, int limit, String customerId) {
        this.skip = skip;
        this.limit = limit;
        this.customerId = customerId;
    }
}
