package com.ebs.services;

import com.ebs.entities.CostPerUnit;

import java.util.HashMap;
import java.util.Optional;

public interface CostPerUnitService {

    Optional<CostPerUnit> getCostPerUnitById(int id);

    HashMap<String, String> updateCostPerUnit(CostPerUnit costPerUnit);
}
