/**
 * AccessToken.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 6, 2015
 */
package cn.wisdom.lottery.payment.dao.vo;

import java.sql.Timestamp;


/**
 * AccessToken
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class UserToken extends BaseEntity
{

    private long userId;
    
    private String token;
    
    private Timestamp expireTime;
    
    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public Timestamp getExpireTime()
    {
        return expireTime;
    }

    public void setExpireTime(Timestamp expireTime)
    {
        this.expireTime = expireTime;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("AccessToken [userId=").append(userId)
                .append(", token=").append(token).append(", clientType=")
                .append(", expireTime=").append(expireTime)
                .append(", id=").append(id).append(", createTime=")
                .append(createTime).append(", updateTime=").append(updateTime).append("]");
        return builder.toString();
    }

    
}
