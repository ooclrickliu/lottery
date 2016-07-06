/**
 * AppProperties.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 13, 2015
 */
package cn.wisdom.lottery.payment.service;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.wisdom.lottery.payment.common.exception.OVTRuntimeException;
import cn.wisdom.lottery.payment.common.log.Logger;
import cn.wisdom.lottery.payment.common.log.LoggerFactory;
import cn.wisdom.lottery.payment.common.utils.CollectionUtils;
import cn.wisdom.lottery.payment.common.utils.DataConvertUtils;
import cn.wisdom.lottery.payment.dao.AppPropertiesDao;
import cn.wisdom.lottery.payment.dao.vo.AppProperty;

/**
 * AppProperties
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
@Service
public class AppPropertiesServiceImpl implements AppPropertiesService
{
    @Autowired
    private AppPropertiesDao appPropertiesDao;

    private static final String COOKIE_ACCESS_AGE_STRING = "cookie.access_token.age";

    private static final String RESOURCE_MANAGE_URL_STRING = "resource.manager.url";

    private static final String PAYMENT_SERVICE_URL_STRING = "payment.service.url";

    // pattern: 扣款费率,最大值,最小值 e.g. 0.003,10,0
    private static final String PAYMENT_REFUND_DEDUCT_FEE_STRING = "payment.refund.deduct.fee";

    private static final String DEBUG_MODE_STRING = "debugMode";

    private static final String DEBUG_PAY_STRING = "debugPay";

    private static final String BOOLEAN_TRUE_STRING = "true";

    private Map<String, AppProperty> appPropertiesMap;

    private Logger logger = LoggerFactory.getLogger(AppPropertiesServiceImpl.class.getName());

    public int getCookieAccessTokenAge()
    {
        AppProperty property = getAppPropertiesMap().get(COOKIE_ACCESS_AGE_STRING);
        return property == null ? 0 : DataConvertUtils.toInt(property.getPropValue());
    }

    public String getResourceManagerUrl()
    {
        AppProperty property = getAppPropertiesMap().get(RESOURCE_MANAGE_URL_STRING);
        return property == null ? "" : property.getPropValue();
    }

    public String getPaymentServiceUrl()
    {
        AppProperty property = getAppPropertiesMap().get(PAYMENT_SERVICE_URL_STRING);
        return property == null ? "" : property.getPropValue();
    }

    public boolean isDebugMode()
    {      
        AppProperty property = getAppPropertiesMap().get(DEBUG_MODE_STRING);
        return property == null ? false : property.getPropValue().equalsIgnoreCase(
                BOOLEAN_TRUE_STRING);
    }

    public boolean isDebugPay()
    {
        AppProperty property = getAppPropertiesMap().get(DEBUG_PAY_STRING);
        return property == null ? false : property.getPropValue().equalsIgnoreCase(
                BOOLEAN_TRUE_STRING);
    }

    public String getRefundDeductFee()
    {
        AppProperty property = getAppPropertiesMap().get(PAYMENT_REFUND_DEDUCT_FEE_STRING);
        return property == null ? "" : property.getPropValue();
    }

    @PostConstruct
    private void init()
    {
        try
        {
            appPropertiesMap = appPropertiesDao.getAppProperties();
        }
        catch (OVTRuntimeException e)
        {
            logger.error("Failed to get app properties.", e);
        }
    }

    public Map<String, AppProperty> getAppPropertiesMap()
    {
        if (CollectionUtils.isEmpty(appPropertiesMap))
        {
            init();
        }

        return appPropertiesMap;
    }

    public void setAppPropertiesMap(Map<String, AppProperty> appPropertiesMap)
    {
        this.appPropertiesMap = appPropertiesMap;
    }
}
