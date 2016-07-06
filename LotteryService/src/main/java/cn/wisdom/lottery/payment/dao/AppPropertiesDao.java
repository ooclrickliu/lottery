/**
 * AppProperties.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * 2016-2-4
 */
package cn.wisdom.lottery.payment.dao;

import java.util.Map;

import cn.wisdom.lottery.payment.dao.vo.AppProperty;

/**
 * AppProperties
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See 
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public interface AppPropertiesDao
{
    public Map<String, AppProperty> getAppProperties();
}
