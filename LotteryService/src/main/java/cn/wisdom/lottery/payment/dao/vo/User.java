/**
 * User.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 4, 2015
 */
package cn.wisdom.lottery.payment.dao.vo;

import cn.wisdom.lottery.payment.dao.annotation.Column;
import cn.wisdom.lottery.payment.dao.constant.RoleType;

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
	@Column("openid")
    private String openid;

	@Column("role")
    private RoleType role;
	
	@Column("real_name")
	private String real_name;

	@Column("phone")
    private String phone;
	
	@Column("card_no")
	private String cardNo;

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
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

	public RoleType getRole() {
		return role;
	}

	public void setRole(RoleType role) {
		this.role = role;
	}

	public String getReal_name() {
		return real_name;
	}

	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}
    
}
