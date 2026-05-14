package com.smartclinic.service;

import com.smartclinic.model.MedicineInventory;
import com.smartclinic.model.StockMovement;
import java.util.List;

public interface StockMovementService {
    void record(MedicineInventory medicine, String movementType, int quantity, String reason);
    List<StockMovement> findAll();
}
