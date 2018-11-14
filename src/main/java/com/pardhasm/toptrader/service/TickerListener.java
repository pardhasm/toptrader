package com.pardhasm.toptrader.service;

import com.zerodhatech.models.Tick;

import java.util.ArrayList;

public interface TickerListener {
    void process(ArrayList<Tick> ticks);
}
