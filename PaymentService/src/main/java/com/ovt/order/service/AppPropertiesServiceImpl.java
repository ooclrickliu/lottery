package com.ovt.order.service;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ovt.common.log.Logger;
import com.ovt.common.log.LoggerFactory;
import com.ovt.common.exception.OVTRuntimeException;
import com.ovt.order.dao.AppPropertiesDao;
import com.ovt.order.dao.vo.AppProperty;

@Service
public class AppPropertiesServiceImpl implements AppPropertiesService
{
    @Autowired
    private AppPropertiesDao appPropertiesDao;

    private static final String ALIPAY_PARTENER_STRING = "alipay.partner";

    private static final String ALIPAY_PRIVATE_KEY_STRING = "alipay.private_key";

    private static final String ALIPAY_INPUT_CHARSET_STRING = "alipay.input_charset";

    private static final String ALIPAY_SIGN_TYPE_STRING = "alipay.sign_type";

    private static final String ALIPAY_SELLER_EMAIL_STRING = "alipay.seller_email";

    private static final String ALIPAY_REFUND_NOTIFY_STRING = "alipay.refund_notify_url";

    private static final String ALIPAY_TRANSFER_NOTIFY_STRING = "alipay.transfer_notify_url";

    private static final String DOORBELL_SERVER_STRING = "doorbell.server_url";
    
    private static final String ALIPAY_DEADLINE_STRING = "payment.pay_deadline";
    
    private Map<String, AppProperty> appPropertiesMap;
    
    private Logger logger = LoggerFactory.getLogger(AppPropertiesServiceImpl.class.getName());
    
    public String getAlipayPartener()
    {
        AppProperty property = getAppPropertiesMap().get(ALIPAY_PARTENER_STRING);
        return property == null ? "" : property.getPropValue();
    }

    public String getAlipayPrivateKey()
    {
        AppProperty property = getAppPropertiesMap().get(ALIPAY_PRIVATE_KEY_STRING);
        return property == null ? "" : property.getPropValue();
    }

    public String getAlipayInputCharset()
    {
        AppProperty property = getAppPropertiesMap().get(ALIPAY_INPUT_CHARSET_STRING);
        return property == null ? "" : property.getPropValue();
    }

    public String getAlipaySignType()
    {
        AppProperty property = getAppPropertiesMap().get(ALIPAY_SIGN_TYPE_STRING);
        return property == null ? "" : property.getPropValue();
    }

    public String getAlipaySellerEmail()
    {
        AppProperty property = getAppPropertiesMap().get(ALIPAY_SELLER_EMAIL_STRING);
        return property == null ? "" : property.getPropValue();
    }

    public String getAlipayRefundNotifyUrl()
    {
        AppProperty property = getAppPropertiesMap().get(ALIPAY_REFUND_NOTIFY_STRING);
        return property == null ? "" : property.getPropValue();
    }

    public String getAlipayTransferNotifyUrl()
    {
        AppProperty property = getAppPropertiesMap().get(ALIPAY_TRANSFER_NOTIFY_STRING);
        return property == null ? "" : property.getPropValue();
    }

    public String getDoorbellServer()
    {
        AppProperty property = getAppPropertiesMap().get(DOORBELL_SERVER_STRING);
        return property == null ? "" : property.getPropValue();
    }
    
    @Override
    public String getAlipayPayDeadline()
    {
        AppProperty property = getAppPropertiesMap().get(ALIPAY_DEADLINE_STRING);
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
        if(CollectionUtils.isEmpty(appPropertiesMap))
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
