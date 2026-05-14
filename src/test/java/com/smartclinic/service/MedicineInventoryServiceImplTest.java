package com.smartclinic.service;

import com.smartclinic.dao.MedicineInventoryDao;
import com.smartclinic.model.MedicineInventory;
import com.smartclinic.model.Prescription;
import com.smartclinic.model.PrescriptionItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MedicineInventoryServiceImplTest {

    @Mock
    private MedicineInventoryDao medicineInventoryDao;

    @InjectMocks
    private MedicineInventoryServiceImpl medicineInventoryService;

    @Test
    void dispensePrescriptionReducesStockByPrescribedQuantity() {
        MedicineInventory aspirin = medicine("Aspirin", 10, 5);
        Prescription prescription = prescription(item("Aspirin", 3));
        when(medicineInventoryDao.findByName("Aspirin")).thenReturn(aspirin);

        medicineInventoryService.dispensePrescription(prescription);

        assertEquals(7, aspirin.getStockQuantity());
        verify(medicineInventoryDao).update(aspirin);
    }

    @Test
    void dispensePrescriptionRejectsInsufficientStockBeforeUpdating() {
        MedicineInventory atorvastatin = medicine("Atorvastatin", 1, 5);
        Prescription prescription = prescription(item("Atorvastatin", 2));
        when(medicineInventoryDao.findByName("Atorvastatin")).thenReturn(atorvastatin);

        assertThrows(IllegalStateException.class,
                () -> medicineInventoryService.dispensePrescription(prescription));

        assertEquals(1, atorvastatin.getStockQuantity());
        verify(medicineInventoryDao, never()).update(atorvastatin);
    }

    private MedicineInventory medicine(String name, int stock, int reorderLevel) {
        MedicineInventory medicine = new MedicineInventory();
        medicine.setMedicineName(name);
        medicine.setStockQuantity(stock);
        medicine.setReorderLevel(reorderLevel);
        return medicine;
    }

    private Prescription prescription(PrescriptionItem... items) {
        Prescription prescription = new Prescription();
        prescription.setItems(List.of(items));
        return prescription;
    }

    private PrescriptionItem item(String medicineName, int quantity) {
        PrescriptionItem item = new PrescriptionItem();
        item.setMedicineName(medicineName);
        item.setDosage("75 mg");
        item.setDuration("7 days");
        item.setQuantity(quantity);
        return item;
    }
}
