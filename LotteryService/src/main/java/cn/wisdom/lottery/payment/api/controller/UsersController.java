/**
 * UsersController.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 4, 2015
 */
package cn.wisdom.lottery.payment.api.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.wisdom.lottery.payment.api.response.DoorbellPaymentAPIResult;
import cn.wisdom.lottery.payment.common.model.JsonDocument;
import cn.wisdom.lottery.payment.common.utils.CookieUtil;
import cn.wisdom.lottery.payment.dao.vo.AppProperty;
import cn.wisdom.lottery.payment.dao.vo.User;
import cn.wisdom.lottery.payment.service.UserService;
import cn.wisdom.lottery.payment.service.context.SessionContext;
import cn.wisdom.lottery.payment.service.exception.ServiceException;

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

    private static final JsonDocument SUCCESS = DoorbellPaymentAPIResult.SUCCESS;
    @Autowired
    private AppProperty appProperties;

    /**
     * login.
     * 
     * @param email
     * @param password
     * @returns
     * @throws ServiceException
     */
    @RequestMapping(method = RequestMethod.POST, value = "/login")
    @ResponseBody
    public JsonDocument login(HttpServletResponse response,
            @RequestParam String name, @RequestParam String password)
                    throws ServiceException
    {
        String accessToken = userService.login(name, password);

        CookieUtil.addCookie(response, CookieUtil.KEY_ACCESS_TOKEN, accessToken,
                appProperties.cookieAccessTokenAge);

        return new DoorbellPaymentAPIResult(SessionContext.getCurrentUser());
    }

    /**
     * logout.
     * 
     * @param response
     * @return
     * @throws ServiceException
     */
    @RequestMapping(method = RequestMethod.POST, value = "/logout")
    @ResponseBody
    public JsonDocument logout(HttpServletRequest request,
            HttpServletResponse response) throws ServiceException
    {
        String accessToken = CookieUtil.getCookie(request,
                CookieUtil.KEY_ACCESS_TOKEN);
        userService.logout(accessToken);

        CookieUtil.addCookie(response, CookieUtil.KEY_ACCESS_TOKEN, accessToken,
                0);

        return SUCCESS;
    }

    /**
     * Change user password.
     * 
     * @param oldPassword
     * @param newPassword
     * @return
     * @throws ServiceException
     */
    @RequestMapping(method = RequestMethod.POST, value = "/changePassword")
    @ResponseBody
    public JsonDocument changePassword(HttpServletResponse response,
            @RequestParam String oldPassword, @RequestParam String newPassword)
                    throws ServiceException
    {
        String newAccessToken = userService.changePassword(oldPassword,
                newPassword);

        CookieUtil.addCookie(response, CookieUtil.KEY_ACCESS_TOKEN,
                newAccessToken, appProperties.cookieAccessTokenAge);

        return SUCCESS;
    }

    /**
     * Get user list.
     * 
     * @param page
     * @param limit
     * @param sortBy
     * @param order
     * @return
     * @throws ServiceException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/listAll")
    @ResponseBody
    public JsonDocument getUserList() throws ServiceException
    {
        List<User> userList = userService.getUserList();

        return new DoorbellPaymentAPIResult(userList);
    }

    /**
     * Create an user.
     * 
     * @param email
     * @param password
     * @return
     * @throws ServiceException
     */
    @RequestMapping(method = RequestMethod.POST, value = "/add")
    @ResponseBody
    public JsonDocument createUser(@RequestParam String name,
            @RequestParam String password) throws ServiceException
    {
        User user = userService.createUser(name, password);

        return new DoorbellPaymentAPIResult(user);
    }

    /**
     * Create an admin.
     * 
     * @param email
     * @param password
     * @return
     * @throws ServiceException
     */
    @RequestMapping(method = RequestMethod.POST, value = "/{id}/delete")
    @ResponseBody
    public JsonDocument deleteUser(@PathVariable int id) throws ServiceException
    {
        userService.deleteUser(id);
        return SUCCESS;
    }

    /**
     * Get current User
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
    
    
    /**
     * Trigger clean token task
     * 
     * @return
     * @throws ServiceException
     */

    @RequestMapping(method = RequestMethod.GET, value = "/cleanToken")
    @ResponseBody
    public JsonDocument cleanExpireUserToken() throws ServiceException
    {
        userService.cleanExpiredUserToken();
        
        return DoorbellPaymentAPIResult.SUCCESS;
    }

}
