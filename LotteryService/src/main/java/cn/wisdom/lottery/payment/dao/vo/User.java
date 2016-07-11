/**
 * User.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 4, 2015
 */
package cn.wisdom.lottery.payment.dao.vo;

import cn.wisdom.lottery.payment.dao.annotation.Column;
import cn.wisdom.lottery.payment.dao.constant.UserType;

/**
 * User2
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[DAO] 1.0
 */
public class User extends BaseEntity
{

	@Column("user_type")
    private UserType type;

	@Column("phone")
    private String phone;
	
	@Column("card_no")
	private String cardNo;
	
	@Column("openid")
    private String openid;

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
    
}
