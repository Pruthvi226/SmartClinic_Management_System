package com.smartclinic.service;

import com.smartclinic.dao.SystemSettingDao;
import com.smartclinic.model.SystemSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class SystemSettingServiceImpl implements SystemSettingService {
    @Autowired
    private SystemSettingDao systemSettingDao;

    @Override
    public List<SystemSetting> findAll() {
        return systemSettingDao.findAll();
    }

    @Override
    public String getValue(String key, String defaultValue) {
        SystemSetting setting = systemSettingDao.findByKey(key);
        return setting == null || setting.getSettingValue() == null ? defaultValue : setting.getSettingValue();
    }

    @Override
    public BigDecimal getDecimal(String key, BigDecimal defaultValue) {
        try {
            return new BigDecimal(getValue(key, defaultValue.toPlainString()));
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    @Override
    public void save(String key, String value, String description) {
        SystemSetting setting = systemSettingDao.findByKey(key);
        if (setting == null) {
            setting = new SystemSetting();
            setting.setSettingKey(key);
        }
        setting.setSettingValue(value == null ? "" : value.trim());
        setting.setDescription(description);
        systemSettingDao.save(setting);
    }
}
