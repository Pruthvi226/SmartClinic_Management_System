package com.smartclinic.dao;
import com.smartclinic.model.Billing;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
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
            try (CallableStatement callableStatement = connection.prepareCall("{call calculate_tax(?, ?)}")) {
                callableStatement.setBigDecimal(1, baseAmount);
                callableStatement.registerOutParameter(2, Types.DECIMAL);
                callableStatement.execute();
                result[0] = callableStatement.getBigDecimal(2);
            }
        });
        return result[0];
    }

    @Override
    public Billing findByAppointmentId(Long appointmentId) {
        return getSession().createQuery(
                "select b from Billing b join fetch b.appointment a join fetch a.patient join fetch a.doctor d join fetch d.user where a.id = :appointmentId",
                Billing.class)
                .setParameter("appointmentId", appointmentId)
                .uniqueResult();
    }
}
