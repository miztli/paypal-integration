package com.example.paypalintegration.controllers;

import javax.annotation.Resource;

import com.example.paypalintegration.services.PaypalOrderOperation;
import com.example.paypalintegration.services.PaypalService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/paypal")
public class PaypalController
{
    @Resource
    private PaypalService paypalService;

    @PostMapping(value = "/orders")
    public void createOrder() {
        paypalService.createOrder(PaypalOrderOperation.CAPTURE);
    }
}