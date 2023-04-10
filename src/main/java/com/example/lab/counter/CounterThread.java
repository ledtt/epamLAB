package com.example.lab.counter;

import org.springframework.stereotype.Component;

@Component
public class CounterThread extends Thread{ //поток, переопределяющий метод start()

    @Override
    public void start() {
        Counter.increment();
    }
}