package cn.wisdom.lottery.payment.service.wx.event;

public class MenuClickEvent extends WXEventBase {

	private String eventKey;

	public String getEventKey() {
		return eventKey;
	}

	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}
}
