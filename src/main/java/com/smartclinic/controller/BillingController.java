package com.smartclinic.controller;

import com.smartclinic.model.Billing;
import com.smartclinic.service.BillingService;
import com.smartclinic.util.PdfReportGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/billing")
public class BillingController {

    @Autowired
    private BillingService billingService;

    @GetMapping("/list")
    public String listBills(Model model) {
        model.addAttribute("bills", billingService.findAll());
        return "billing/list";
    }

    @GetMapping("/invoice/{id}")
    public String viewInvoice(@PathVariable("id") Long id, Model model) {
        model.addAttribute("bill", billingService.findById(id));
        return "billing/invoice";
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadInvoice(@PathVariable("id") Long id) {
        Billing bill = billingService.findById(id);
        if(bill == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        byte[] pdf = PdfReportGenerator.generateBillingInvoicePdf(bill);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "invoice_" + id + ".pdf");

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}
