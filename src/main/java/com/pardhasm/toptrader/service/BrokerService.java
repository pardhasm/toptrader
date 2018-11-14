package com.pardhasm.toptrader.service;

import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.*;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface BrokerService {
    String  loginUrl();

    void initialiseSession(@NonNull String requestToken) throws KiteException, IOException;

    Margin getMargin(String segment) throws KiteException, IOException;

    String placeOrder(OrderParams orderParams) throws KiteException, IOException;

    Boolean cancelOrder(String orderId) throws KiteException, IOException;

    boolean initialiseTicker(ArrayList<Long> tokens, TickerListener tickerListener) throws KiteException;

    HistoricalData getHistoricalData(Date from, Date to, String token, String interval, boolean continuous) throws KiteException, IOException;

    Map<String, List<Position>> getPostions() throws KiteException, IOException;

    List<Trade> getTrades() throws KiteException, IOException;

    List<Order> getOrders() throws KiteException, IOException;
}
