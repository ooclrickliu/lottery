/**
 * AliRefundLog.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月21日
 */
package com.ovt.order.util.entity;

import java.sql.Timestamp;

/**
 * AliRefundLog
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class AliRefundLog extends RefundLog
{
    private String batchNo;

    private Timestamp notifyTime;

    private String notifyType;

    private String notifyId;

    private String signType;

    private String sign;
    
    private String successNum;
    
    private String resultDetails;
    
    public String getBatchNo()
    {
        return batchNo;
    }

    public void setBatchNo(String batchNo)
    {
        this.batchNo = batchNo;
    }

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

    public String getSuccessNum()
    {
        return successNum;
    }

    public void setSuccessNum(String successNum)
    {
        this.successNum = successNum;
    }

    public String getResultDetails()
    {
        return resultDetails;
    }

    public void setResultDetails(String resultDetails)
    {
        this.resultDetails = resultDetails;
    }

    @Override
    public String toString()
    {
        return "AliRefundLog [batchNo=" + batchNo + ", notifyTime="
                + notifyTime + ", notifyType=" + notifyType + ", notifyId="
                + notifyId + ", signType=" + signType + ", sign=" + sign
                + ", successNum=" + successNum + ", resultDetails="
                + resultDetails + ", id=" + id + ", createTime=" + createTime
                + ", updateTime=" + updateTime + ", isDelete=" + isDelete + "]";
    }

}
