package com.project.ecommerce.notification;

import java.math.BigDecimal;

import com.project.ecommerce.payment.PaymentMethod;

public record PaymentNotificationRequest(
    String orderReference,
    BigDecimal amount,
    PaymentMethod paymentMethod,
    String customerFirstName,
    String customerLastName,
    String customerEmail
) {

}
