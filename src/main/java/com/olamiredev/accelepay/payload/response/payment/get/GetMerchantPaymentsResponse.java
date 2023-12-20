package com.olamiredev.accelepay.payload.response.payment.get;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetMerchantPaymentsResponse implements GetPaymentsResponse{

    private List<MerchantTransactionDTO> merchantTransactions;

    private int pageNumber;

    private int pageSize;

    private int totalPages;
    
    private long totalElements;
}
