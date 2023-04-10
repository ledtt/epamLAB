package com.example.lab;

import com.example.lab.calculations.Calculation;
import com.example.lab.controllers.CalculationController;
import com.example.lab.cache.Cache;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.w3c.dom.ranges.RangeException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class LabApplicationTests {
    @Mock
    private Cache<List<Double>, List<Double>> cache;

    @Mock
    private Calculation calculation;

    @InjectMocks
    private CalculationController calculationController;

    @Test
    public void testCalculateWhenResultInCache() throws Exception {
        // Mock data
        double a = 2.0;
        double b = 5.0;
        double start = 0.0;
        double end = 10.0;
        List<Double> params = Arrays.asList(a, b, start, end);
        List<Double> results = Collections.singletonList(3.0);

        // Mock behavior
        when(cache.isContains(params)).thenReturn(true);
        when(cache.get(params)).thenReturn(results);

        // Call the method and assert the result
        ResponseEntity<?> response = calculationController.calculate(a, b, start, end);
        verify(cache, times(1)).isContains(params);
        verify(cache, times(1)).get(params);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Result from cache: [3.0]  counter=1", response.getBody());
    }

    @Test
    public void testCalculateWhenResultNotInCache() throws Exception {
        // Mock data
        double a = 2.0;
        double b = 5.0;
        double start = 0.0;
        double end = 10.0;
        List<Double> params = Arrays.asList(a, b, start, end);
        List<Double> results = Collections.singletonList(3.0);

        // Mock behavior
        when(cache.isContains(params)).thenReturn(false);
        when(calculation.solveEquation(a, b, start, end)).thenReturn(3.0);

        // Call the method and assert the result
        ResponseEntity<?> response = calculationController.calculate(a, b, start, end);
        verify(cache, times(1)).isContains(params);
        verify(calculation, times(1)).solveEquation(a, b, start, end);
        verify(cache, times(1)).push(params, results);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Calculated result: 3, [3.0]", response.getBody());
    }
    @Test
    public void testCalculate() throws RangeException {
        double a = 1.0;
        double b = 2.0;
        double start = 0.0;
        double end = 10.0;
        double result = 1.0;

        when(calculation.solveEquation(a, b, start, end)).thenReturn(result);

        ResponseEntity<?> responseEntity = calculationController.calculate(a, b, start, end);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Calculated result: 2, [1.0]", responseEntity.getBody());
    }

    @Test
    public void testCalculateThrowsException() throws RangeException {
        double a = 1.0;
        double b = 2.0;
        double start = 10.0;
        double end = 0.0;

        Mockito.doThrow(new RangeException(RangeException.BAD_BOUNDARYPOINTS_ERR, "Start value is bigger than End value"))
                .when(calculation).solveEquation(a, b, start, end);

        ResponseEntity<String> responseEntity = calculationController.handleException(new RangeException(RangeException.BAD_BOUNDARYPOINTS_ERR, "Start value is bigger than End value"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Error: 500. Wrong Range", responseEntity.getBody());
    }
}



