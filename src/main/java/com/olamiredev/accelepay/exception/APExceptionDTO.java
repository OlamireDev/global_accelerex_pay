package com.olamiredev.accelepay.exception;

import com.olamiredev.accelepay.data.error.APErrorType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class APExceptionDTO {

    private APErrorType errorType;

    private String message;

}
