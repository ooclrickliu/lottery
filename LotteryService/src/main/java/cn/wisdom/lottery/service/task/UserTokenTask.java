/**
 * MemberProfileTask.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * Jan 7, 2016
 */
package cn.wisdom.lottery.service.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import cn.wisdom.lottery.common.log.Logger;
import cn.wisdom.lottery.common.log.LoggerFactory;
import cn.wisdom.lottery.service.exception.ServiceException;
import cn.wisdom.lottery.service.manager.UserTokenManager;

/**
 * MemberProfileTask
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class UserTokenTask
{
    @Autowired
    private UserTokenManager userTokenManager;
    
    private Logger logger = LoggerFactory.getLogger(UserTokenTask.class.getName());

    /**
     * 01:00, 01:30 every day
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanExpireUserToken()
    {
        logger.info("Check expire user token task start!");
        
        try
        {
            userTokenManager.cleanExpiredUserToken();
        }
        catch (ServiceException e)
        {
            logger.error("Clean expired user token failed", e);
        }
        
        logger.info("Check expire user token task complete!");
    }
}
