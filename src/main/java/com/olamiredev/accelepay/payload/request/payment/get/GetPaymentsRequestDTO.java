package com.olamiredev.accelepay.payload.request.payment.get;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class GetPaymentsRequestDTO {

    private String apiKey;

    private int pageNumber;

    private int pageSize;

}
