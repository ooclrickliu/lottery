/**
 * OrderTask.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * 2016年1月20日
 */
package com.ovt.order.service.task;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.ovt.common.log.Logger;
import com.ovt.common.log.LoggerFactory;
import com.ovt.common.utils.CollectionUtils;
import com.ovt.common.utils.DateTimeUtils;
import com.ovt.common.utils.StringUtils;
import com.ovt.order.dao.vo.AccountPage;
import com.ovt.order.dao.vo.AccountQueryAccountLog;
import com.ovt.order.dao.vo.PageInfo;
import com.ovt.order.dao.vo.Payment;
import com.ovt.order.dao.vo.Refund;
import com.ovt.order.service.AccountQueryService;
import com.ovt.order.service.PaymentService;
import com.ovt.order.service.RefundService;
import com.ovt.order.service.exception.ServiceException;

/**
 * OrderTask
 * 
 * @Author brad.zhou
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class AccountCheckTask
{
    @Autowired
    private AccountQueryService accountQueryService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RefundService refundService;

    private Logger logger = LoggerFactory.getLogger(AccountCheckTask.class
            .getName());

    @Scheduled(cron = "0 58 23 * * ?")
    public void checkAccount() throws ServiceException
    {
        logger.info("account check task start!");

        Date currentDate = new Date();

        PageInfo pageInfo = new PageInfo(0, 5000, "id", "asc");
        String startTime = DateTimeUtils.formatSqlDate(currentDate)
                + " 00:00:00";
        String endTime = DateTimeUtils.formatSqlDate(currentDate) + " 23:59:59";
        String pageNo = "1";

        // 获取当天的支付宝账户流水,将支付宝的数据放到Float[]的第一个
        Map<String, float[]> orderIncomeMap = new HashMap<String, float[]>();
        Map<String, float[]> orderOutcomeMap = new HashMap<String, float[]>();
        AccountPage accountPage = accountQueryService.getAccountPageByTime(
                pageNo, startTime, endTime);
        List<AccountQueryAccountLog> accountLogList = null;
        if (accountPage != null)
        {
            accountLogList = accountPage.getAccountLogList();
        }
        if (CollectionUtils.isNotEmpty(accountLogList))
        {
            for (AccountQueryAccountLog accountLog : accountLogList)
            {
                Float income = str2Float(accountLog.getIncome());
                Float outcome = str2Float(accountLog.getOutcome());
                String orderNo = accountLog.getMerchantOutOrderNo();
                if (income > 0)
                {
                    float[] incomePair = orderIncomeMap.get(orderNo);
                    if (incomePair == null)
                    {
                        incomePair = new float[2];
                    }
                    incomePair[0] = income;
                    orderIncomeMap.put(orderNo, incomePair);
                }
                if (outcome > 0)
                {
                    float[] outcomePair = orderOutcomeMap.get(orderNo);
                    if (outcomePair == null)
                    {
                        outcomePair = new float[2];
                    }
                    outcomePair[0] = outcome;
                    orderOutcomeMap.put(orderNo, outcomePair);
                }
            }
        }

        // 获取当天的Doorbell系统流水,将doorbell系统的数据放到Float[]的第二个
        List<Payment> paymentList = paymentService.getPaymentListByTime(
                pageInfo, startTime, endTime);
        List<Refund> refundList = refundService.getRefundListByTime(pageInfo,
                startTime, endTime);
        if (CollectionUtils.isNotEmpty(paymentList))
        {
            for (Payment payment : paymentList)
            {
                float[] incomePair = orderIncomeMap.get(payment.getOrderNo());
                if (incomePair == null)
                {
                    incomePair = new float[2];
                }
                incomePair[1] = payment.getPayFee();
                orderIncomeMap.put(payment.getOrderNo(), incomePair);
            }
        }
        if (CollectionUtils.isNotEmpty(refundList))
        {
            for (Refund refund : refundList)
            {
                float[] outcomePair = orderOutcomeMap.get(refund.getOrderNo());
                if (outcomePair == null)
                {
                    outcomePair = new float[2];
                }
                outcomePair[1] = refund.getRefundFee();
                orderOutcomeMap.put(refund.getOrderNo(), outcomePair);
            }
        }

        // 对比两个系统的数据,记录不相同的数据到数据库
        if (CollectionUtils.isNotEmpty(orderIncomeMap))
        {
            Map<String, String> errMap = new HashMap<String, String>();
            for (String orderNo : orderIncomeMap.keySet())
            {
                float[] incomePair = orderIncomeMap.get(orderNo);
                if (incomePair[0] != incomePair[1])
                {
                    errMap.put(orderNo, "alipay totalPay is: " + incomePair[0]
                            + ", doorbell totalPay is: " + incomePair[1]);
                }
            }
            accountQueryService.saveAccountCheckError(errMap);
        }

        if (CollectionUtils.isNotEmpty(orderOutcomeMap))
        {
            Map<String, String> errMap = new HashMap<String, String>();
            for (String orderNo : orderOutcomeMap.keySet())
            {
                float[] outcomePair = orderOutcomeMap.get(orderNo);
                if (outcomePair[0] != outcomePair[1])
                {
                    errMap.put(orderNo, "alipay totalRefund is: "
                            + outcomePair[0] + ", doorbell totalRefund is: "
                            + outcomePair[1]);
                }
            }
            accountQueryService.saveAccountCheckError(errMap);
        }

        logger.info("account check task complete!");
    }

    private Float str2Float(String value)
    {
        Float ret = 0.00f;
        if (StringUtils.isNotBlank(value))
        {
            try
            {
                ret = Float.valueOf(value);
            }
            catch (NumberFormatException e)
            {
                ret = 0.00f;
            }
        }
        return ret;
    }
}
