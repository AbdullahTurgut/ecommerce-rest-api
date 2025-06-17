package com.dailyalcorcode.buynowdotcom.request;

import lombok.Data;

@Data
public class PaymentRequest {

    private int amount;
    private String currency;
}
