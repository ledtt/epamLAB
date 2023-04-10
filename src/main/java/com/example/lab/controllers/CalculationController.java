package com.example.lab.controllers;
import com.example.lab.cache.Cache;
import com.example.lab.calculations.Calculation;
import com.example.lab.counter.Counter;
import com.example.lab.counter.CounterThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.w3c.dom.ranges.RangeException;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import org.springframework.lang.NonNull;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Validated
@RestController
public class CalculationController {
    private final Calculation calculation;
    private static final Logger LOGGER = LogManager.getLogger(CalculationController.class);
    private final Cache<List<Double>, List<Double>> cache;

    @Autowired
    public CalculationController(Calculation calculation, Cache<List<Double>,
            List<Double>> cache) {
        this.calculation = calculation;
        this.cache = cache;

    }
    @GetMapping("/counter")
    public ResponseEntity<?> count()
    {
        CounterThread counter = new CounterThread();
        counter.start();
        return ResponseEntity.ok("Counter="+Counter.getCounter());
    }
    @GetMapping("/calculation")

    public ResponseEntity<?> calculate( @Valid @NonNull @RequestParam(value = "a")  double a,
                                        @RequestParam(value = "b") @Valid @NonNull double b,
                                        @RequestParam(value ="start") @Valid @NonNull double start,
                                        @RequestParam(value ="end") @Valid @NonNull double end)
            throws RangeException{

        CounterThread counter = new CounterThread();
        counter.start();

        LOGGER.info("GetMapping by address localhost:8080/calculation?a=...&b=...start=...&end=...");

        List<Double> params = new ArrayList<>();
        params.add(a);
        params.add(b);
        params.add(start);
        params.add(end);
        double result;
        if (cache.isContains(params)) {
            LOGGER.info("Retrieved result from cache");
            result = cache.get(params).get(0);
            return ResponseEntity.ok("Result from cache: "+result+"  counter="+Counter.getCounter());
        }
        else {
            LOGGER.info("Calculate");
            result = calculation.solveEquation(a, b, start, end);
            //results.add(result);
            cache.push(params, Collections.singletonList(result));
            String message = String.format("%f + x = %f, x = %f - pushed in cache", a, b, result);
            LOGGER.info(message);
        }
        return new ResponseEntity<>("Calculated result: " + result + ", counter=" +Counter.getCounter(), HttpStatus.OK);
    }
    @ExceptionHandler(RangeException.class)
    public ResponseEntity<String> handleException(Exception ex) {
        String message = "Error: 500. Wrong Range";
        LOGGER.error(message, ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }
}
