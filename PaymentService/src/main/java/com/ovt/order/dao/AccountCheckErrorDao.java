package com.ovt.order.dao;

import java.util.List;

import com.ovt.order.dao.vo.AccountCheckError;

public interface AccountCheckErrorDao
{
    /**
     * 
     * @param accountCheckErrors
     */
    public void save(List<AccountCheckError> accountCheckErrors);
    
    /**
     * 
     * @return
     */
    public List<AccountCheckError> getAccountCheckErrorList();
    
    /**
     * 
     * @param id
     */
    public void markAccountCheckErrorAsRead(long id);
}
