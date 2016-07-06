package com.ovt.order.dao.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "account_page_query_result")
@XmlAccessorType(XmlAccessType.FIELD)
public class AccountPage
{
    @XmlElementWrapper(name = "account_log_list")
    @XmlElement(name = "AccountQueryAccountLogVO")
    private List<AccountQueryAccountLog> accountLogList;

    @XmlElement(name = "has_next_page")
    private String hasNextPage;

    @XmlElement(name = "page_no")
    private String pageNo;

    @XmlElement(name = "page_size")
    private String pageSize;

    public List<AccountQueryAccountLog> getAccountLogList()
    {
        return accountLogList;
    }

    public void setAccountLogList(List<AccountQueryAccountLog> accountLogList)
    {
        this.accountLogList = accountLogList;
    }

    public String getHasNextPage()
    {
        return hasNextPage;
    }

    public void setHasNextPage(String hasNextPage)
    {
        this.hasNextPage = hasNextPage;
    }

    public String getPageNo()
    {
        return pageNo;
    }

    public void setPageNo(String pageNo)
    {
        this.pageNo = pageNo;
    }

    public String getPageSize()
    {
        return pageSize;
    }

    public void setPageSize(String pageSize)
    {
        this.pageSize = pageSize;
    }

}
