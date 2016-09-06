package cn.wisdom.lottery.api.model;

import java.util.ArrayList;
import java.util.List;

public class SentRedpackList {

	private String nickName;
	
	private String headImgUrl;
	
	private int totalNum;
	
	private float totalFee;
	
	List<SentRedpackItem> items = new ArrayList<SentRedpackItem>();

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public float getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(float totalFee) {
		this.totalFee = totalFee;
	}

	public List<SentRedpackItem> getItems() {
		return items;
	}

	public void setItems(List<SentRedpackItem> items) {
		this.items = items;
	}
}
