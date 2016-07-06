package com.ovt.order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ovt.common.exception.OVTException;
import com.ovt.common.model.JsonDocument;
import com.ovt.order.controller.response.PaymentServiceAPIResult;
import com.ovt.order.dao.vo.AccountCheckError;
import com.ovt.order.dao.vo.AccountPage;
import com.ovt.order.service.AccountQueryService;
import com.ovt.order.service.exception.ServiceException;
import com.ovt.order.service.task.AccountCheckTask;

@Controller
@RequestMapping("/account")
public class AccountQueryController
{
    @Autowired
    private AccountQueryService accountQueryService;
    
    @Autowired
    private AccountCheckTask accountCheckTask;

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/query")
    public JsonDocument queryAliAccountPage(@RequestParam String pageNo,
            @RequestParam String startTime, @RequestParam String endTime)
            throws ServiceException
    {
        AccountPage accountPage = accountQueryService.getAccountPageByTime(
                pageNo, startTime, endTime);

        return new PaymentServiceAPIResult(accountPage);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/query/byOrder")
    public JsonDocument queryAliAccountPage(@RequestParam String pageNo,
            @RequestParam String orderNo) throws ServiceException
    {
        AccountPage accountPage = accountQueryService.getAccountPageByOrderNo(
                pageNo, orderNo);

        return new PaymentServiceAPIResult(accountPage);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/query/errors")
    public JsonDocument getAccountCheckErrors() throws ServiceException
    {
        List<AccountCheckError> accountCheckErrors = accountQueryService
                .getAccountCheckErrorList();

        return new PaymentServiceAPIResult(accountCheckErrors);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/query/error/read")
    public JsonDocument markAccountCheckErrorRead(@RequestParam long id)
            throws ServiceException
    {
        accountQueryService.markAccourCheckErrorRead(id);

        return new PaymentServiceAPIResult();
    }
    
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/query/task/test")
    public JsonDocument testAccountCheckTask()
            throws OVTException
    {
        accountCheckTask.checkAccount();

        return new PaymentServiceAPIResult();
    }
}
