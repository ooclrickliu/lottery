/**
 * Order.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月21日
 */
package com.ovt.order.dao.vo;

import java.sql.Timestamp;
import java.util.List;

import com.ovt.common.exception.OVTException;
import com.ovt.common.utils.JsonUtils;
import com.ovt.order.dao.constant.OrderState;

/**
 * Order
 * 
 * @Author brad.zhou
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class Order extends BaseEntity
{
    private String orderNo;

    private OrderState orderState;

    private float orderTotalFee;
    
    private float refundedFee;

    private String userId;
    
    private String userName;

    private String createBy; // the userId who create this order.
    
    private String creatorName;

    private String orderRemark;

    private int isDelete;
    
    private int extra1;

    private String extra2;

    private Timestamp extra3;

    private List<OrderItem> orderItemList;
    
    private Timestamp payTime;
    
    private Timestamp refundTime;

    /**
     * why named as getRefundableFee?
     * Gson will think refundableFee as a field.
     * 
     * @return
     */
    public float obtainRefundableFee()
    {
        return orderTotalFee - refundedFee;
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
     * @return the orderState
     */
    public OrderState getOrderState()
    {
        return orderState;
    }

    /**
     * @param orderState the orderState to set
     */
    public void setOrderState(OrderState orderState)
    {
        this.orderState = orderState;
    }

    /**
     * @return the orderTotalFee
     */
    public float getOrderTotalFee()
    {
        return orderTotalFee;
    }

    /**
     * @param orderTotalFee the orderTotalFee to set
     */
    public void setOrderTotalFee(float orderTotalFee)
    {
        this.orderTotalFee = orderTotalFee;
    }
    
    /**
	 * @return the refundedFee
	 */
	public float getRefundedFee() {
		return refundedFee;
	}

	/**
	 * @param refundedFee the refundedFee to set
	 */
	public void setRefundedFee(float refundedFee) {
		this.refundedFee = refundedFee;
	}

	/**
     * @return the userId
     */
    public String getUserId()
    {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId)
    {
        this.userId = userId;
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
     * @return the orderRemark
     */
    public String getOrderRemark()
    {
        return orderRemark;
    }

    /**
     * @param orderRemark the orderRemark to set
     */
    public void setOrderRemark(String orderRemark)
    {
        this.orderRemark = orderRemark;
    }

    /**
     * @return the isDelete
     */
    public int getIsDelete()
    {
        return isDelete;
    }

    /**
     * @param isDelete the isDelete to set
     */
    public void setIsDelete(int isDelete)
    {
        this.isDelete = isDelete;
    }

    /**
     * @return the orderItemList
     */
    public List<OrderItem> getOrderItemList()
    {
        return orderItemList;
    }

    /**
     * @param orderItemList the orderItemList to set
     */
    public void setOrderItemList(List<OrderItem> orderItemList)
    {
        this.orderItemList = orderItemList;
    }
    
    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getCreatorName()
    {
        return creatorName;
    }

    public void setCreatorName(String creatorName)
    {
        this.creatorName = creatorName;
    }

    @Override
    public String toString()
    {
        String str = "";
        
        try
        {
            str = JsonUtils.toJson(this);
        }
        catch (OVTException e)
        {
            str = this.orderNo;
        }
        return str;
    }

    public int getExtra1()
    {
        return extra1;
    }

    public void setExtra1(int extra1)
    {
        this.extra1 = extra1;
    }

    public String getExtra2()
    {
        return extra2;
    }

    public void setExtra2(String extra2)
    {
        this.extra2 = extra2;
    }

    public Timestamp getExtra3()
    {
        return extra3;
    }

    public void setExtra3(Timestamp extra3)
    {
        this.extra3 = extra3;
    }

    /**
     * @return the payTime
     */
    public Timestamp getPayTime()
    {
        return payTime;
    }

    /**
     * @param payTime the payTime to set
     */
    public void setPayTime(Timestamp payTime)
    {
        this.payTime = payTime;
    }

    /**
     * @return the refundTime
     */
    public Timestamp getRefundTime()
    {
        return refundTime;
    }

    /**
     * @param refundTime the refundTime to set
     */
    public void setRefundTime(Timestamp refundTime)
    {
        this.refundTime = refundTime;
    }
}
