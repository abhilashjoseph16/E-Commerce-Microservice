package com.project.ecommerce.payment;

import java.math.BigDecimal;

import com.project.ecommerce.customer.CustomerResponse;
import com.project.ecommerce.order.PaymentMethod;

public record PaymentRequest(
    BigDecimal amount,
    PaymentMethod paymentMethod,
    Integer orderId,
    String orderReference,
    CustomerResponse customer
) {

}
