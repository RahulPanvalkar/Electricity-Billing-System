package com.ebs.dao;

import com.ebs.entities.CostPerUnit;

import java.util.Optional;

public interface CostPerUnitDao {
    Optional<CostPerUnit> findById(int id);

    int updateCostPerUnit(CostPerUnit costPerUnit);
}
