/**
 * OrderItem.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月21日
 */
package com.ovt.order.dao.vo;

/**
 * OrderItem
 * 
 * @Author brad.zhou
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class OrderItem extends BaseEntity
{
    private long orderId;

    private String productNo;

    private String productName;

    private float productPrice;

    private int productNum;

    public OrderItem()
    {
        
    }
    
    public OrderItem(String productNo, String productName, float productPrice,
            int productNum)
    {
        this.productNo = productNo;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productNum = productNum;
    }

    /**
     * @return the orderId
     */
    public long getOrderId()
    {
        return orderId;
    }

    /**
     * @param orderId the orderId to set
     */
    public void setOrderId(long orderId)
    {
        this.orderId = orderId;
    }

    /**
     * @return the productNo
     */
    public String getProductNo()
    {
        return productNo;
    }

    /**
     * @param productNo the productNo to set
     */
    public void setProductNo(String productNo)
    {
        this.productNo = productNo;
    }

    /**
     * @return the productName
     */
    public String getProductName()
    {
        return productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    /**
     * @return the productPrice
     */
    public float getProductPrice()
    {
        return productPrice;
    }

    /**
     * @param productPrice the productPrice to set
     */
    public void setProductPrice(float productPrice)
    {
        this.productPrice = productPrice;
    }

    /**
     * @return the productNum
     */
    public int getProductNum()
    {
        return productNum;
    }

    /**
     * @param productNum the productNum to set
     */
    public void setProductNum(int productNum)
    {
        this.productNum = productNum;
    }

    @Override
    public String toString()
    {
        return "OrderItem [orderId=" + orderId + ", productNo=" + productNo
                + ", productName=" + productName + ", productPrice="
                + productPrice + ", productNum=" + productNum + "]";
    }

}
