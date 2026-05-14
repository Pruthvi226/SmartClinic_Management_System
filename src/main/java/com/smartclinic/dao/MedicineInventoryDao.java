package com.smartclinic.dao;

import com.smartclinic.model.MedicineInventory;
import java.util.List;

public interface MedicineInventoryDao extends GenericDao<MedicineInventory, Long> {
    MedicineInventory findByName(String medicineName);
    List<MedicineInventory> findLowStock();
    List<MedicineInventory> search(String keyword);
}
