package com.example.paypalintegration.services;

import java.io.IOException;

public interface PaypalService
{
    void createOrder(PaypalOrderOperation operation) throws IOException;
}
