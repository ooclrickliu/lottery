package cn.wisdom.lottery.api.controller.merchant;

import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.wisdom.lottery.api.response.LotteryAPIResult;
import cn.wisdom.lottery.common.model.JsonDocument;
import cn.wisdom.lottery.common.utils.CookieUtil;
import cn.wisdom.lottery.dao.constant.RoleType;
import cn.wisdom.lottery.dao.vo.AppProperty;
import cn.wisdom.lottery.dao.vo.User;
import cn.wisdom.lottery.service.UserService;
import cn.wisdom.lottery.service.context.SessionContext;
import cn.wisdom.lottery.service.exception.ServiceErrorCode;
import cn.wisdom.lottery.service.exception.ServiceException;

@Controller
@RequestMapping("/merchant")
public class MerchantControllers {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AppProperty appProperty;

	private static final JsonDocument SUCCESS = LotteryAPIResult.SUCCESS;


	/**
	 * login.
	 * 
	 * @return
	 * @throws ServiceException
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/login")
	@ResponseBody
	public JsonDocument login(HttpServletResponse response,
			@RequestParam String phone, @RequestParam String password)
			throws ServiceException {
		
		String accessToken = userService.login(phone, password);
		
		User user = SessionContext.getCurrentUser();
		if (user.getRole() != RoleType.ADMIN) {
			String errMsg = MessageFormat.format("Admin [{0}] not exists.", phone);
			throw new ServiceException(ServiceErrorCode.USER_NOT_EXIST, errMsg);
		}

		CookieUtil.addCookie(response, CookieUtil.KEY_ACCESS_TOKEN,
				accessToken, appProperty.cookieAccessTokenHourAge * CookieUtil.ONE_HOUR);

		return SUCCESS;
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
        String accessToken =
                CookieUtil.getCookie(request, CookieUtil.KEY_ACCESS_TOKEN);
        userService.logout(accessToken);

        CookieUtil.addCookie(response, CookieUtil.KEY_ACCESS_TOKEN,
                accessToken, 0);

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
        String newAccessToken =
                userService.changePassword(oldPassword, newPassword);

        CookieUtil.addCookie(response, CookieUtil.KEY_ACCESS_TOKEN,
                newAccessToken, appProperty.cookieAccessTokenHourAge);

        return SUCCESS;
    }
}
