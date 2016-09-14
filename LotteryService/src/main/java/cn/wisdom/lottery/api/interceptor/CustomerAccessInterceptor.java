/**
 * PlatformAccessInterceptor.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 4, 2015
 */
package cn.wisdom.lottery.api.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.wisdom.lottery.api.exception.NoAccessException;
import cn.wisdom.lottery.api.response.LotteryAPIResult;
import cn.wisdom.lottery.common.log.Logger;
import cn.wisdom.lottery.common.log.LoggerFactory;
import cn.wisdom.lottery.common.model.JsonDocument;
import cn.wisdom.lottery.common.utils.CookieUtil;
import cn.wisdom.lottery.common.utils.HttpUtils;
import cn.wisdom.lottery.common.utils.StringUtils;
import cn.wisdom.lottery.dao.vo.User;
import cn.wisdom.lottery.service.UserService;
import cn.wisdom.lottery.service.context.SessionContext;
import cn.wisdom.lottery.service.exception.ServiceErrorCode;
import cn.wisdom.lottery.service.exception.ServiceException;

/**
 * PlatformAccessInterceptor do the following things: <li>identify user by
 * access token from cookies before call controller</li>
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class CustomerAccessInterceptor extends HandlerInterceptorAdapter
{

	private Logger logger = LoggerFactory.getLogger(CustomerAccessInterceptor.class.getName());
	
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception
    {
        super.preHandle(request, response, handler);
        
        User user = null;
        String openId = HttpUtils.getParamValue(request, CookieUtil.OPENID);
        if (StringUtils.isNotBlank(openId)) {
        	user = userService.getUserByOpenId(openId); 
        	
		}
        String code = "";
    	if (user == null) {
    		code = HttpUtils.getParamValue(request, CookieUtil.OAUTH_CODE);
            if (StringUtils.isNotBlank(code)) {
            	user = userService.getUserByOauthCode(code);
            	
            	if (user != null) {
            		CookieUtil.addCookie(response, CookieUtil.OPENID, user.getOpenid());
				}
            	CookieUtil.addCookie(response, CookieUtil.OAUTH_CODE, code, 0);
			}
		}
        
        if (user == null) {
        	logger.error("Got user NotSubscribe error, code = [{}], openid = [{}]", code, openId);
        	writeResponse(response, ServiceErrorCode.NOT_SUBSCRIBE);
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
        JsonDocument respBody = new LotteryAPIResult(errCode);

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.write(respBody, MediaType.APPLICATION_JSON, new ServletServerHttpResponse(
                response));
    }
}
