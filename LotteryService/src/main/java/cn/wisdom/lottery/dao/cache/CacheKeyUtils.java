/**
 * MemcacheKeyUtils.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * Apr 8, 2016
 */
package cn.wisdom.lottery.dao.cache;

import java.text.MessageFormat;

/**
 * MemcacheKeyUtils
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See 
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class CacheKeyUtils
{
    private static final String KEY_PATTERN_MEMBER_PROFILE = "MP:{0}";
    
    private static final String KEY_PATTERN_MEMBER_ACTION = "D:{0}_MP:{1}";
    
    private static final String KEY_PATTERN_MEMBER_ACTION_BY_DEVICE = "D:{0}_MP:*";
    
    public static String buildMemberProfileKey(String deviceId)
    {
        return MessageFormat.format(KEY_PATTERN_MEMBER_PROFILE, deviceId);
    }
    
    public static String buildMemberActionKey(String deviceId)
    {
        return MessageFormat.format(KEY_PATTERN_MEMBER_ACTION_BY_DEVICE, deviceId);
    }
    
    public static String buildMemberActionKey(String deviceId, String profileId)
    {
        return MessageFormat.format(KEY_PATTERN_MEMBER_ACTION, deviceId, profileId);
    }
}
