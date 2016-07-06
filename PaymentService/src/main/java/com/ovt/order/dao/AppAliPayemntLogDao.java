package com.ovt.order.dao;

import java.util.List;

import com.ovt.order.dao.vo.AppAliPaymentLog;

public interface AppAliPayemntLogDao
{
    public long save(AppAliPaymentLog paymentLog);
    
    public List<AppAliPaymentLog> getPaymentLogList();
    
    public AppAliPaymentLog getPaymentLog(String orderNo);
}
