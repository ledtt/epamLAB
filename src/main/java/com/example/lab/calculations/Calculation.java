package com.example.lab.calculations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.w3c.dom.ranges.RangeException;
@Component
public class Calculation {
    private static final Logger LOGGER = LogManager.getLogger(Calculation.class);

    public double solveEquation(double a, double b, double start, double end) throws RangeException {
        if (start>end)
        {
            LOGGER.error("Range exception");
            throw new RangeException(RangeException.BAD_BOUNDARYPOINTS_ERR, "Start value is bigger than End value");
        }
        double x = start;
        double y = x + a;
        if (y > b)
        {
            LOGGER.error("Range exception");
            throw new RangeException(RangeException.BAD_BOUNDARYPOINTS_ERR, "There's no answer in the range");
        }
        while (y < b && x < end) {
            x += 0.01;
            y = x + a;
        }
        return x;
    }
}
