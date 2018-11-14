package com.pardhasm.toptrader.service.impl;

import com.pardhasm.toptrader.service.BrokerService;
import com.pardhasm.toptrader.service.TickerListener;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.kiteconnect.utils.Constants;
import com.zerodhatech.models.*;
import com.zerodhatech.ticker.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class KiteServiceImpl implements BrokerService {

    @Value("${userId}")
    private String userId;
    @Value("${apiKey}")
    private String apiKey;
    @Value("${apiSecret}")
    private String apiSecret;
    private KiteConnect kiteSdk;
    private KiteTicker ticker;
    private final ConcurrentMap<String, Order> orders = new ConcurrentHashMap<>();

    @PostConstruct
    void init(){
    }

    @Override
    public String  loginUrl(){
        kiteSdk=new KiteConnect(apiKey);
        kiteSdk.setUserId(userId);
        String url = kiteSdk.getLoginURL();
        System.out.println(url);
        return url;
    }

    @Override
    public void initialiseSession(@NonNull String requestToken) throws KiteException, IOException {
        User user =  kiteSdk.generateSession(requestToken, apiSecret);
        kiteSdk.setAccessToken(user.accessToken);
        kiteSdk.setPublicToken(user.publicToken);
        kiteSdk.setSessionExpiryHook(() -> System.out.println("session expired"));
    }

    @Override
    public Margin getMargin(String segment) throws KiteException, IOException {
        Margin margins = kiteSdk.getMargins(segment);
        System.out.println(margins);
        return margins;
    }

    @Override
    public String placeOrder(OrderParams orderParams) throws KiteException, IOException {
        Order order = kiteSdk.placeOrder(orderParams, Constants.VARIETY_REGULAR);
        System.out.println(order.orderId);
        orders.putIfAbsent(order.orderId,order);
        return order.orderId;
    }

    @Override
    public Boolean cancelOrder(String orderId) throws KiteException, IOException {
        Boolean cancelled = false;
        Order order = orders.get(orderId);
        if(order!=null){
            Order cancelledOrder = kiteSdk.cancelOrder(orderId,order.orderVariety);
            cancelled = cancelledOrder.status.equalsIgnoreCase("cancelled");
        }
        return cancelled;
    }

    @Override
    public boolean initialiseTicker(ArrayList<Long> tokens, TickerListener tickerListener) throws KiteException {
        ticker = new KiteTicker(kiteSdk.getAccessToken(), kiteSdk.getApiKey());
        ticker.setOnConnectedListener(() -> {
            ticker.subscribe(tokens);
            ticker.setMode(tokens, KiteTicker.modeFull);
        });
        ticker.setOnTickerArrivalListener(ticks -> {
            if(ticks.size() > 0) {
                tickerListener.process(ticks);
            }
        });
        ticker.setOnDisconnectedListener(() -> {});
        ticker.setOnOrderUpdateListener(order -> System.out.println("order update "+order.orderId));
        ticker.setTryReconnection(true);
        ticker.setMaximumRetries(10);
        ticker.setMaximumRetryInterval(30);
        ticker.connect();
        return ticker.isConnectionOpen();
    }

    @Override
    public HistoricalData getHistoricalData(Date from, Date to, String token, String interval, boolean continuous) throws KiteException, IOException {
        HistoricalData historicalData = kiteSdk.getHistoricalData(from, to, token, interval, continuous);
        return historicalData;
    }

    @Override
    public Map<String, List<Position>> getPostions() throws KiteException, IOException {
        Map<String, List<Position>> positions = kiteSdk.getPositions();
        return positions;
    }
    @Override
    public List<Trade> getTrades() throws KiteException, IOException {
        List<Trade> trades = kiteSdk.getTrades();
        return trades;
    }

    @Override
    public List<Order> getOrders() throws KiteException, IOException {
        List<Order> orders = kiteSdk.getOrders();
        return orders;
    }



}
