package cn.wisdom.lottery.payment.service.wx;

import cn.wisdom.lottery.payment.service.wx.message.TextMessage;

public interface WXMessageListener {

	/**
	 * 接收到文本消息
	 * 
	 * @param message
	 */
	TextMessage onTextMessage(TextMessage message);
}
