package com.ewolff.microservice.order.logic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class PaymentsController {

    @Autowired
    public PaymentsController() {
        super();
    }

    @Value("${payments.service.url}")
    private String paymentsUrl;

    @GetMapping("/paymenturl")
    public String getPaymentsUrl() {
        return paymentsUrl;
    }
}