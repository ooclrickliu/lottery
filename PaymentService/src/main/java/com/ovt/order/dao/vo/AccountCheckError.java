package com.ovt.order.dao.vo;

public class AccountCheckError extends BaseEntity
{
    private String orderNo;
    private String detail;
    private boolean isRead;

    public AccountCheckError()
    {

    }

    public AccountCheckError(String orderNo, String detail)
    {
        super();
        this.orderNo = orderNo;
        this.detail = detail;
    }

    public String getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    public String getDetail()
    {
        return detail;
    }

    public void setDetail(String detail)
    {
        this.detail = detail;
    }

    public boolean isRead()
    {
        return isRead;
    }

    public void setRead(boolean isRead)
    {
        this.isRead = isRead;
    }

}
