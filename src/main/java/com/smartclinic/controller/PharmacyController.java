package com.smartclinic.controller;

import com.smartclinic.model.MedicineInventory;
import com.smartclinic.model.Prescription;
import com.smartclinic.model.User;
import com.smartclinic.service.AuditLogService;
import com.smartclinic.service.MedicineInventoryService;
import com.smartclinic.service.PrescriptionService;
import com.smartclinic.service.StockMovementService;
import com.smartclinic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/pharmacy")
public class PharmacyController {

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private MedicineInventoryService medicineInventoryService;

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private UserService userService;

    @Autowired
    private StockMovementService stockMovementService;

    @GetMapping("/queue")
    public String pharmacyQueue(Model model) {
        model.addAttribute("prescriptions", prescriptionService.getPendingPrescriptionsFetched());
        model.addAttribute("lowStockMedicines", medicineInventoryService.findLowStock());
        return "pharmacy/queue";
    }

    @GetMapping("/inventory")
    public String inventory(@RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", required = false) Integer page,
            Model model) {
        java.util.List<MedicineInventory> medicines = medicineInventoryService.search(keyword);
        com.smartclinic.util.PageSlice<MedicineInventory> pageSlice = com.smartclinic.util.PaginationUtil.paginate(medicines, page, 10);
        model.addAttribute("medicines", pageSlice.getItems());
        model.addAttribute("pageSlice", pageSlice);
        model.addAttribute("lowStockMedicines", medicineInventoryService.findLowStock());
        model.addAttribute("expiringSoonMedicines", medicineInventoryService.findExpiringSoon());
        model.addAttribute("medicine", new MedicineInventory());
        model.addAttribute("keyword", keyword);
        return "pharmacy/inventory";
    }

    @PostMapping("/inventory")
    public String saveMedicine(@ModelAttribute("medicine") MedicineInventory medicine) {
        medicineInventoryService.save(medicine);
        return "redirect:/pharmacy/inventory?saved=true";
    }

    @PostMapping("/inventory/{id}/restock")
    public String restockMedicine(@PathVariable("id") Long medicineId,
            @RequestParam("quantityToAdd") int quantityToAdd) {
        try {
            medicineInventoryService.restock(medicineId, quantityToAdd);
            return "redirect:/pharmacy/inventory?restocked=true";
        } catch (Exception e) {
            return "redirect:/pharmacy/inventory?error=restock";
        }
    }

    @PostMapping("/prescriptions/{id}/dispense")
    public String dispensePrescription(@PathVariable("id") Long prescriptionId, HttpServletRequest request) {
        try {
            Prescription prescription = prescriptionService.findById(prescriptionId);
            if (prescription == null || prescription.isDispensed()) {
                return "redirect:/pharmacy/queue?error=missing";
            }
            medicineInventoryService.dispensePrescription(prescription);
            prescriptionService.markDispensed(prescriptionId);
            auditLogService.logAction(currentUser(), "DISPENSED prescription #" + prescriptionId,
                    "APPOINTMENT", prescription.getAppointment().getId(), request.getRemoteAddr());
            return "redirect:/pharmacy/queue?dispensed=true";
        } catch (Exception e) {
            return "redirect:/pharmacy/queue?error=stock";
        }
    }

    @GetMapping("/movements")
    public String stockMovements(Model model) {
        model.addAttribute("movements", stockMovementService.findAll());
        return "pharmacy/movements";
    }

    @GetMapping("/purchase-order")
    public String purchaseOrder(Model model) {
        model.addAttribute("lowStockMedicines", medicineInventoryService.findLowStock());
        model.addAttribute("expiringSoonMedicines", medicineInventoryService.findExpiringSoon());
        return "pharmacy/purchase-order";
    }

    private User currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return null;
        }
        return userService.findByEmail(auth.getName());
    }
}
