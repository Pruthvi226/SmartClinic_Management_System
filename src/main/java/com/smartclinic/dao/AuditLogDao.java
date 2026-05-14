package com.smartclinic.dao;
import com.smartclinic.model.AuditLog;
import java.util.List;
public interface AuditLogDao extends GenericDao<AuditLog, Long> {
    List<AuditLog> findByEntity(String entityType, Long entityId);
}
