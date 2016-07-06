/**
 * AppPropertiesDaoImpl.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * 2016-2-4
 */
package cn.wisdom.lottery.payment.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.wisdom.lottery.payment.common.utils.CollectionUtils;
import cn.wisdom.lottery.payment.dao.mapper.AppPropertyMapper;
import cn.wisdom.lottery.payment.dao.vo.AppProperty;

/**
 * AppPropertiesDaoImpl
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
@Repository
public class AppPropertiesDaoImpl implements AppPropertiesDao
{
    @Autowired
    private DaoHelper daoHelper;

    @Autowired
    private AppPropertyMapper appPropertyMapper;

    private static final String SQL_GET_APP_PROPERTIES = "SELECT id, prop_name, prop_value, `desc`"
            + " from app_properties";

    /*
     * (non-Javadoc)
     * 
     * @see cn.wisdom.lottery.payment.dao.AppPropertiesDao#getAppProperties()
     */
    @Override
    public Map<String, AppProperty> getAppProperties()
    {
        Map<String, AppProperty> appPropertiesMap = new HashMap<String, AppProperty>();
        String errMsg = "Failed to get app properties from database";

        List<AppProperty> appProperties = daoHelper.queryForList(SQL_GET_APP_PROPERTIES,
                appPropertyMapper, errMsg);
        if (CollectionUtils.isNotEmpty(appProperties))
        {
            for (AppProperty appProperty : appProperties)
            {
                appPropertiesMap.put(appProperty.getPropName(), appProperty);
            }
        }

        return appPropertiesMap;
    }

}
