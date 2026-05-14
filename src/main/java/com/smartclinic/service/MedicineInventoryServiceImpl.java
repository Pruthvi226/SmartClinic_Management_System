package com.smartclinic.service;

import com.smartclinic.dao.MedicineInventoryDao;
import com.smartclinic.model.MedicineInventory;
import com.smartclinic.model.Prescription;
import com.smartclinic.model.PrescriptionItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class MedicineInventoryServiceImpl implements MedicineInventoryService {

    @Autowired
    private MedicineInventoryDao medicineInventoryDao;

    @Autowired
    private StockMovementService stockMovementService;

    @Override
    public List<MedicineInventory> findAll() {
        return medicineInventoryDao.findAll();
    }

    @Override
    public List<MedicineInventory> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return findAll();
        }
        return medicineInventoryDao.search(keyword);
    }

    @Override
    public List<MedicineInventory> findLowStock() {
        return medicineInventoryDao.findLowStock();
    }

    @Override
    public MedicineInventory findById(Long id) {
        return medicineInventoryDao.findById(id);
    }

    @Override
    public MedicineInventory findByName(String medicineName) {
        return medicineInventoryDao.findByName(medicineName);
    }

    @Override
    public void save(MedicineInventory medicine) {
        MedicineInventory existing = medicine.getId() != null
                ? medicineInventoryDao.findById(medicine.getId())
                : medicineInventoryDao.findByName(medicine.getMedicineName());

        MedicineInventory target = existing != null ? existing : medicine;
        target.setMedicineName(clean(medicine.getMedicineName()));
        target.setCategory(clean(medicine.getCategory()));
        target.setStockQuantity(nonNegative(medicine.getStockQuantity()));
        target.setReorderLevel(nonNegative(medicine.getReorderLevel()));
        target.setUnitPrice(medicine.getUnitPrice() == null ? BigDecimal.ZERO : medicine.getUnitPrice());
        target.setBatchNumber(clean(medicine.getBatchNumber()));
        target.setExpiryDate(medicine.getExpiryDate());
        target.setSupplierName(clean(medicine.getSupplierName()));
        target.setSubstitutionName(clean(medicine.getSubstitutionName()));
        medicineInventoryDao.save(target);
    }

    @Override
    public void restock(Long id, int quantityToAdd) {
        if (quantityToAdd <= 0) {
            throw new IllegalArgumentException("Restock quantity must be greater than zero");
        }
        MedicineInventory medicine = medicineInventoryDao.findById(id);
        if (medicine == null) {
            throw new IllegalArgumentException("Medicine not found");
        }
        medicine.setStockQuantity(medicine.getStockQuantity() + quantityToAdd);
        medicineInventoryDao.update(medicine);
        recordMovement(medicine, "RESTOCK", quantityToAdd, "Manual restock");
    }

    @Override
    public List<MedicineInventory> findExpiringSoon() {
        return findAll().stream()
                .filter(MedicineInventory::isExpiringSoon)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public void dispensePrescription(Prescription prescription) {
        if (prescription == null || prescription.getItems() == null || prescription.getItems().isEmpty()) {
            throw new IllegalArgumentException("Prescription has no medicine items");
        }

        Map<MedicineInventory, Integer> dispenseQuantities = new LinkedHashMap<>();
        for (PrescriptionItem item : prescription.getItems()) {
            MedicineInventory medicine = medicineInventoryDao.findByName(item.getMedicineName());
            if (medicine == null) {
                throw new IllegalStateException("Inventory item missing: " + item.getMedicineName());
            }

            int requested = item.getQuantity();
            int totalRequested = dispenseQuantities.getOrDefault(medicine, 0) + requested;
            if (medicine.getStockQuantity() < totalRequested) {
                throw new IllegalStateException("Insufficient stock for " + medicine.getMedicineName());
            }
            dispenseQuantities.put(medicine, totalRequested);
        }

        dispenseQuantities.forEach((medicine, quantity) -> {
            medicine.setStockQuantity(medicine.getStockQuantity() - quantity);
            medicineInventoryDao.update(medicine);
            recordMovement(medicine, "DISPENSE", quantity, "Prescription #" + prescription.getId());
        });
    }

    private void recordMovement(MedicineInventory medicine, String movementType, int quantity, String reason) {
        if (stockMovementService != null) {
            stockMovementService.record(medicine, movementType, quantity, reason);
        }
    }

    private Integer nonNegative(Integer value) {
        return value == null || value < 0 ? 0 : value;
    }

    private String clean(String value) {
        return value == null ? null : value.trim();
    }
}
