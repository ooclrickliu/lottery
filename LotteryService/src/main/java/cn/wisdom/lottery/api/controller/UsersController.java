/**
 * UsersController.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 4, 2015
 */
package cn.wisdom.lottery.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.wisdom.lottery.api.response.DoorbellPaymentAPIResult;
import cn.wisdom.lottery.common.model.JsonDocument;
import cn.wisdom.lottery.dao.vo.AppProperty;
import cn.wisdom.lottery.dao.vo.User;
import cn.wisdom.lottery.service.UserService;
import cn.wisdom.lottery.service.context.SessionContext;
import cn.wisdom.lottery.service.exception.ServiceException;
import cn.wisdom.lottery.service.wx.WXService;

/**
 * UsersController provides restful APIs of user
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */

@Controller
@RequestMapping("/user")
public class UsersController
{
    @Autowired
    private UserService userService;
    
    @Autowired
    private WXService wxService;
    
    @Autowired
    private AppProperty appProperties;

//    private static final JsonDocument SUCCESS = DoorbellPaymentAPIResult.SUCCESS;

    /**
     * Get current User by oauth code.
     * 
     * @return
     * @throws ServiceException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/current")
    @ResponseBody
    public JsonDocument getCurrentUser() throws ServiceException
    {
    	User currentUser = SessionContext.getCurrentUser();
        return new DoorbellPaymentAPIResult(currentUser);
    }

}
