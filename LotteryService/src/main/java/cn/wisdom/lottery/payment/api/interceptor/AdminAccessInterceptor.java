/**
 * PlatformAccessInterceptor.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 4, 2015
 */
package cn.wisdom.lottery.payment.api.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.wisdom.lottery.payment.api.exception.NoAccessException;
import cn.wisdom.lottery.payment.api.model.LotteryJsonDocument;
import cn.wisdom.lottery.payment.common.model.JsonDocument;
import cn.wisdom.lottery.payment.common.utils.HttpUtils;
import cn.wisdom.lottery.payment.common.utils.StringUtils;
import cn.wisdom.lottery.payment.dao.vo.User;
import cn.wisdom.lottery.payment.service.UserService;
import cn.wisdom.lottery.payment.service.context.SessionContext;
import cn.wisdom.lottery.payment.service.exception.ServiceErrorCode;
import cn.wisdom.lottery.payment.service.exception.ServiceException;

/**
 * PlatformAccessInterceptor do the following things: <li>identify user by
 * access token from cookies before call controller</li>
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class AdminAccessInterceptor extends HandlerInterceptorAdapter
{
    private static final String ACCESS_TOKEN = "access_token";

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception
    {
        super.preHandle(request, response, handler);
        
        User user = null;
        String accessToken = HttpUtils.getParamValue(request, ACCESS_TOKEN);
        if (StringUtils.isNotBlank(accessToken)) {
        	user = userService.getUserByAccessToken(accessToken);
		}
        if (user == null) {
        	writeResponse(response, ServiceErrorCode.NOT_LOGIN);
        	return false;
		}
        else {
        	initSessionContext(user);
		}

        return true;
    }

    /**
     * Initial session context.
     * 
     * @param user
     * @throws ServiceException
     * @throws NoAccessException
     */
    private void initSessionContext(User user)
    {
        SessionContext.setCurrentUser(user);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler, ModelAndView modelAndView) throws Exception
    {
        super.postHandle(request, response, handler, modelAndView);
        SessionContext.destroy();
    }

    private void writeResponse(HttpServletResponse response, String errCode) throws Exception
    {
        JsonDocument respBody = new LotteryJsonDocument(errCode);

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.write(respBody, MediaType.APPLICATION_JSON, new ServletServerHttpResponse(
                response));
    }
}
