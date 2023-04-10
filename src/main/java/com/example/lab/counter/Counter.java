package com.example.lab.counter;

public class Counter {
    private static int COUNTER = 0;

    public static synchronized void increment(){
        COUNTER++;
    }
    //если несколько потоков одновременно вызовут метод increment(),
    // только один поток будет иметь доступ к выполнению этого метода
    // в любой момент времени. Гарантируется, что значение переменной COUNTER
    // будет увеличено только на 1 в любой момент времени.

    public static int getCounter(){
        return COUNTER;
    }

}