package com.smartclinic.controller;

import com.smartclinic.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pharmacy")
public class PharmacyController {

    @Autowired
    private PrescriptionService prescriptionService;

    @GetMapping("/queue")
    public String pharmacyQueue(Model model) {
        model.addAttribute("prescriptions", prescriptionService.getAllPrescriptionsFetched());
        return "pharmacy/queue";
    }
}
