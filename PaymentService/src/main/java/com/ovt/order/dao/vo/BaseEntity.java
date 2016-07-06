/**
 * BaseEntity.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 14, 2015
 */
package com.ovt.order.dao.vo;

import java.sql.Timestamp;

/**
 * BaseEntity
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class BaseEntity
{

    protected long id;
    
    protected Timestamp createTime = null;
    
    protected Timestamp updateTime = null;
    
    protected boolean isDelete;

    public BaseEntity()
    {
        super();
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public boolean isDelete()
    {
        return isDelete;
    }

    public void setDelete(boolean isDelete)
    {
        this.isDelete = isDelete;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }

        if (!(obj instanceof BaseEntity))
        {
            return false;
        }
        BaseEntity other = (BaseEntity) obj;
        if (id != other.id)
        {
            return false;
        }
        return true;
    }

    public Timestamp getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime)
    {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime)
    {
        this.updateTime = updateTime;
    }

}