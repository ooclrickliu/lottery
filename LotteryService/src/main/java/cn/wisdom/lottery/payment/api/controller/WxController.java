package cn.wisdom.lottery.payment.api.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.wisdom.lottery.payment.common.utils.HttpUtils;
import cn.wisdom.lottery.payment.service.wx.WXService;

@RequestMapping("/wx")
@Controller
public class WxController {
	
	@Autowired
	private WXService wxService;

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
    public String oauth2(HttpServletRequest request) {
    	
    	String code = HttpUtils.getParamValue(request, "code");
    	String state = HttpUtils.getParamValue(request, "state");
    	
    	return echostr;
    }
}
