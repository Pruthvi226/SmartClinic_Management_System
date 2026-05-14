package com.smartclinic.dao;

import com.smartclinic.model.MedicineInventory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MedicineInventoryDaoImpl extends GenericDaoImpl<MedicineInventory, Long> implements MedicineInventoryDao {
    @Override
    public List<MedicineInventory> findAll() {
        return getSession()
                .createQuery("from MedicineInventory m order by m.medicineName", MedicineInventory.class)
                .list();
    }

    @Override
    public MedicineInventory findByName(String medicineName) {
        if (medicineName == null) {
            return null;
        }
        return getSession()
                .createQuery("from MedicineInventory m where lower(m.medicineName) = :medicineName", MedicineInventory.class)
                .setParameter("medicineName", medicineName.trim().toLowerCase())
                .uniqueResult();
    }

    @Override
    public List<MedicineInventory> findLowStock() {
        return getSession()
                .createQuery("from MedicineInventory m where m.stockQuantity <= m.reorderLevel order by m.stockQuantity asc, m.medicineName", MedicineInventory.class)
                .list();
    }

    @Override
    public List<MedicineInventory> search(String keyword) {
        String pattern = "%" + keyword.trim().toLowerCase() + "%";
        return getSession()
                .createQuery("from MedicineInventory m where lower(m.medicineName) like :keyword or lower(m.category) like :keyword order by m.medicineName", MedicineInventory.class)
                .setParameter("keyword", pattern)
                .list();
    }
}
