package com.example.paypalintegration.services.impl;

import com.example.paypalintegration.services.PaypalOrderOperation;
import com.example.paypalintegration.services.PaypalService;
import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.AmountWithBreakdown;
import com.paypal.orders.ApplicationContext;
import com.paypal.orders.Order;
import com.paypal.orders.OrderRequest;
import com.paypal.orders.OrdersCreateRequest;
import com.paypal.orders.PurchaseUnitRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Service
public class PaypalOperationServiceImpl implements PaypalService
{
    @Resource
    private PayPalHttpClient payPalHttpClient;

    @Override
    public void createOrder(final PaypalOrderOperation operation) throws IOException {
        final OrdersCreateRequest request = new OrdersCreateRequest();
        request.prefer("return=representation");
        request.payPalPartnerAttributionId("");
        request.contentType("application/json");
        request.header("PayPal-Auth-Assertion","");
        request.requestBody(requestBody());

        payPalHttpClient.execute(request);

        /* FIX THIS
        final HttpResponse<Order> response = Credentials.client.execute(request);

        // If call returns body in response, you can get the de-serialized version by
        // calling result() on the response
        order = response.result();
        System.out.println("Order ID: " + order.id());
        order.links().forEach(link -> System.out.println(link.rel() + " => " + link.method() + ":" + link.href()));*/
    }

    private OrderRequest requestBody() {
        final OrderRequest request = new OrderRequest();

        request.checkoutPaymentIntent("CAPTURE");
        request.purchaseUnits(purchaseUnits());
        request.applicationContext(applicationContext());

        return request;
    }

    private ApplicationContext applicationContext() {
        final ApplicationContext context = new ApplicationContext();
        context.brandName("Merchant BRAND");
        context.landingPage("LOGIN");
        context.userAction("PAY_NOW");
        context.returnUrl("");

        return context;
    }

    private List<PurchaseUnitRequest> purchaseUnits() {
        final AmountWithBreakdown amountBreakdown = new AmountWithBreakdown();
        amountBreakdown.currencyCode("USD");
        amountBreakdown.value("100.00");

        final PurchaseUnitRequest purchaseUnit = new PurchaseUnitRequest();
        purchaseUnit.amountWithBreakdown(amountBreakdown);

        return List.of(purchaseUnit);
    }
}
