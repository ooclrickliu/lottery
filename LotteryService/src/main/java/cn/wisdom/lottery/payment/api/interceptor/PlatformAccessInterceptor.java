/**
 * PlatformAccessInterceptor.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 4, 2015
 */
package cn.wisdom.lottery.payment.api.interceptor;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.wisdom.lottery.payment.api.exception.NoAccessException;
import cn.wisdom.lottery.payment.api.response.DoorbellPaymentAPIResult;
import cn.wisdom.lottery.payment.common.model.JsonDocument;
import cn.wisdom.lottery.payment.common.utils.CollectionUtils;
import cn.wisdom.lottery.payment.common.utils.CookieUtil;
import cn.wisdom.lottery.payment.common.utils.HttpUtils;
import cn.wisdom.lottery.payment.common.utils.StringUtils;
import cn.wisdom.lottery.payment.dao.constant.AppClientType;
import cn.wisdom.lottery.payment.dao.vo.Permission;
import cn.wisdom.lottery.payment.dao.vo.User;
import cn.wisdom.lottery.payment.service.AppPropertiesService;
import cn.wisdom.lottery.payment.service.PermissionGrantService;
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
public class PlatformAccessInterceptor extends HandlerInterceptorAdapter
{
    private static final String CLIENT_TYPE = "clientType";
    
    private static final String OPENID = "openId";

    @Autowired
    private UserService userService;

    @Autowired
    private AppPropertiesService appProperties;

    @Autowired
    private PermissionGrantService permissionConstants;

    @PostConstruct
    private void init()
    {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception
    {
        super.preHandle(request, response, handler);
        
        String openId = HttpUtils.getParamValue(request, OPENID);
        User user = userService.getUserByOpenId(openId);
        
        if (user == null) {
        	// 场景：非关注用户访问需授权页面
			// a. 重定向到OAuth2.0授权页面?
        	
        	// b. 直接保存openid为新用户? 有openid吗?
		}
        else {
        	initSessionContext(user);
		}

//        String accessToken = null;
//        String clientType = HttpUtils.getParamValue(request, CLIENT_TYPE);
//
//        if (AppClientType.isMobile(clientType))
//        {
//            accessToken = HttpUtils.getParamValue(request, TOKEN);
//            if (StringUtils.isBlank(accessToken))
//            {
//                throw new ServiceException(ServiceErrorCode.INVALID_ACCESS,
//                        "Please specify a valid token!");
//            }
//
//            initSessionContext(user);
//        }
//        else if (AppClientType.isWeb(clientType))
//        {
//            accessToken = HttpUtils.getParamValue(request, CookieUtil.KEY_ACCESS_TOKEN);
//            if (StringUtils.isBlank(accessToken))
//            {
//                writeResponse(response, ServiceErrorCode.NOT_LOGIN);
//                return false;
//            }
//
//            User user = userService.getUserByAccessToken(accessToken);
//            initSessionContext(user);
//
//            if (!checkPermission(request))
//            {
//                writeResponse(response, ServiceErrorCode.NO_PERMISSION);
//                return false;
//            }
//
//            CookieUtil.addCookie(response, CookieUtil.KEY_ACCESS_TOKEN, accessToken,
//                    appProperties.getCookieAccessTokenAge());
//        }
//        else
//        {
//            throw new ServiceException(ServiceErrorCode.INVALID_ACCESS,
//                    "Please specify a correct clientType arg: ios | android | web!");
//        }

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
        JsonDocument respBody = new DoorbellPaymentAPIResult(errCode);

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.write(respBody, MediaType.APPLICATION_JSON, new ServletServerHttpResponse(
                response));
    }
}
