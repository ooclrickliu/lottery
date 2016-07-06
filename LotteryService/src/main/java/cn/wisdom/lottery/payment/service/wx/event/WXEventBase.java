package cn.wisdom.lottery.payment.service.wx.event;

import cn.wisdom.lottery.payment.service.wx.consts.Event;
import cn.wisdom.lottery.payment.service.wx.consts.MessageType;

public class WXEventBase {

	private String toUserName;
	
	private String fromUserName;
	
	private long createTime;
	
	private MessageType msgType;
	
	private Event event;

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public MessageType getMsgType() {
		return msgType;
	}

	public void setMsgType(MessageType msgType) {
		this.msgType = msgType;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	
}
