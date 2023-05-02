package com.example.lab.calculations;

public class CalculationParams {
    private double a;
    private double b;
    private double start;
    private double end;
    public CalculationParams(double a, double b, double start, double end) {
        this.a = a;
        this.b = b;
        this.start = start;
        this.end = end;
    }
    public double getA() {
        return a;
    }
    public void setA(double a) {
        this.a = a;
    }
    public double getB() {
        return b;
    }
    public void setB(double b) {
        this.b = b;
    }
    public double getStart() {
        return start;
    }
    public void setStart(double start) {
        this.start = start;
    }
    public double getEnd() {
        return end;
    }
    public void setEnd(double end) {
        this.end = end;
    }
}

