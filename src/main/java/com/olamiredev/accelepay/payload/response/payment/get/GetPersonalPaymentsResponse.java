package com.olamiredev.accelepay.payload.response.payment.get;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetPersonalPaymentsResponse implements GetPaymentsResponse {

    private List<PersonalTransactionDTO> personalTransactions;

    private int pageNumber;
    
    private int pageSize;

    private int totalPages;

    private long totalElements;



    
}
