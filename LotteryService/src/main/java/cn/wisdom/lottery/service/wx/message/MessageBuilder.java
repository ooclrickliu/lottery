package cn.wisdom.lottery.service.wx.message;

import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;

public interface MessageBuilder {

	WxMpXmlOutMessage buildMessage(WxMpXmlMessage wxMessage);
	
	String getMenuKey();
}
