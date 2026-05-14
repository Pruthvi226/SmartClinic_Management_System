package com.smartclinic.dao;

import com.smartclinic.model.SystemSetting;

public interface SystemSettingDao extends GenericDao<SystemSetting, Long> {
    SystemSetting findByKey(String key);
}
