package cn.wisdom.lottery.payment.api.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import me.chanjar.weixin.common.util.StringUtils;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import cn.wisdom.lottery.payment.service.wx.WXService;

@RequestMapping("/wx")
@Controller
public class WxMessageController {

	@Autowired
	private WXService wxService;

	@RequestMapping("/message")
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);

		String signature = request.getParameter("signature");
		String nonce = request.getParameter("nonce");
		String timestamp = request.getParameter("timestamp");

		if (!wxService.getWxMpService().checkSignature(timestamp, nonce,
				signature)) {
			// 消息签名不正确，说明不是公众平台发过来的消息
			response.getWriter().println("非法请求");
			return;
		}

		String echostr = request.getParameter("echostr");
		if (StringUtils.isNotBlank(echostr)) {
			// 说明是一个仅仅用来验证的请求，回显echostr
			response.getWriter().println(echostr);
			return;
		}

		String encryptType = StringUtils.isBlank(request
				.getParameter("encrypt_type")) ? "raw" : request
				.getParameter("encrypt_type");

		if ("raw".equals(encryptType)) {
			// 明文传输的消息
			WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(request
					.getInputStream());
			System.out.println("------------raw Message:\n" + inMessage.toString());
			WxMpXmlOutMessage outMessage = wxService.getWxMpMessageRouter()
					.route(inMessage);
			if (outMessage != null) {
				response.getWriter().write(outMessage.toXml());
			}
			return;
		}

		if ("aes".equals(encryptType)) {
			// 是aes加密的消息
			String msgSignature = request.getParameter("msg_signature");
			WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(
					request.getInputStream(), wxService.getWxConfig(),
					timestamp, nonce, msgSignature);
			
			System.out.println("------------- Aes Message:\n" + inMessage.toString());
			
			WxMpXmlOutMessage outMessage = wxService.getWxMpMessageRouter()
					.route(inMessage);
			response.getWriter().write(
					outMessage.toEncryptedXml(wxService.getWxConfig()));
			return;
		}

		response.getWriter().println("不可识别的加密类型");

		return;
	}
}
