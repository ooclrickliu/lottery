/**
 * UsersController.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 4, 2015
 */
package cn.wisdom.lottery.api.controller.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.wisdom.lottery.api.response.LotteryAPIResult;
import cn.wisdom.lottery.common.model.JsonDocument;
import cn.wisdom.lottery.dao.vo.User;
import cn.wisdom.lottery.service.UserService;
import cn.wisdom.lottery.service.context.SessionContext;
import cn.wisdom.lottery.service.exception.ServiceException;

/**
 * UsersController provides restful APIs of user
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */

@Controller
@RequestMapping("/account")
public class CustomerAccountController
{
	@Autowired
	private UserService userService;
	
    /**
     * Get current User.
     * 
     * @return
     * @throws ServiceException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/profile")
    @ResponseBody
    public JsonDocument getCurrentUser() throws ServiceException
    {
    	User currentUser = SessionContext.getCurrentUser();
        return new LotteryAPIResult(currentUser);
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/trans")
    @ResponseBody
    public JsonDocument getTransactions() throws ServiceException
    {
    	return LotteryAPIResult.SUCCESS;
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/pay")
    @ResponseBody
    public JsonDocument pay(@RequestParam long lotteryId, @RequestParam int amount) throws ServiceException
    {
    	return LotteryAPIResult.SUCCESS;
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/withdraw")
    @ResponseBody
    public JsonDocument applyWithdraw(@RequestParam float amount) throws ServiceException
    {
    	return LotteryAPIResult.SUCCESS;
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/withdraw/approve")
    @ResponseBody
    public JsonDocument approveWithdraw(@RequestParam long applyId) throws ServiceException
    {
    	return LotteryAPIResult.SUCCESS;
    }
    
}
