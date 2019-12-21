package com.tritrantam.ppmtool.exceptions;

public class SameEmailRegisterExceptionResponse {

    private String sameEmail;

    public SameEmailRegisterExceptionResponse(String sameEmail) {
        this.sameEmail = sameEmail;
    }

    public String getSameEmail() {
        return sameEmail;
    }

    public void setSameEmail(String sameEmail) {
        this.sameEmail = sameEmail;
    }
}
