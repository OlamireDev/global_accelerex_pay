package com.olamiredev.accelepay.data.error;

import lombok.Getter;

@Getter
public enum APErrorType {

    ACCOUNT_PROCESSING_ERROR(500),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    NOT_FOUND(404);

    private final int responseCode;
    APErrorType(int responseCode) {
        this.responseCode = responseCode;
    }
}
