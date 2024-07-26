package com.project.ecommerce.payment;

import org.springframework.stereotype.Service;

import com.project.ecommerce.notification.NotificationProducer;
import com.project.ecommerce.notification.PaymentNotificationRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository repostiory;
    private final PaymentMapper mapper;
    private final NotificationProducer notificationProducer;

    public Integer createPayment(PaymentRequest request) {
        var payment = repostiory.save(mapper.toPayment(request));
        notificationProducer.sendNotification(
            new PaymentNotificationRequest(
                request.orderReference(), 
                request.amount(), 
                request.paymentMethod(), 
                request.customer().firstname(), 
                request.customer().lastname(), 
                request.customer().email())
        );
        return payment.getId();
}

}
