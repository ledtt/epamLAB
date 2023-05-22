package com.example.lab.async;
import com.example.lab.calculations.Calculation;
import com.example.lab.calculations.CalculationParams;
import com.example.lab.services.CalculationParamsService;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
@Component
public class CalculationParamsAsync {

    private final CalculationParamsService calculationParamsService;
    private final Calculation calculation;

    public CalculationParamsAsync(CalculationParamsService calculationParamsService, Calculation calculation) {
        this.calculationParamsService = calculationParamsService;
        this.calculation = calculation;
    }

    public Integer createAsync(CalculationParams params) {
        CalculationParams params2 = new CalculationParams();
        params2.setA(params.getA());
        params2.setB(params.getB());
        params2.setStart(params.getStart());
        params2.setEnd(params.getEnd());

        calculationParamsService.save(params2);
        return params2.getId();
    }

    public void computeAsync(int id) {
        CompletableFuture.supplyAsync(() -> {
            try {
                CalculationParams result = calculationParamsService.findOne(id);

                Thread.sleep(15000);
                result.setResult(calculation.solveEquation(result.getA(), result.getB(), result.getStart(), result.getEnd()));

                calculationParamsService.save(result);

                return result.getId();
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
        });
    }

}