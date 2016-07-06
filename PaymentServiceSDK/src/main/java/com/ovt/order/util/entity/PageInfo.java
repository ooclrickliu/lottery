/**
 * PageInfo.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 12, 2015
 */
package com.ovt.order.util.entity;

import com.ovt.order.util.entity.StringUtils;

/**
 * PageInfo
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class PageInfo
{

    // page no starts from 0
    private int pageNo = 0;

    private int pageSize = 10;

    private String sortBy;

    private Order order = Order.ASC;

    public PageInfo()
    {

    }

    public PageInfo(int pageNo, int pageSize)
    {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public PageInfo(int pageNo, int pageSize, String sortBy, String order)
    {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.sortBy = sortBy;
        this.order = Order.toOrder(order);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder me = new StringBuilder();
        me.append("{pageNo: ").append(this.pageNo).append(" pageSize: ")
                .append(this.pageSize).append(" sortBy: ").append(this.sortBy)
                .append(" order: ").append(this.order).append(" }");
        return me.toString();
    }

    public enum Order
    {
        DESC, ASC;

        public static Order toOrder(String orderStr)
        {
            if (StringUtils.equalsIgnoreCase(orderStr, DESC.toString()))
            {
                return DESC;
            }
            else
            {
                return ASC;
            }
        }
    }

    public int getPageNo()
    {
        return pageNo;
    }

    public void setPageNo(int pageNo)
    {
        this.pageNo = pageNo;
    }

    public int getPageSize()
    {
        return pageSize;
    }

    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }

    public String getSortBy()
    {
        return sortBy;
    }

    public void setSortBy(String sortBy)
    {
        this.sortBy = sortBy;
    }

    public Order getOrder()
    {
        return order;
    }

    public void setOrder(Order order)
    {
        this.order = order;
    }
}
