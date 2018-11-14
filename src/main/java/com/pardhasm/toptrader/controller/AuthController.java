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
    //TODO: Authorize,Authenticate,Create session,Redirect. Refer https://auth0.com/blog/implementing-jwt-authentication-on-spring-boot/
    //TODO: We can also use auth0 (check https://auth0.com/docs/quickstart/webapp/java-spring-security-mvc).
    //TODO: Compare both approaches and decide on the best (https://auth0.com/why-auth0)
    public RedirectView redirectWithUsingRedirectView(
            RedirectAttributes attributes) {
        return new RedirectView(brokerService.loginUrl());
    }

    //TODO: Initialize per user session
    @GetMapping(value = "/authorise",produces = "application/json")
    public void authenticate(@RequestParam("request_token") String requestToken) throws KiteException, IOException {
        brokerService.initialiseSession(requestToken);
    }




}
