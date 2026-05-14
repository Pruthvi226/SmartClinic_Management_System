package com.smartclinic.dao;

import com.smartclinic.model.SystemSetting;
import org.springframework.stereotype.Repository;

@Repository
public class SystemSettingDaoImpl extends GenericDaoImpl<SystemSetting, Long> implements SystemSettingDao {
    @Override
    public SystemSetting findByKey(String key) {
        return getSession().createQuery("from SystemSetting s where s.settingKey = :key", SystemSetting.class)
                .setParameter("key", key)
                .uniqueResult();
    }
}
