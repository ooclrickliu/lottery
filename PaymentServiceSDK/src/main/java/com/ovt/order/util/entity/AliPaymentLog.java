/**
 * AliPaymentLog.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月21日
 */
package com.ovt.order.util.entity;

import java.sql.Timestamp;

/**
 * AliPaymentLog
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See 
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class AliPaymentLog extends PaymentLog
{
    private Timestamp notifyTime;
    
    private String notifyType;
    
    private String notifyId;
    
    private String signType;
    
    private String sign;
    
    private String outTradeNo;
    
    private String subject;
    
    private String tradeNo;
    
    private String tradeStatus;
    
    private String buyerId;
    
    private String buyerEmail;
    
    private float totalFee;
    
    private Timestamp gmtCreate;

    private Timestamp gmtPayment;
    
    private String isTotalFeeAdjust;
    
    private String useCoupon;
    
    private float discount;
    
    private String refundStatus;
    
    private Timestamp gmtRefund;

    public Timestamp getNotifyTime()
    {
        return notifyTime;
    }

    public void setNotifyTime(Timestamp notifyTime)
    {
        this.notifyTime = notifyTime;
    }

    public String getNotifyType()
    {
        return notifyType;
    }

    public void setNotifyType(String notifyType)
    {
        this.notifyType = notifyType;
    }

    public String getNotifyId()
    {
        return notifyId;
    }

    public void setNotifyId(String notifyId)
    {
        this.notifyId = notifyId;
    }

    public String getSignType()
    {
        return signType;
    }

    public void setSignType(String signType)
    {
        this.signType = signType;
    }

    public String getSign()
    {
        return sign;
    }

    public void setSign(String sign)
    {
        this.sign = sign;
    }

    public String getOutTradeNo()
    {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo)
    {
        this.outTradeNo = outTradeNo;
    }

    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public String getTradeNo()
    {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo)
    {
        this.tradeNo = tradeNo;
    }

    public String getTradeStatus()
    {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus)
    {
        this.tradeStatus = tradeStatus;
    }

    public String getBuyerId()
    {
        return buyerId;
    }

    public void setBuyerId(String buyerId)
    {
        this.buyerId = buyerId;
    }

    public String getBuyerEmail()
    {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail)
    {
        this.buyerEmail = buyerEmail;
    }

    public float getTotalFee()
    {
        return totalFee;
    }

    public void setTotalFee(float totalFee)
    {
        this.totalFee = totalFee;
    }

    public Timestamp getGmtCreate()
    {
        return gmtCreate;
    }

    public void setGmtCreate(Timestamp gmtCreate)
    {
        this.gmtCreate = gmtCreate;
    }

    public Timestamp getGmtPayment()
    {
        return gmtPayment;
    }

    public void setGmtPayment(Timestamp gmtPayment)
    {
        this.gmtPayment = gmtPayment;
    }

    public String getIsTotalFeeAdjust()
    {
        return isTotalFeeAdjust;
    }

    public void setIsTotalFeeAdjust(String isTotalFeeAdjust)
    {
        this.isTotalFeeAdjust = isTotalFeeAdjust;
    }

    public String getUseCoupon()
    {
        return useCoupon;
    }

    public void setUseCoupon(String useCoupon)
    {
        this.useCoupon = useCoupon;
    }

    public float getDiscount()
    {
        return discount;
    }

    public void setDiscount(float discount)
    {
        this.discount = discount;
    }

    public String getRefundStatus()
    {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus)
    {
        this.refundStatus = refundStatus;
    }

    public Timestamp getGmtRefund()
    {
        return gmtRefund;
    }

    public void setGmtRefund(Timestamp gmtRefund)
    {
        this.gmtRefund = gmtRefund;
    }

    @Override
    public String toString()
    {
        return "AliPaymentLog [notifyTime=" + notifyTime + ", notifyType="
                + notifyType + ", notifyId=" + notifyId + ", signType="
                + signType + ", sign=" + sign + ", outTradeNo=" + outTradeNo
                + ", subject=" + subject + ", tradeNo=" + tradeNo
                + ", tradeStatus=" + tradeStatus + ", buyerId=" + buyerId
                + ", buyerEmail=" + buyerEmail + ", totalFee=" + totalFee
                + ", gmtCreate=" + gmtCreate + ", gmtPayment=" + gmtPayment
                + ", isTotalFeeAdjust=" + isTotalFeeAdjust + ", useCoupon="
                + useCoupon + ", discount=" + discount + ", refundStatus="
                + refundStatus + ", gmtRefund=" + gmtRefund + ", id=" + id
                + ", createTime=" + createTime + ", updateTime=" + updateTime
                + ", isDelete=" + isDelete + "]";
    }

}
