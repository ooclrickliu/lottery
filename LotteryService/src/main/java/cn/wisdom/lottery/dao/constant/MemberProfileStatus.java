/**
 * MemberProfileStatus.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * Dec 29, 2015
 */
package cn.wisdom.lottery.dao.constant;

import java.util.Date;

import cn.wisdom.lottery.common.utils.DateTimeUtils;

/**
 * MemberProfileStatus
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See 
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public enum MemberProfileStatus
{
    // End user can access below status:
    ACTIVE, CANCELLING, CANCELLED, INACTIVE,
    
    // End user can't access below status:
    EXPIRED,
    
    //Temp status
    TOACTIVE;
    
    public static boolean isCancelable(MemberProfileStatus status)
    {
        return status == ACTIVE || status == INACTIVE;
    }
    
    public static boolean isCancelling(MemberProfileStatus status)
    {
        return status == CANCELLING;
    }
    
    public static boolean isUsableProfile(MemberProfileStatus status, Date startTime)
    {
        boolean usable = (status == ACTIVE || status == CANCELLING || status == CANCELLED);
        
        return usable && DateTimeUtils.isPast(startTime);
    }
    
}
