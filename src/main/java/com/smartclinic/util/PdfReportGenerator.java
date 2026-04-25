package com.smartclinic.util;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.smartclinic.model.Billing;
import com.smartclinic.model.Prescription;
import com.smartclinic.model.PrescriptionItem;

import java.io.ByteArrayOutputStream;

public class PdfReportGenerator {

    public static byte[] generatePrescriptionPdf(Prescription prescription) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("SmartClinic Hospital").setBold().setFontSize(20).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Prescription Slip").setFontSize(14).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Patient Name: " + prescription.getPatient().getName()));
            document.add(new Paragraph("Doctor Name: " + prescription.getDoctor().getUser().getName()));
            document.add(new Paragraph("Date: " + prescription.getIssuedAt().toString()));
            document.add(new Paragraph("Diagnosis: " + prescription.getDiagnosis()));
            document.add(new Paragraph("\n"));

            Table table = new Table(new float[]{4, 2, 2, 4});
            table.setWidth(UnitValue.createPercentValue(100));
            table.addHeaderCell("Medicine");
            table.addHeaderCell("Dosage");
            table.addHeaderCell("Duration");
            table.addHeaderCell("Instructions");

            if (prescription.getItems() != null) {
                for (PrescriptionItem item : prescription.getItems()) {
                    table.addCell(item.getMedicineName());
                    table.addCell(item.getDosage());
                    table.addCell(item.getDuration());
                    table.addCell(item.getInstructions() != null ? item.getInstructions() : "");
                }
            }
            document.add(table);

            document.add(new Paragraph("\n\n\nDoctor Signature: _______________________").setTextAlignment(TextAlignment.RIGHT));

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    public static byte[] generateBillingInvoicePdf(Billing billing) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("SmartClinic Hospital").setBold().setFontSize(20).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Billing Invoice").setFontSize(14).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Invoice Number: INV-" + billing.getId()));
            document.add(new Paragraph("Date: " + billing.getGeneratedAt().toString()));
            document.add(new Paragraph("Patient Name: " + billing.getAppointment().getPatient().getName()));
            document.add(new Paragraph("\n"));

            Table table = new Table(new float[]{6, 4});
            table.setWidth(UnitValue.createPercentValue(100));
            table.addHeaderCell("Description");
            table.addHeaderCell("Amount (USD)");

            table.addCell("Consultation Fee");
            table.addCell(billing.getAmount().toString());
            table.addCell("Tax (18%)");
            table.addCell(billing.getTax().toString());
            
            Cell totalLabel = new Cell().add(new Paragraph("Total").setBold());
            table.addCell(totalLabel);
            Cell totalAmount = new Cell().add(new Paragraph(billing.getTotal().toString()).setBold());
            table.addCell(totalAmount);

            document.add(table);
            document.add(new Paragraph("\nPayment Status: " + billing.getPaymentStatus()).setBold());

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }
}
