package com.smartclinic.dao;
import com.smartclinic.model.Billing;
import java.math.BigDecimal;
public interface BillingDao extends GenericDao<Billing, Long> {
    BigDecimal calculateTax(BigDecimal baseAmount);
}
