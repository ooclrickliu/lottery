/**
 * BaseEntity.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 14, 2015
 */
package cn.wisdom.lottery.payment.dao.vo;

import java.sql.Timestamp;

import cn.wisdom.lottery.payment.common.exception.OVTException;
import cn.wisdom.lottery.payment.common.utils.JsonUtils;
import cn.wisdom.lottery.payment.dao.annotation.Column;

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

	@Column("id")
    protected long id;

	@Column("create_time")
    protected Timestamp createTime;

	@Column("update_time")
    protected Timestamp updateTime;

	@Column("update_by")
    protected int updateBy;

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

    public int getUpdateBy()
    {
        return updateBy;
    }

    public void setUpdateBy(int updateBy)
    {
        this.updateBy = updateBy;
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

	@Override
	public String toString() {
		String json = "";
		try {
			json = JsonUtils.toJson(this);
		} catch (OVTException e) {
			json = super.toString();
		}
		
		return json;
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