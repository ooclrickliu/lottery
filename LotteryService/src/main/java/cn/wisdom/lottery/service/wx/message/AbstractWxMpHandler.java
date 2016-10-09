package cn.wisdom.lottery.service.wx.message;

import java.util.Map;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;

public abstract class AbstractWxMpHandler implements WxMpMessageHandler {

	@Override
	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
			Map<String, Object> context, WxMpService wxMpService,
			WxSessionManager sessionManager) throws WxErrorException {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected WxMpXmlOutMessage buildOutMessage(WxMpXmlMessage wxMessage, String content)
	{
		return WxMpXmlOutMessage.TEXT().content(content)
				.toUser(wxMessage.getFromUserName())
				.fromUser(wxMessage.getToUserName()).build();
	}
	
	protected WxMpXmlOutMessage buildSuccessOutMessage(WxMpXmlMessage wxMessage)
	{
		return WxMpXmlOutMessage.TEXT().content("success")
		.toUser(wxMessage.getFromUserName())
		.fromUser(wxMessage.getToUserName()).build();
	}

}
