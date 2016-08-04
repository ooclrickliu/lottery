package cn.wisdom.lottery.dao.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class WxPayLog
{
    @XmlElement(name = "appid")
    private String appId;

    @XmlElement(name = "bank_type")
    private String bankType;

    @XmlElement(name = "cash_fee")
    private String cashFee;

    @XmlElement(name = "fee_type")
    private String feeType;

    @XmlElement(name = "is_subscribe")
    private String isSubscribe;

    @XmlElement(name = "mch_id")
    private String mchId;

    @XmlElement(name = "nonce_str")
    private String nonceStr;

    @XmlElement(name = "openid")
    private String openId;

    @XmlElement(name = "out_trade_no")
    private String outTradeNo;

    @XmlElement(name = "result_code")
    private String resultCode;

    @XmlElement(name = "return_code")
    private String returnCode;

    @XmlElement(name = "sign")
    private String sign;

    @XmlElement(name = "time_end")
    private String timeEnd;

    @XmlElement(name = "total_fee")
    private int totalFee;

    @XmlElement(name = "trade_type")
    private String tradeType;

    @XmlElement(name = "transaction_id")
    private String transactionId;

    public String getAppId()
    {
        return appId;
    }

    public void setAppId(String appId)
    {
        this.appId = appId;
    }

    public String getBankType()
    {
        return bankType;
    }

    public void setBankType(String bankType)
    {
        this.bankType = bankType;
    }

    public String getCashFee()
    {
        return cashFee;
    }

    public void setCashFee(String cashFee)
    {
        this.cashFee = cashFee;
    }

    public String getFeeType()
    {
        return feeType;
    }

    public void setFeeType(String feeType)
    {
        this.feeType = feeType;
    }

    public String getIsSubscribe()
    {
        return isSubscribe;
    }

    public void setIsSubscribe(String isSubscribe)
    {
        this.isSubscribe = isSubscribe;
    }

    public String getMchId()
    {
        return mchId;
    }

    public void setMchId(String mchId)
    {
        this.mchId = mchId;
    }

    public String getNonceStr()
    {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr)
    {
        this.nonceStr = nonceStr;
    }

    public String getOpenId()
    {
        return openId;
    }

    public void setOpenId(String openId)
    {
        this.openId = openId;
    }

    public String getOutTradeNo()
    {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo)
    {
        this.outTradeNo = outTradeNo;
    }

    public String getResultCode()
    {
        return resultCode;
    }

    public void setResultCode(String resultCode)
    {
        this.resultCode = resultCode;
    }

    public String getReturnCode()
    {
        return returnCode;
    }

    public void setReturnCode(String returnCode)
    {
        this.returnCode = returnCode;
    }

    public String getSign()
    {
        return sign;
    }

    public void setSign(String sign)
    {
        this.sign = sign;
    }

    public String getTimeEnd()
    {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd)
    {
        this.timeEnd = timeEnd;
    }

    public int getTotalFee()
    {
        return totalFee;
    }

    public void setTotalFee(int totalFee)
    {
        this.totalFee = totalFee;
    }

    public String getTradeType()
    {
        return tradeType;
    }

    public void setTradeType(String tradeType)
    {
        this.tradeType = tradeType;
    }

    public String getTransactionId()
    {
        return transactionId;
    }

    public void setTransactionId(String transactionId)
    {
        this.transactionId = transactionId;
    }

    @Override
    public String toString()
    {
        return "WxPayLog [appId=" + appId + ", bankType=" + bankType
                + ", cashFee=" + cashFee + ", feeType=" + feeType
                + ", isSubscribe=" + isSubscribe + ", mchId=" + mchId
                + ", nonceStr=" + nonceStr + ", openId=" + openId
                + ", outTradeNo=" + outTradeNo + ", resultCode=" + resultCode
                + ", returnCode=" + returnCode + ", sign=" + sign
                + ", timeEnd=" + timeEnd + ", totalFee=" + totalFee
                + ", tradeType=" + tradeType + ", transactionId="
                + transactionId + "]";
    }

}
