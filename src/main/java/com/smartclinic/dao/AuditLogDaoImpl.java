package com.smartclinic.dao;
import com.smartclinic.model.AuditLog;
import org.springframework.stereotype.Repository;
@Repository
public class AuditLogDaoImpl extends GenericDaoImpl<AuditLog, Long> implements AuditLogDao {
}
