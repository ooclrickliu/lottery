package com.ovt.order.util.entity;

public class AppAliPaymentLog extends BaseEntity
{
    private String resultStatus;
    
    private String memo;

    private String partner;

    private String sellerId;

    private String outTradeNo;

    private String subject;

    private String body;

    private String totalFee;

    private String notifyUrl;

    private String service;

    private String paymentType;

    private String inputCharset;

    private String itBPay;

    private String success;

    private String signType;

    private String sign;
    
    public static final String SUCCESS_STATUS = "9000";
    
    public static final String TRUE = "true";

    public String getResultStatus()
    {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus)
    {
        this.resultStatus = resultStatus;
    }

    public String getMemo()
    {
        return memo;
    }

    public void setMemo(String memo)
    {
        this.memo = memo;
    }

    public String getPartner()
    {
        return partner;
    }

    public void setPartner(String partner)
    {
        this.partner = partner;
    }

    public String getSellerId()
    {
        return sellerId;
    }

    public void setSellerId(String sellerId)
    {
        this.sellerId = sellerId;
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

    public String getBody()
    {
        return body;
    }

    public void setBody(String body)
    {
        this.body = body;
    }

    public String getTotalFee()
    {
        return totalFee;
    }

    public void setTotalFee(String totalFee)
    {
        this.totalFee = totalFee;
    }

    public String getNotifyUrl()
    {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl)
    {
        this.notifyUrl = notifyUrl;
    }

    public String getService()
    {
        return service;
    }

    public void setService(String service)
    {
        this.service = service;
    }

    public String getPaymentType()
    {
        return paymentType;
    }

    public void setPaymentType(String paymentType)
    {
        this.paymentType = paymentType;
    }

    public String getInputCharset()
    {
        return inputCharset;
    }

    public void setInputCharset(String inputCharset)
    {
        this.inputCharset = inputCharset;
    }

    public String getItBPay()
    {
        return itBPay;
    }

    public void setItBPay(String itBPay)
    {
        this.itBPay = itBPay;
    }

    public String getSuccess()
    {
        return success;
    }

    public void setSuccess(String success)
    {
        this.success = success;
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

}
