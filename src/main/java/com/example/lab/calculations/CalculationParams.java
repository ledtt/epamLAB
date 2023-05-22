package com.example.lab.calculations;
import jakarta.persistence.*;

@Entity
@Table(name="calculation_params")
public class CalculationParams {

    @Id @Column (name="/id/")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column (name="/a/")
    private double a;
    @Column (name="/b/")
    private double b;
    @Column (name="/start/")
    private double start;
    @Column (name="/end/")
    private double end;
    @Column (name="/result/")
    private double result;

    public CalculationParams(double a, double b, double start, double end) {
        this.a = a;
        this.b = b;
        this.start = start;
        this.end = end;
    }

    public CalculationParams(double a, double b, double start, double end,double result) {
        this.a = a;
        this.b = b;
        this.start = start;
        this.end = end;
        this.result=result;
    }
    public CalculationParams() {

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

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public double getResult() {
        return result;
    }
    public void setResult(double result) {
        this.result = result;
    }

}

