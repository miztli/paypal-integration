package com.example.paypalintegration.services;

import com.paypal.http.HttpResponse;
import com.paypal.orders.Order;
import com.paypal.payments.Capture;

import java.io.IOException;

public interface PaypalService
{
    void sale() throws IOException;

    HttpResponse<Order> createOrderWithAuthorizeIntent() throws IOException;

    HttpResponse<Order> authorizeOrderCreatedWithAuthorizeIntent(String orderId) throws IOException;

    HttpResponse<Capture> capturePaymentAuthorization(String authorizationId) throws IOException;

    HttpResponse<Order> capture(String orderId) throws IOException;
}
