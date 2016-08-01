/**
 * AppClientType.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 6, 2015
 */
package cn.wisdom.lottery.dao.constant;

import cn.wisdom.lottery.common.utils.StringUtils;

/**
 * AppClientType
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See 
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public enum AppClientType
{
    WEB, IOS, ANDROID;
    
    public static boolean isWeb(String clientType)
    {
        if (StringUtils.isBlank(clientType))
        {
            return false;
        }
        
        AppClientType appClientType = AppClientType.valueOf(clientType.toUpperCase());

        return appClientType == WEB;
    }
    
    public static boolean isMobile(String clientType)
    {
        if (StringUtils.isBlank(clientType))
        {
            return false;
        }
        
        AppClientType appClientType = AppClientType.valueOf(clientType.toUpperCase());
        return appClientType == IOS || appClientType == ANDROID;
    }
}
