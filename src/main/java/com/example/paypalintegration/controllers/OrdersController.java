package com.example.paypalintegration.controllers;

import com.example.paypalintegration.services.PaypalService;
import com.paypal.http.HttpResponse;
import com.paypal.orders.LinkDescription;
import com.paypal.orders.Order;
import com.paypal.payments.Capture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/paypal/orders")
public class OrdersController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(OrdersController.class);

    @Resource
    private PaypalService paypalService;

    @PostMapping()
    public ResponseEntity<Map<String, Object>> createOrderWithAuthorizeIntent() throws IOException {
        final HttpResponse<Order> orderHttpResponse = paypalService.createOrderWithAuthorizeIntent();
        logOrder(orderHttpResponse);
        final Map<String, Object> orderValues = toMap(orderHttpResponse);
        return ResponseEntity.status(orderHttpResponse.statusCode()).body(orderValues);
    }

    @PostMapping(value = "/{orderId}/authorize")
    public ResponseEntity<Map<String, Object>> authorizeOrderCreatedWithAuthorizeIntent(@PathVariable(value = "orderId") final String orderId) throws IOException {
        final HttpResponse<Order> orderHttpResponse = paypalService.authorizeOrderCreatedWithAuthorizeIntent(orderId);
        logOrder(orderHttpResponse);
        final Map<String, Object> orderValues = toMap(orderHttpResponse);
        return ResponseEntity.status(orderHttpResponse.statusCode()).body(orderValues);
    }

    @PostMapping(value = "{orderId}/capture", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> capture(@PathVariable(value = "orderId") final String orderId) throws IOException {
        final HttpResponse<Order> orderHttpResponse = paypalService.capture(orderId);
        logOrder(orderHttpResponse);
        final Map<String, Object> orderValues = toMap(orderHttpResponse);
        return ResponseEntity.status(orderHttpResponse.statusCode()).body(orderValues);
    }

    private void logOrder(final HttpResponse<Order> orderHttpResponse) {
        final Order order = orderHttpResponse.result();
        LOGGER.info("------------------------------------------------------");
        LOGGER.info("HTTP status -> {}", orderHttpResponse.statusCode());
        LOGGER.info("Order id -> {}", order.id());
        LOGGER.info("Order intent -> {}", order.checkoutPaymentIntent());
        LOGGER.info("Order time -> {}", order.createTime());
        order.links().stream().forEach(link -> LOGGER.info("href [{}] -> method [{}] -> rel [{}]", link.href(), link.method(), link.rel()));
    }


    private Map<String, Object> toMap(final HttpResponse<Order> orderHttpResponse) {
        final Order order = orderHttpResponse.result();
        final Map<String, Object> values = new HashMap<>();
        values.put("id", order.id());
        values.put("intent", order.checkoutPaymentIntent());
        values.put("createTime", order.createTime());

        for (int i = 0; i < order.links().size(); i++) {
            final LinkDescription link = order.links().get(i);
            final Map<String, String> linkValues = Map.of(
                    "href", link.href(),
                    "method", link.method());
            values.put(link.rel(), linkValues);
        }

        return values;
    }
}
