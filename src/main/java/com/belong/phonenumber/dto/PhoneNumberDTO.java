package com.belong.phonenumber.dto;

public class PhoneNumberDTO {
    public String id;
    public String phoneNumber;
    public Boolean active;
    public String customerId;

    public String id() {
        return this.id;
    }

    public PhoneNumberDTO(String id, String phoneNumber, boolean active, String customerId) {

        this.id = id;
        this.phoneNumber = phoneNumber;
        this.active = active;
        this.customerId = customerId;
    }
}
