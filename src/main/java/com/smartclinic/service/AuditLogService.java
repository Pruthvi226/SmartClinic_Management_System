package com.smartclinic.service;
import com.smartclinic.model.AuditLog;
import java.util.List;
public interface AuditLogService {
    void save(AuditLog log);
    List<AuditLog> findAll();
}
