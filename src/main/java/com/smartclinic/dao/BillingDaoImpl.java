package com.smartclinic.dao;
import com.smartclinic.model.Billing;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

@Repository
public class BillingDaoImpl extends GenericDaoImpl<Billing, Long> implements BillingDao {
    @Override
    public Billing findById(Long id) {
        return getSession().createQuery(
                "select b from Billing b join fetch b.appointment a join fetch a.patient join fetch a.doctor d join fetch d.user where b.id = :id",
                Billing.class)
                .setParameter("id", id)
                .uniqueResult();
    }

    @Override
    public List<Billing> findAll() {
        return getSession().createQuery(
                "select b from Billing b join fetch b.appointment a join fetch a.patient join fetch a.doctor d join fetch d.user order by b.generatedAt desc",
                Billing.class)
                .list();
    }

    @Override
    public BigDecimal calculateTax(BigDecimal baseAmount) {
        Session session = getSession();
        BigDecimal[] result = new BigDecimal[1];
        session.doWork((Connection connection) -> {
            try (CallableStatement callableStatement = connection.prepareCall("{call calculate_tax(?, @smartclinic_calculated_tax)}")) {
                callableStatement.setBigDecimal(1, baseAmount);
                callableStatement.execute();
            }
            try (Statement statement = connection.createStatement();
                    ResultSet rs = statement.executeQuery("select @smartclinic_calculated_tax")) {
                if (rs.next()) {
                    result[0] = rs.getBigDecimal(1);
                }
            }
        });
        return result[0];
    }
}
