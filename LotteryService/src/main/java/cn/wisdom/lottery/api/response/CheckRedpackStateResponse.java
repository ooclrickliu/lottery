package cn.wisdom.lottery.api.response;

public class CheckRedpackStateResponse {

	private long sender;
	
	private String senderName;
	
	private String headImgUrl;
	
	private String status;

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getSender() {
		return sender;
	}

	public void setSender(long sender) {
		this.sender = sender;
	}
}
