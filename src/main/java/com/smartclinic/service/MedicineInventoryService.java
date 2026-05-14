package com.smartclinic.service;

import com.smartclinic.model.MedicineInventory;
import com.smartclinic.model.Prescription;

import java.util.List;

public interface MedicineInventoryService {
    List<MedicineInventory> findAll();
    List<MedicineInventory> search(String keyword);
    List<MedicineInventory> findLowStock();
    MedicineInventory findById(Long id);
    MedicineInventory findByName(String medicineName);
    void save(MedicineInventory medicine);
    void restock(Long id, int quantityToAdd);
    List<MedicineInventory> findExpiringSoon();
    void dispensePrescription(Prescription prescription);
}
