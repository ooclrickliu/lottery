package cn.wisdom.lottery.payment.api.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.wisdom.lottery.payment.common.utils.HttpUtils;
import cn.wisdom.lottery.payment.service.UserService;
import cn.wisdom.lottery.payment.service.exception.ServiceErrorCode;
import cn.wisdom.lottery.payment.service.exception.ServiceException;
import cn.wisdom.lottery.payment.service.wx.WXService;

@RequestMapping("/wx")
@Controller
public class WxController {
	
	@Autowired
	private WXService wxService;
	
	@Autowired
	private UserService userService;

    @RequestMapping(method = RequestMethod.GET, value = "/verify1")
	public String wxVerify(String signature, String token, String timestamp, String nonce,
			String echostr) {
		List<String> args = new ArrayList<String>();
		args.add(token);
		args.add(timestamp);
		args.add(nonce);
		
		return echostr;
	}

    @RequestMapping(method = RequestMethod.GET, value = "/verify")
    @ResponseBody
	public String wxVerify(@RequestParam String echostr) {
		return echostr;
	}
    
    @RequestMapping(method = RequestMethod.GET, value = "/oauth2")
    @ResponseBody
    public String oauth2(HttpServletRequest request) throws ServiceException {
    	
    	String code = HttpUtils.getParamValue(request, "code");
//    	String state = HttpUtils.getParamValue(request, "state");
    	
		try {
			WxMpOAuth2AccessToken oAuth2AccessToken = wxService.getWxMpService().oauth2getAccessToken(code);
			
	    	WxMpUser wxMpUser = wxService.getWxMpService().oauth2getUserInfo(oAuth2AccessToken, "zh_CN");
	    	
	    	userService.updateUserInfo(wxMpUser);
		} catch (WxErrorException e) {
			throw new ServiceException(ServiceErrorCode.OAUTH_FAIL, "WX oauth fail!", e);
		}
    	
    	
    	
    	return "url";
    }
}
