package com.smartclinic.dao;

import com.smartclinic.model.StockMovement;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class StockMovementDaoImpl extends GenericDaoImpl<StockMovement, Long> implements StockMovementDao {
    @Override
    public List<StockMovement> findAllFetched() {
        return getSession().createQuery(
                "select m from StockMovement m join fetch m.medicine order by m.id desc",
                StockMovement.class)
                .list();
    }
}
