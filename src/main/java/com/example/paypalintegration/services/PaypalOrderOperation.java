package com.example.paypalintegration.services;

public enum PaypalOrderOperation
{
    AUTHORIZE("AUTHORIZE"),
    CAPTURE("CAPTURE");

    private final String operation;

    PaypalOrderOperation(final String operation)
    {
        this.operation = operation;
    }

    public String getOperation()
    {
        return operation;
    }
}
