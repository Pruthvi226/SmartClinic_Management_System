package com.smartclinic.service;
import com.smartclinic.model.AuditLog;
import com.smartclinic.model.User;
import java.util.List;
public interface AuditLogService {
    void save(AuditLog log);
    void logAction(User user, String action, String entityType, Long entityId, String ipAddress);
    List<AuditLog> findAll();
    List<AuditLog> findByEntity(String entityType, Long entityId);
}
