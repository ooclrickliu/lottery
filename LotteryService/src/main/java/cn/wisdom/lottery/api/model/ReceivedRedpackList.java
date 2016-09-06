package cn.wisdom.lottery.api.model;

import java.util.ArrayList;
import java.util.List;

public class ReceivedRedpackList {

	private String nickName;
	
	private String headImgUrl;
	
	private int totalNum;
	
	private float totalBonus;
	
	List<ReceivedRedpackItem> items = new ArrayList<ReceivedRedpackItem>();

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

	public float getTotalBonus() {
		return totalBonus;
	}

	public void setTotalBonus(float totalBonus) {
		this.totalBonus = totalBonus;
	}

	public List<ReceivedRedpackItem> getItems() {
		return items;
	}

	public void setItems(List<ReceivedRedpackItem> items) {
		this.items = items;
	}
}
