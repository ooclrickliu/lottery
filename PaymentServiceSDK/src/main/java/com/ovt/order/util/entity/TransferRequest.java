/**
 * TransferRequest.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月21日
 */
package com.ovt.order.util.entity;

/**
 * TransferRequest
 * 
 * @Author brad.zhou
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class TransferRequest extends BaseEntity
{
    private String receiverAccount;

    private String receiverName;

    private String transRequestReason;

    private float transRequestFee;

    private String transRequestState;

    /**
     * @return the receiverAccount
     */
    public String getReceiverAccount()
    {
        return receiverAccount;
    }

    /**
     * @param receiverAccount the receiverAccount to set
     */
    public void setReceiverAccount(String receiverAccount)
    {
        this.receiverAccount = receiverAccount;
    }

    /**
     * @return the receiverName
     */
    public String getReceiverName()
    {
        return receiverName;
    }

    /**
     * @param receiverName the receiverName to set
     */
    public void setReceiverName(String receiverName)
    {
        this.receiverName = receiverName;
    }

    /**
     * @return the transRequestReason
     */
    public String getTransRequestReason()
    {
        return transRequestReason;
    }

    /**
     * @param transRequestReason the transRequestReason to set
     */
    public void setTransRequestReason(String transRequestReason)
    {
        this.transRequestReason = transRequestReason;
    }

    /**
     * @return the transRequestFee
     */
    public float getTransRequestFee()
    {
        return transRequestFee;
    }

    /**
     * @param transRequestFee the transRequestFee to set
     */
    public void setTransRequestFee(float transRequestFee)
    {
        this.transRequestFee = transRequestFee;
    }

    /**
     * @return the transRequestState
     */
    public String getTransRequestState()
    {
        return transRequestState;
    }

    /**
     * @param transRequestState the transRequestState to set
     */
    public void setTransRequestState(String transRequestState)
    {
        this.transRequestState = transRequestState;
    }

    @Override
    public String toString()
    {
        return "TransferRequest [receiverAccount=" + receiverAccount
                + ", receiverName=" + receiverName + ", transRequestReason="
                + transRequestReason + ", transRequestFee=" + transRequestFee
                + ", transRequestState=" + transRequestState + "]";
    }

}
