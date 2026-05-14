package com.smartclinic.service;

import com.smartclinic.dao.StockMovementDao;
import com.smartclinic.model.MedicineInventory;
import com.smartclinic.model.StockMovement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class StockMovementServiceImpl implements StockMovementService {
    @Autowired
    private StockMovementDao stockMovementDao;

    @Override
    public void record(MedicineInventory medicine, String movementType, int quantity, String reason) {
        if (medicine == null || quantity <= 0) {
            return;
        }
        StockMovement movement = new StockMovement();
        movement.setMedicine(medicine);
        movement.setMovementType(movementType);
        movement.setQuantity(quantity);
        movement.setReason(reason);
        stockMovementDao.save(movement);
    }

    @Override
    public List<StockMovement> findAll() {
        return stockMovementDao.findAllFetched();
    }
}
