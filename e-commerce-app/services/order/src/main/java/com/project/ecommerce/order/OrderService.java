package com.project.ecommerce.order;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.project.ecommerce.customer.CustomerClient;
import com.project.ecommerce.exception.BusinessException;
import com.project.ecommerce.kafka.OrderConfirmation;
import com.project.ecommerce.kafka.OrderProducer;
import com.project.ecommerce.orderline.OrderLineRequest;
import com.project.ecommerce.orderline.OrderLineService;
import com.project.ecommerce.payment.PaymentClient;
import com.project.ecommerce.payment.PaymentRequest;
import com.project.ecommerce.product.ProductClient;
import com.project.ecommerce.product.PurchaseRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderMapper mapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;
    private final PaymentClient paymentClient;

    public Integer createdOrder(OrderRequest request) {
        var customer = this.customerClient.findCustomerById(request.customerId())
                .orElseThrow(
                        () -> new BusinessException("Cannot create Order:: No customer exists with the provided ID"));

        var purchasedProducts = this.productClient.purchaseProducts(request.products());

        var order = this.repository.save(mapper.toOrder(request));

        for (PurchaseRequest purchaseRequest : request.products()) {
            orderLineService.saveOrderLine(
                    new OrderLineRequest(null, order.getId(), purchaseRequest.productId(), purchaseRequest.quantity()));
        }

        var paymentRequest = new PaymentRequest(
                request.amount(),
                request.paymentMethod(),
                order.getId(),
                order.getReference(),
                customer);

        paymentClient.requestOrderPayment(paymentRequest);

        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        request.reference(),
                        request.amount(),
                        request.paymentMethod(),
                        customer,
                        purchasedProducts));

        return order.getId();
    }

    public List<OrderResponse> findAll() {
        return repository.findAll().stream().map(mapper::fromOrder).collect(Collectors.toList());
    }

    public OrderResponse findById(Integer orderId) {
        return repository.findById(orderId)
                .map(mapper::fromOrder)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("No Order Found with the provided ID: %d", orderId)));
    }

}
