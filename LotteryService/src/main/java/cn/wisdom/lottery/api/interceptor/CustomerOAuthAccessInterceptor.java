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
import cn.wisdom.lottery.common.model.JsonDocument;
import cn.wisdom.lottery.common.utils.HttpUtils;
import cn.wisdom.lottery.common.utils.StringUtils;
import cn.wisdom.lottery.dao.vo.User;
import cn.wisdom.lottery.service.UserService;
import cn.wisdom.lottery.service.context.SessionContext;
import cn.wisdom.lottery.service.exception.ServiceErrorCode;
import cn.wisdom.lottery.service.exception.ServiceException;

public class CustomerOAuthAccessInterceptor extends HandlerInterceptorAdapter {

	private static final String OAUTH_CODE = "code";

	private static final String OPENID = "openid";

	@Autowired
	private UserService userService;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		super.preHandle(request, response, handler);

		User user = null;
		String openId = HttpUtils.getParamValue(request, OPENID);
		if (StringUtils.isNotBlank(openId)) {
			user = userService.getUserByOpenId(openId);

		}
		if (user == null) {
			String code = HttpUtils.getParamValue(request, OAUTH_CODE);
			if (StringUtils.isNotBlank(code)) {
				user = userService.getSubscribedUserByOauthCode(code);
			}
		}

		if (user == null) {
			writeResponse(response, ServiceErrorCode.OAUTH_FAIL);
			return false;
		} else {
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
	private void initSessionContext(User user) {
		SessionContext.setCurrentUser(user);
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
		SessionContext.destroy();
	}

	private void writeResponse(HttpServletResponse response, String errCode)
			throws Exception {
		JsonDocument respBody = new LotteryAPIResult(errCode);

		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.write(respBody, MediaType.APPLICATION_JSON,
				new ServletServerHttpResponse(response));
	}

}
