package com.smartclinic.dao;

import com.smartclinic.model.StockMovement;
import java.util.List;

public interface StockMovementDao extends GenericDao<StockMovement, Long> {
    List<StockMovement> findAllFetched();
}
