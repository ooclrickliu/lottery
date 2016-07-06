package com.ovt.order.dao.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "AccountQueryAccountLogVO")
@XmlAccessorType(XmlAccessType.FIELD)
public class AccountQueryAccountLog
{
    @XmlElement(name = "balance")
    private String balance;
    
    @XmlElement(name = "buyer_account")
    private String buyerAccount;
    
    @XmlElement(name = "currency")
    private String currency;
    
    @XmlElement(name = "deposit_bank_no")
    private String depositBankNo;
    
    @XmlElement(name = "goods_title")
    private String goodsTitle;
    
    @XmlElement(name = "income")
    private String income;
    
    @XmlElement(name = "iw_account_log_id")
    private String iwAccountLogId;
    
    @XmlElement(name = "memo")
    private String memo;
    
    @XmlElement(name = "merchant_out_order_no")
    private String merchantOutOrderNo;
    
    @XmlElement(name = "outcome")
    private String outcome;
    
    @XmlElement(name = "partner_id")
    private String partnerId;
    
    @XmlElement(name = "rate")
    private String rate;
    
    @XmlElement(name = "seller_account")
    private String sellerAccount;
    
    @XmlElement(name = "seller_full_name")
    private String sellerFullName;
    
    @XmlElement(name = "service_fee")
    private String serviceFee;
    
    @XmlElement(name = "service_fee_ratio")
    private String serviceFeeRatio;
    
    @XmlElement(name = "sign_product_name")
    private String signProductName;
    
    @XmlElement(name = "sub_trans_code_msg")
    private String subTransCodeMsg;
    
    @XmlElement(name = "total_fee")
    private String totalFee;
    
    @XmlElement(name = "trade_no")
    private String tradeNo;
    
    @XmlElement(name = "trade_refund_amount")
    private String tradeRefundAmount;
    
    @XmlElement(name = "trans_code_msg")
    private String transCodeMsg;
    
    @XmlElement(name = "trans_date")
    private String transDate;

    public String getBalance()
    {
        return balance;
    }

    public void setBalance(String balance)
    {
        this.balance = balance;
    }

    public String getBuyerAccount()
    {
        return buyerAccount;
    }

    public void setBuyerAccount(String buyerAccount)
    {
        this.buyerAccount = buyerAccount;
    }

    public String getCurrency()
    {
        return currency;
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
    }

    public String getDepositBankNo()
    {
        return depositBankNo;
    }

    public void setDepositBankNo(String depositBankNo)
    {
        this.depositBankNo = depositBankNo;
    }

    public String getGoodsTitle()
    {
        return goodsTitle;
    }

    public void setGoodsTitle(String goodsTitle)
    {
        this.goodsTitle = goodsTitle;
    }

    public String getIncome()
    {
        return income;
    }

    public void setIncome(String income)
    {
        this.income = income;
    }

    public String getIwAccountLogId()
    {
        return iwAccountLogId;
    }

    public void setIwAccountLogId(String iwAccountLogId)
    {
        this.iwAccountLogId = iwAccountLogId;
    }

    public String getMemo()
    {
        return memo;
    }

    public void setMemo(String memo)
    {
        this.memo = memo;
    }

    public String getMerchantOutOrderNo()
    {
        return merchantOutOrderNo;
    }

    public void setMerchantOutOrderNo(String merchantOutOrderNo)
    {
        this.merchantOutOrderNo = merchantOutOrderNo;
    }

    public String getOutcome()
    {
        return outcome;
    }

    public void setOutcome(String outcome)
    {
        this.outcome = outcome;
    }

    public String getPartnerId()
    {
        return partnerId;
    }

    public void setPartnerId(String partnerId)
    {
        this.partnerId = partnerId;
    }

    public String getRate()
    {
        return rate;
    }

    public void setRate(String rate)
    {
        this.rate = rate;
    }

    public String getSellerAccount()
    {
        return sellerAccount;
    }

    public void setSellerAccount(String sellerAccount)
    {
        this.sellerAccount = sellerAccount;
    }

    public String getSellerFullName()
    {
        return sellerFullName;
    }

    public void setSellerFullName(String sellerFullName)
    {
        this.sellerFullName = sellerFullName;
    }

    public String getServiceFee()
    {
        return serviceFee;
    }

    public void setServiceFee(String serviceFee)
    {
        this.serviceFee = serviceFee;
    }

    public String getServiceFeeRatio()
    {
        return serviceFeeRatio;
    }

    public void setServiceFeeRatio(String serviceFeeRatio)
    {
        this.serviceFeeRatio = serviceFeeRatio;
    }

    public String getSignProductName()
    {
        return signProductName;
    }

    public void setSignProductName(String signProductName)
    {
        this.signProductName = signProductName;
    }

    public String getSubTransCodeMsg()
    {
        return subTransCodeMsg;
    }

    public void setSubTransCodeMsg(String subTransCodeMsg)
    {
        this.subTransCodeMsg = subTransCodeMsg;
    }

    public String getTotalFee()
    {
        return totalFee;
    }

    public void setTotalFee(String totalFee)
    {
        this.totalFee = totalFee;
    }

    public String getTradeNo()
    {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo)
    {
        this.tradeNo = tradeNo;
    }

    public String getTradeRefundAmount()
    {
        return tradeRefundAmount;
    }

    public void setTradeRefundAmount(String tradeRefundAmount)
    {
        this.tradeRefundAmount = tradeRefundAmount;
    }

    public String getTransCodeMsg()
    {
        return transCodeMsg;
    }

    public void setTransCodeMsg(String transCodeMsg)
    {
        this.transCodeMsg = transCodeMsg;
    }

    public String getTransDate()
    {
        return transDate;
    }

    public void setTransDate(String transDate)
    {
        this.transDate = transDate;
    }

}
