/**
 * RefundRequest.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月21日
 */
package com.ovt.order.dao.vo;

import com.ovt.order.dao.constant.OrderState;

/**
 * RefundRequest
 * 
 * @Author brad.zhou
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class RefundRequest extends BaseEntity
{
    private String orderNo;

    private String createBy;

    private String refundReason;

    private float refundFee;

    private String refundDesc;

    private OrderState refundState;

    private long auditorId;

    private String feedback;
    
    private String batchNo;

    private String refundLog;

    public RefundRequest()
    {
        super();
    }

    public RefundRequest(long id, String feedback)
    {
        this.id = id;
        this.feedback = feedback;
    }

    public RefundRequest(String orderNo, String createBy, String refundReason,
            float refundFee, String refundDesc)
    {
        this.orderNo = orderNo;
        this.createBy = createBy;
        this.refundReason = refundReason;
        this.refundFee = refundFee;
        this.refundDesc = refundDesc;
    }

    /**
     * @return the orderNo
     */
    public String getOrderNo()
    {
        return orderNo;
    }

    /**
     * @param orderNo the orderNo to set
     */
    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    /**
     * @return the createBy
     */
    public String getCreateBy()
    {
        return createBy;
    }

    /**
     * @param createBy the createBy to set
     */
    public void setCreateBy(String createBy)
    {
        this.createBy = createBy;
    }

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

    /**
     * @return the refundFee
     */
    public float getRefundFee()
    {
        return refundFee;
    }

    /**
     * @param refundFee the refundFee to set
     */
    public void setRefundFee(float refundFee)
    {
        this.refundFee = refundFee;
    }

    /**
     * @return the refundDesc
     */
    public String getRefundDesc()
    {
        return refundDesc;
    }

    /**
     * @param refundDesc the refundDesc to set
     */
    public void setRefundDesc(String refundDesc)
    {
        this.refundDesc = refundDesc;
    }

    /**
     * @return the refundState
     */
    public OrderState getRefundState()
    {
        return refundState;
    }

    /**
     * @param refundState the refundState to set
     */
    public void setRefundState(OrderState refundState)
    {
        this.refundState = refundState;
    }

    /**
     * @return the auditorId
     */
    public long getAuditorId()
    {
        return auditorId;
    }

    /**
     * @param auditorId the auditorId to set
     */
    public void setAuditorId(long auditorId)
    {
        this.auditorId = auditorId;
    }

    /**
     * @return the feedback
     */
    public String getFeedback()
    {
        return feedback;
    }

    /**
     * @param feedback the feedback to set
     */
    public void setFeedback(String feedback)
    {
        this.feedback = feedback;
    }

    public String getBatchNo()
    {
        return batchNo;
    }

    public void setBatchNo(String batchNo)
    {
        this.batchNo = batchNo;
    }

    public String getRefundLog()
    {
        return refundLog;
    }

    public void setRefundLog(String refundLog)
    {
        this.refundLog = refundLog;
    }

    @Override
    public String toString()
    {
        return "RefundRequest [orderNo=" + orderNo + ", createBy=" + createBy + ", refundReason="
                + refundReason + ", refundFee=" + refundFee + ", refundDesc=" + refundDesc
                + ", refundState=" + refundState + ", auditorId=" + auditorId + ", feedback="
                + feedback + ", batchNo=" + batchNo + ", refundLog=" + refundLog + ", id=" + id
                + ", createTime=" + createTime + ", updateTime=" + updateTime + ", isDelete="
                + isDelete + "]";
    }
}
