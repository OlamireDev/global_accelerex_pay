package com.olamiredev.accelepay.enums;

import com.olamiredev.accelepay.data.error.APErrorType;
import com.olamiredev.accelepay.exception.APException;

public enum SupportedPlatform {

    ANDROID,
    BROWSER,
    IOS;

    public static SupportedPlatform fromString(String platform) throws APException {
        for (SupportedPlatform supportedPlatform : SupportedPlatform.values()) {
            if (supportedPlatform.name().equalsIgnoreCase(platform)) {
                return supportedPlatform;
            }
        }
        throw new APException(APErrorType.BAD_REQUEST, "Invalid request platform string provide", SupportedPlatform.class.getName());
    }
}
