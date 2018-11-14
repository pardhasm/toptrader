package com.pardhasm.toptrader.controller;


import com.pardhasm.toptrader.service.BrokerService;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@Controller
@RequestMapping("/")
public class AuthController {

    private final BrokerService brokerService;

    @Autowired
    public AuthController(BrokerService brokerService) {this.brokerService = brokerService;}

    @GetMapping("/login")
    public RedirectView redirectWithUsingRedirectView(
            RedirectAttributes attributes) {
        return new RedirectView(brokerService.loginUrl());
    }

    @GetMapping(value = "/authorise",produces = "application/json")
    public void authenticate(@RequestParam("request_token") String requestToken) throws KiteException, IOException {
        brokerService.initialiseSession(requestToken);
    }




}
