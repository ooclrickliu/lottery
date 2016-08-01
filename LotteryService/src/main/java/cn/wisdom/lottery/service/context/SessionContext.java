/**
 * SessionContext.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 4, 2015
 */
package cn.wisdom.lottery.service.context;

import cn.wisdom.lottery.dao.vo.User;

/**
 * SessionContext
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See 
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class SessionContext
{
    private static ThreadLocal<User> currentUser = new ThreadLocal<User>();
    
    /**
     * @param user the currentUser to set.
     */
    public static void setCurrentUser(User user)
    {
        currentUser.set(user);
    }
    
    /**
     * @return the currentUser.
     */
    public static User getCurrentUser()
    {
        return currentUser.get();
    }
    
    /**
     * remove current user to avoid memory leak.
     */
    public static void destroy()
    {
        currentUser.remove();
    }
}
