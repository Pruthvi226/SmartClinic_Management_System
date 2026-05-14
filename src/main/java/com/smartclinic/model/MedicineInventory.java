package com.smartclinic.model;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Objects;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "medicine_inventory")
public class MedicineInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "medicine_name", nullable = false, unique = true)
    @NotBlank(message = "Medicine name is required")
    @Size(max = 255, message = "Medicine name must not exceed 255 characters")
    private String medicineName;

    @Column(length = 100)
    @Size(max = 100, message = "Category must not exceed 100 characters")
    private String category;

    @Column(name = "stock_quantity", nullable = false)
    @Min(value = 0, message = "Stock quantity cannot be negative")
    private Integer stockQuantity = 0;

    @Column(name = "reorder_level", nullable = false)
    @Min(value = 0, message = "Reorder level cannot be negative")
    private Integer reorderLevel = 10;

    @Column(name = "unit_price", precision = 10, scale = 2)
    @DecimalMin(value = "0.00", message = "Unit price cannot be negative")
    private BigDecimal unitPrice = BigDecimal.ZERO;

    @Column(name = "batch_number", length = 80)
    private String batchNumber;

    @Column(name = "expiry_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate expiryDate;

    @Column(name = "supplier_name", length = 150)
    private String supplierName;

    @Column(name = "substitution_name", length = 255)
    private String substitutionName;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @PrePersist
    @PreUpdate
    public void touch() {
        updatedAt = new Timestamp(System.currentTimeMillis());
    }

    public boolean isLowStock() {
        return getStockQuantity() <= getReorderLevel();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getStockQuantity() {
        return stockQuantity == null ? 0 : stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Integer getReorderLevel() {
        return reorderLevel == null ? 0 : reorderLevel;
    }

    public void setReorderLevel(Integer reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice == null ? BigDecimal.ZERO : unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSubstitutionName() {
        return substitutionName;
    }

    public void setSubstitutionName(String substitutionName) {
        this.substitutionName = substitutionName;
    }

    public boolean isExpiringSoon() {
        return expiryDate != null && !expiryDate.isBefore(LocalDate.now())
                && expiryDate.isBefore(LocalDate.now().plusDays(45));
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof MedicineInventory))
            return false;
        MedicineInventory that = (MedicineInventory) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
