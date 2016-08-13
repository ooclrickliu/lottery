/**
 * AccessToken.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 6, 2015
 */
package cn.wisdom.lottery.dao.vo;

import java.sql.Date;

import cn.wisdom.lottery.dao.annotation.Column;


/**
 * AccessToken
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class AccessToken extends BaseEntity
{

	@Column("user_id")
    private long userId;

	@Column("access_token")
    private String accessToken;

	@Column("client_type")
    private String clientType;

	@Column("expire_time")
    private Date expireTime;
	
	@Column("is_delete")
	private boolean delete;
    
    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }

    public String getClientType()
    {
        return clientType;
    }

    public void setClientType(String clientType)
    {
        this.clientType = clientType;
    }

    public Date getExpireTime()
    {
        return expireTime;
    }

    public void setExpireTime(Date expireTime)
    {
        this.expireTime = expireTime;
    }

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public boolean isDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}


    
}
