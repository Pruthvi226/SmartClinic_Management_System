package com.smartclinic.service;

import com.smartclinic.dao.AuditLogDao;
import com.smartclinic.model.AuditLog;
import com.smartclinic.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AuditLogServiceImpl implements AuditLogService {

    @Autowired
    private AuditLogDao auditLogDao;

    @Override
    public void save(AuditLog log) {
        auditLogDao.save(log);
    }

    @Override
    public void logAction(User user, String action, String entityType, Long entityId, String ipAddress) {
        AuditLog log = new AuditLog();
        log.setUser(user);
        log.setAction(action);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setIpAddress(ipAddress);
        auditLogDao.save(log);
    }

    @Override
    public List<AuditLog> findAll() {
        return auditLogDao.findAll();
    }

    @Override
    public List<AuditLog> findByEntity(String entityType, Long entityId) {
        return auditLogDao.findByEntity(entityType, entityId);
    }
}
