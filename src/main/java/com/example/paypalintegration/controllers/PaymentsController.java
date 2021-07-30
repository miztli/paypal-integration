package com.example.paypalintegration.controllers;

import com.example.paypalintegration.services.PaypalService;
import com.paypal.http.HttpResponse;
import com.paypal.payments.Capture;
import com.paypal.payments.LinkDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/paypal/payments")
public class PaymentsController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentsController.class);

    @Resource
    private PaypalService paypalService;

    @PostMapping(value = "/authorizations/{authId}/capture")
    public ResponseEntity<Map<String, Object>> capturePaymentAuthorization(@PathVariable(value = "authId") final String authId) throws IOException {
        final HttpResponse<Capture> captureHttpResponse = paypalService.capturePaymentAuthorization(authId);
        logCapture(captureHttpResponse);
        final Map<String, Object> captureValues = toMap(captureHttpResponse);
        return ResponseEntity.status(captureHttpResponse.statusCode()).body(captureValues);
    }

    private void logCapture(final HttpResponse<Capture> captureHttpResponse) {
        final Capture capture = captureHttpResponse.result();
        LOGGER.info("------------------------------------------------------");
        LOGGER.info("HTTP status -> {}", captureHttpResponse.statusCode());
        LOGGER.info("Capture id -> {}", capture.id());
        LOGGER.info("Capture amount -> value [{}] -> currency code -> [{}]", capture.amount().value(), capture.amount().currencyCode());
        LOGGER.info("Capture time -> {}", capture.createTime());
        capture.links().stream().forEach(link -> LOGGER.info("href [{}] -> method [{}] -> rel [{}]", link.href(), link.method(), link.rel()));
    }


    private Map<String, Object> toMap(final HttpResponse<Capture> captureHttpResponse) {
        final Capture capture = captureHttpResponse.result();
        final Map<String, Object> values = new HashMap<>();
        values.put("id", capture.id());
        values.put("amount", Map.of("value", capture.amount().value(), "currency-code", capture.amount().currencyCode()));
        values.put("createTime", capture.createTime());

        for (int i = 0; i < capture.links().size(); i++) {
            final LinkDescription link = capture.links().get(i);
            final Map<String, String> linkValues = Map.of(
                    "href", link.href(),
                    "method", link.method());
            values.put(link.rel(), linkValues);
        }

        return values;
    }
}
