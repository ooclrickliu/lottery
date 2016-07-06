/**
 * Refund.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月21日
 */
package com.ovt.order.util.entity;

import java.sql.Timestamp;

/**
 * Refund
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class Refund extends BaseEntity
{
    private long refundRequestId;

    private String orderNo;

    private String batchNo;

    private String tradeNo;

    private float refundFee;

    private String refundState;

    private float refundTax;

    private String refundTaxState;

    private Timestamp refundTime;

    private long refundLogId;

    private Timestamp createTime;

    public long getRefundRequestId()
    {
        return refundRequestId;
    }

    public void setRefundRequestId(long refundRequestId)
    {
        this.refundRequestId = refundRequestId;
    }

    public String getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    public String getBatchNo()
    {
        return batchNo;
    }

    public void setBatchNo(String batchNo)
    {
        this.batchNo = batchNo;
    }

    public String getTradeNo()
    {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo)
    {
        this.tradeNo = tradeNo;
    }

    public float getRefundFee()
    {
        return refundFee;
    }

    public void setRefundFee(float refundFee)
    {
        this.refundFee = refundFee;
    }

    public String getRefundState()
    {
        return refundState;
    }

    public void setRefundState(String refundState)
    {
        this.refundState = refundState;
    }

    public float getRefundTax()
    {
        return refundTax;
    }

    public void setRefundTax(float refundTax)
    {
        this.refundTax = refundTax;
    }

    public String getRefundTaxState()
    {
        return refundTaxState;
    }

    public void setRefundTaxState(String refundTaxState)
    {
        this.refundTaxState = refundTaxState;
    }

    public Timestamp getRefundTime()
    {
        return refundTime;
    }

    public void setRefundTime(Timestamp refundTime)
    {
        this.refundTime = refundTime;
    }

    public long getRefundLogId()
    {
        return refundLogId;
    }

    public void setRefundLogId(long refundLogId)
    {
        this.refundLogId = refundLogId;
    }

    public Timestamp getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime)
    {
        this.createTime = createTime;
    }

    @Override
    public String toString()
    {
        return "Refund [refundRequestId=" + refundRequestId + ", batchNo="
                + batchNo + ", tradeNo=" + tradeNo + ", refundFee=" + refundFee
                + ", refundState=" + refundState.toString() + ", refundTax="
                + refundTax + ", refundTaxState=" + refundTaxState
                + ", refundTime=" + refundTime + ", refundLogId=" + refundLogId
                + ", createTime=" + createTime + ", id=" + id + ", updateTime="
                + updateTime + ", isDelete=" + isDelete + "]";
    }

}
