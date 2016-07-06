package cn.wisdom.lottery.payment.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/wx")
@Controller
public class WeiXinController {

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
}
