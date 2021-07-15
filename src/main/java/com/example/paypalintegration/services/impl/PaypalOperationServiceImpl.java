package com.example.paypalintegration.services.impl;

import javax.annotation.Resource;

import com.example.paypalintegration.services.PaypalOrderOperation;
import com.example.paypalintegration.services.PaypalService;
import com.paypal.core.PayPalHttpClient;
import com.paypal.orders.OrdersCreateRequest;
import org.springframework.stereotype.Service;

@Service
public class PaypalOperationServiceImpl implements PaypalService
{
    @Resource
    private PayPalHttpClient payPalHttpClient;

    @Override
    public void createOrder(final PaypalOrderOperation operation)
    {
        final OrdersCreateRequest request = new OrdersCreateRequest();
        request.header("prefer","return=representation");
        request.header("PayPal-Partner-Attribution-Id","FLAVORsb-lith36473523_MP");
    }
}
