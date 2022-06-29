package com.belong.phonenumber;

public class PhoneNumberNotFound extends RuntimeException{
    public PhoneNumberNotFound(String message) {
        super(message);
    }
}
