package com.smartclinic.service;

import com.smartclinic.model.SystemSetting;
import java.math.BigDecimal;
import java.util.List;

public interface SystemSettingService {
    List<SystemSetting> findAll();
    String getValue(String key, String defaultValue);
    BigDecimal getDecimal(String key, BigDecimal defaultValue);
    void save(String key, String value, String description);
}
