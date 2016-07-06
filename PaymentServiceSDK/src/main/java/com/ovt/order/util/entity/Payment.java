/**
 * Payment.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月18日
 */
package com.ovt.order.util.entity;

import java.sql.Timestamp;

/**
 * Payment
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See 
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class Payment extends BaseEntity
{   
    private String orderNo;
    
    private String payNo;
    
    private String payType;
    
    private String paySource;
    
    private float payFee;
    
    private String payState;
    
    private Timestamp payTime;
    
    private long payLogId;

    public String getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    public String getPayNo()
    {
        return payNo;
    }

    public void setPayNo(String payNo)
    {
        this.payNo = payNo;
    }

    public String getPayType()
    {
        return payType;
    }

    public void setPayType(String payType)
    {
        this.payType = payType;
    }

    public String getPaySource()
    {
        return paySource;
    }

    public void setPaySource(String paySource)
    {
        this.paySource = paySource;
    }

    public float getPayFee()
    {
        return payFee;
    }

    public void setPayFee(float payFee)
    {
        this.payFee = payFee;
    }

    public String getPayState()
    {
        return payState;
    }

    public void setPayState(String payState)
    {
        this.payState = payState;
    }

    public Timestamp getPayTime()
    {
        return payTime;
    }

    public void setPayTime(Timestamp payTime)
    {
        this.payTime = payTime;
    }

    public long getPayLogId()
    {
        return payLogId;
    }

    public void setPayLogId(long payLogId)
    {
        this.payLogId = payLogId;
    }

    @Override
    public String toString()
    {
        return "Payment [orderNo=" + orderNo + ", payNo=" + payNo
                + ", payType=" + payType + ", paySource=" + paySource
                + ", payFee=" + payFee + ", payState=" + payState
                + ", payTime=" + payTime + ", payLogId=" + payLogId + ", id="
                + id + ", createTime=" + createTime + ", updateTime="
                + updateTime + ", isDelete=" + isDelete + "]";
    }
    
}
