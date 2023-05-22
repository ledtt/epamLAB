package com.example.lab.services;
import com.example.lab.calculations.CalculationParams;
import com.example.lab.repositories.CalculationParamsRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CalculationParamsService {

    private final CalculationParamsRepository calculationParamsRepository;


    public CalculationParamsService(CalculationParamsRepository calculationParamsRepository) {
        this.calculationParamsRepository = calculationParamsRepository;
    }


    public void save(CalculationParams params) {
        calculationParamsRepository.save(params);
    }


    public CalculationParams findOne(int id) {
        Optional<CalculationParams> numbers = calculationParamsRepository.findById(id);

        return numbers.orElse(null);
    }


}