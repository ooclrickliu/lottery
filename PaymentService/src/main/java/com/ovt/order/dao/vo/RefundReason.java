/**
 * RefundReason.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月21日
 */
package com.ovt.order.dao.vo;

/**
 * RefundReason
 * 
 * @Author brad.zhou
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class RefundReason extends BaseEntity
{
    private String refundReason;

    /**
     * @return the refundReason
     */
    public String getRefundReason()
    {
        return refundReason;
    }

    /**
     * @param refundReason the refundReason to set
     */
    public void setRefundReason(String refundReason)
    {
        this.refundReason = refundReason;
    }

    @Override
    public String toString()
    {
        return "RefundReason [refundReason=" + refundReason + "]";
    }
}
