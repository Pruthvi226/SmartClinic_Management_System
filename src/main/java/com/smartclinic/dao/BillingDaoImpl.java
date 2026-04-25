package com.smartclinic.dao;
import com.smartclinic.model.Billing;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;

@Repository
public class BillingDaoImpl extends GenericDaoImpl<Billing, Long> implements BillingDao {
    @Override
    public BigDecimal calculateTax(BigDecimal baseAmount) {
        Session session = getSession();
        BigDecimal[] result = new BigDecimal[1];
        session.doWork((Connection connection) -> {
            try (CallableStatement callableStatement = connection.prepareCall("{call calculate_tax(?, ?)}")) {
                callableStatement.setBigDecimal(1, baseAmount);
                callableStatement.registerOutParameter(2, Types.DECIMAL);
                callableStatement.execute();
                result[0] = callableStatement.getBigDecimal(2);
            }
        });
        return result[0];
    }
}
