package com.ovt.order.service;

import java.util.List;
import java.util.Map;

import com.ovt.order.dao.vo.AccountCheckError;
import com.ovt.order.dao.vo.AccountPage;
import com.ovt.order.service.exception.ServiceException;

/**
 * 
 * AccountQueryService
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public interface AccountQueryService
{
    /**
     * 
     * @param pageNo
     * @param startTime
     * @param endTime
     * @return
     * @throws ServiceException
     */
    public AccountPage getAccountPageByTime(String pageNo, String startTime,
            String endTime) throws ServiceException;

    /**
     * 
     * @param pageNo
     * @param orderNo
     * @return
     * @throws ServiceException
     */
    public AccountPage getAccountPageByOrderNo(String pageNo, String orderNo)
            throws ServiceException;

    /**
     * 
     * @param orderErrorMap
     * @throws ServiceException
     */
    public void saveAccountCheckError(Map<String, String> orderErrorMap)
            throws ServiceException;

    /**
     * 
     * @return
     * @throws ServiceException
     */
    public List<AccountCheckError> getAccountCheckErrorList()
            throws ServiceException;

    /**
     * 
     * @param id
     * @throws ServiceException
     */
    public void markAccourCheckErrorRead(long id) throws ServiceException;
}
