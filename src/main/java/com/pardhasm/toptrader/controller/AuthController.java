package com.pardhasm.toptrader.controller;


import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.pardhasm.toptrader.service.BrokerService;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;

@RestController
@RequestMapping("/")
public class AuthController {

    private final BrokerService brokerService;

    @Autowired
    public AuthController(BrokerService brokerService) {this.brokerService = brokerService;}

    @GetMapping("/login2")
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
    
    //Redirect endpoint
    @GetMapping(value="/redirect")
    public String successAuthentication(){
    	return "success";
    }

}
