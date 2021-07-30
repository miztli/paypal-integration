package com.example.paypalintegration.services.impl;

import com.example.paypalintegration.config.PaypalConfig;
import com.example.paypalintegration.services.PaypalOrderOperation;
import com.example.paypalintegration.services.PaypalService;
import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.AmountWithBreakdown;
import com.paypal.orders.ApplicationContext;
import com.paypal.orders.Order;
import com.paypal.orders.OrderRequest;
import com.paypal.orders.OrdersAuthorizeRequest;
import com.paypal.orders.OrdersCaptureRequest;
import com.paypal.orders.OrdersCreateRequest;
import com.paypal.orders.PurchaseUnitRequest;
import com.paypal.payments.AuthorizationsCaptureRequest;
import com.paypal.payments.Capture;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Service
public class PaypalOperationServiceImpl implements PaypalService
{
    @Resource
    private PayPalHttpClient payPalHttpClient;

    @Resource
    private PaypalConfig.PaypalApplication paypalApplication;

    @Override
    public void sale() throws IOException {

    }

    @Override
    public HttpResponse<Order> createOrderWithAuthorizeIntent() throws IOException {
        final OrdersCreateRequest request = createRequestOrder();
        request.requestBody(requestBody(PaypalOrderOperation.AUTHORIZE));

        return payPalHttpClient.execute(request);
    }

    @Override
    public HttpResponse<Order> authorizeOrderCreatedWithAuthorizeIntent(final String orderId) throws IOException {
       final OrdersAuthorizeRequest request = createAuthorizeRequest(orderId);

        return payPalHttpClient.execute(request);
    }

    @Override
    public HttpResponse<Capture> capturePaymentAuthorization(final String authorizationId) throws IOException {
        final AuthorizationsCaptureRequest request = createAuthorizationsCaptureRequest(authorizationId);

        return payPalHttpClient.execute(request);
    }

    private AuthorizationsCaptureRequest createAuthorizationsCaptureRequest(final String authorizationId) {
        final AuthorizationsCaptureRequest request = new AuthorizationsCaptureRequest(authorizationId);
        request.prefer("return=representation");
        // ? request.payPalPartnerAttributionId(paypalApplication.getBnCode());
        request.header("PayPal-Auth-Assertion", paypalApplication.getAuthAssertion());
        request.header("PayPal-Partner-Attribution-Id", paypalApplication.getBnCode()); // is this needed for this call?
        return request;
    }

    private OrdersAuthorizeRequest createAuthorizeRequest(final String orderId) {
        final OrdersAuthorizeRequest request = new OrdersAuthorizeRequest(orderId);
        request.prefer("return=representation");
        // ? request.payPalPartnerAttributionId(paypalApplication.getBnCode());
        request.contentType("application/json");
        request.header("PayPal-Auth-Assertion", paypalApplication.getAuthAssertion());
        request.header("PayPal-Partner-Attribution-Id", paypalApplication.getBnCode()); // is this needed for this call?
        return request;
    }

    private OrdersCreateRequest createRequestOrder() {
        final OrdersCreateRequest request = new OrdersCreateRequest();
        request.prefer("return=representation");
        request.payPalPartnerAttributionId(paypalApplication.getBnCode());
        request.contentType("application/json");
        request.header("PayPal-Auth-Assertion",paypalApplication.getAuthAssertion());
        return request;
    }

    @Override
    public HttpResponse<Order> capture(final String orderId) throws IOException {
        final OrdersCaptureRequest request = new OrdersCaptureRequest(orderId);

        return payPalHttpClient.execute(request);
    }

    private OrderRequest requestBody(final PaypalOrderOperation operation) {
        final OrderRequest request = new OrderRequest();

        request.checkoutPaymentIntent(operation.getOperation());
        request.purchaseUnits(purchaseUnits());
        request.applicationContext(applicationContext());

        return request;
    }

    private ApplicationContext applicationContext() {
        final ApplicationContext context = new ApplicationContext();
        context.brandName("Merchant BRAND");
        context.landingPage("LOGIN");
        context.userAction("PAY_NOW");
        context.returnUrl(paypalApplication.getRedirectionUrl());

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
