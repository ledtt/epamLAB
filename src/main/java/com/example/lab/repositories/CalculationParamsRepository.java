package com.example.lab.repositories;
import com.example.lab.calculations.CalculationParams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalculationParamsRepository extends JpaRepository<CalculationParams, Integer> {}