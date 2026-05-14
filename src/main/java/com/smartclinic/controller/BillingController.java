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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/billing")
public class BillingController {

    @Autowired
    private BillingService billingService;

    @GetMapping("/list")
    public String listBills(@RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "page", required = false) Integer page,
            Model model) {
        List<Billing> bills = billingService.findAll().stream()
                .filter(bill -> keyword == null || keyword.trim().isEmpty()
                        || bill.getAppointment().getPatient().getName().toLowerCase().contains(keyword.trim().toLowerCase())
                        || ("INV-" + bill.getId()).toLowerCase().contains(keyword.trim().toLowerCase()))
                .filter(bill -> status == null || status.trim().isEmpty()
                        || status.equals(bill.getPaymentStatus()))
                .collect(Collectors.toList());
        com.smartclinic.util.PageSlice<Billing> pageSlice = com.smartclinic.util.PaginationUtil.paginate(bills, page, 10);
        model.addAttribute("bills", pageSlice.getItems());
        model.addAttribute("pageSlice", pageSlice);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        return "billing/list";
    }

    @GetMapping("/invoice/{id}")
    public String viewInvoice(@PathVariable("id") Long id, Model model) {
        model.addAttribute("bill", billingService.findById(id));
        return "billing/invoice";
    }

    @PostMapping("/invoice/{id}/payment")
    public String updatePayment(@PathVariable("id") Long id,
            @RequestParam("status") String status,
            @RequestParam(value = "paymentMode", required = false) String paymentMode,
            @RequestParam(value = "paidAmount", required = false) BigDecimal paidAmount,
            @RequestParam(value = "paymentReference", required = false) String paymentReference,
            @RequestParam(value = "discountAmount", required = false) BigDecimal discountAmount,
            @RequestParam(value = "discountReason", required = false) String discountReason,
            @RequestParam(value = "insuranceProvider", required = false) String insuranceProvider,
            @RequestParam(value = "insuranceClaimNumber", required = false) String insuranceClaimNumber,
            @RequestParam(value = "insuranceStatus", required = false) String insuranceStatus) {
        billingService.updatePayment(id, status, paymentMode, paidAmount, paymentReference,
                discountAmount, discountReason, insuranceProvider, insuranceClaimNumber, insuranceStatus);
        return "redirect:/billing/invoice/" + id + "?paymentUpdated=true";
    }

    @PostMapping("/invoice/{id}/refund")
    public String recordRefund(@PathVariable("id") Long id,
            @RequestParam(value = "refundAmount", required = false) BigDecimal refundAmount,
            @RequestParam(value = "refundReason", required = false) String refundReason) {
        billingService.recordRefund(id, refundAmount, refundReason);
        return "redirect:/billing/invoice/" + id + "?refundUpdated=true";
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

    @GetMapping("/receipt/{id}")
    public ResponseEntity<byte[]> downloadReceipt(@PathVariable("id") Long id) {
        Billing bill = billingService.findById(id);
        if(bill == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        byte[] pdf = PdfReportGenerator.generatePaymentReceiptPdf(bill);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "receipt_" + id + ".pdf");

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}
