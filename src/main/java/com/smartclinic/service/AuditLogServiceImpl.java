package com.smartclinic.service;

import com.smartclinic.dao.AuditLogDao;
import com.smartclinic.model.AuditLog;
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
    public List<AuditLog> findAll() {
        return auditLogDao.findAll();
    }
}
