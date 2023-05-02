package com.example.lab.controllers;
import com.example.lab.cache.Cache;
import com.example.lab.calculations.Calculation;
import com.example.lab.calculations.CalculationParams;
import com.example.lab.counter.Counter;
import com.example.lab.counter.CounterThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.ranges.RangeException;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import org.springframework.lang.NonNull;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    @PostMapping("/bulk")
    public ResponseEntity<?>  bulkCalculate(@RequestBody List<CalculationParams> paramsList) {
        List<Double> results = paramsList.stream()
                .map(p -> {
                    try {
                        return calculation.solveEquation(p.getA(), p.getB(), p.getStart(), p.getEnd());
                    } catch (RangeException e) {
                        LOGGER.error("Range exception in bulk calculation", e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        Map<String, Double> aggregates = new LinkedHashMap<>();
        aggregates.put("minA", paramsList.stream().mapToDouble(CalculationParams::getA).min().orElse(Double.NaN));
        aggregates.put("\nmaxA", paramsList.stream().mapToDouble(CalculationParams::getA).max().orElse(Double.NaN));
        aggregates.put("\navgA", paramsList.stream().mapToDouble(CalculationParams::getA).average().orElse(Double.NaN));
        aggregates.put("\nminB", paramsList.stream().mapToDouble(CalculationParams::getB).min().orElse(Double.NaN));
        aggregates.put("\nmaxB", paramsList.stream().mapToDouble(CalculationParams::getB).max().orElse(Double.NaN));
        aggregates.put("\navgB", paramsList.stream().mapToDouble(CalculationParams::getB).average().orElse(Double.NaN));
        aggregates.put("\navgStart", paramsList.stream().mapToDouble(CalculationParams::getStart).average().orElse(Double.NaN));
        aggregates.put("\navgEnd", paramsList.stream().mapToDouble(CalculationParams::getEnd).average().orElse(Double.NaN));
        aggregates.put("\navgResult", results.stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN));
        return new ResponseEntity<>("Results:" + results + "\n" + aggregates, HttpStatus.OK);
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
