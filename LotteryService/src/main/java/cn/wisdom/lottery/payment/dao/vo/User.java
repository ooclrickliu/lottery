/**
 * User.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 4, 2015
 */
package cn.wisdom.lottery.payment.dao.vo;

import java.util.List;

/**
 * User2
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[DAO] 1.0
 */
public class User extends BaseEntity
{

    private String userName;

    private String password;
    
    private List<Permission> permission;
    
    public List<Permission> getPermission()
    {
        return permission;
    }

    public void setPermission(List<Permission> permission)
    {
        this.permission = permission;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("User [userName=").append(userName)
                .append(", password=").append(password)
                .append(", userId=").append(id)
                .append(", createTime=").append(createTime)
                .append(", updateTime=").append(updateTime);
        return builder.toString();
    }
}
