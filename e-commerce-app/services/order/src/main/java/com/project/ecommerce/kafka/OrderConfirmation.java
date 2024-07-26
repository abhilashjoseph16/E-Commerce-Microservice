package com.project.ecommerce.kafka;

import java.math.BigDecimal;
import java.util.List;

import com.project.ecommerce.customer.CustomerResponse;
import com.project.ecommerce.order.PaymentMethod;
import com.project.ecommerce.product.PurchaseResponse;

public record OrderConfirmation(
    String orderReference,
    BigDecimal totalAmount,
    PaymentMethod paymentMethod,
    CustomerResponse customer,
    List<PurchaseResponse> products
) {

}
