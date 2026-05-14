package com.smartclinic.dao;
import com.smartclinic.model.AuditLog;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public class AuditLogDaoImpl extends GenericDaoImpl<AuditLog, Long> implements AuditLogDao {
    @Override
    public List<AuditLog> findAll() {
        return getSession().createQuery(
                "select l from AuditLog l left join fetch l.user order by l.timestamp desc",
                AuditLog.class)
                .list();
    }
}
