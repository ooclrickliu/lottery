/**
 * User.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 4, 2015
 */
package cn.wisdom.lottery.dao.vo;

import me.chanjar.weixin.mp.bean.result.WxMpUser;
import cn.wisdom.lottery.dao.annotation.Column;
import cn.wisdom.lottery.dao.constant.RoleType;

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
    private String roleValue;
	private RoleType role;
	
	@Column("real_name")
	private String real_name;

	@Column("phone")
    private String phone;
	
	@Column("password")
	private String password;
	
	@Column("card_no")
	private String cardNo;
	
	@Column("nick_name")
    private String nickName;

	@Column("sex")
    private String sex;
	
	@Column("province")
    private String province;

	@Column("city")
    private String city;

	@Column("country")
    private String country;

	@Column("head_img_url")
    private String headImgUrl;
	
	@Column("unionid")
    private String unionid;
	
	@Column("unionid")
	private boolean subscribe;

	@Column("subscribe_time")
    private long subscribeTime;
	
	public User() {}
	
	public User(WxMpUser wxMpUser) {
		this.openid = wxMpUser.getOpenId();
		this.nickName = wxMpUser.getNickname();
		this.province = wxMpUser.getProvince();
		this.city = wxMpUser.getCity();
		this.country = wxMpUser.getCountry();
		this.sex = wxMpUser.getSex();
		this.headImgUrl = wxMpUser.getHeadImgUrl();
		this.unionid = wxMpUser.getUnionId();
		this.subscribe = wxMpUser.getSubscribe() == null ? false : wxMpUser.getSubscribe();
		this.subscribeTime = wxMpUser.getSubscribeTime() == null ? 0 : wxMpUser.getSubscribeTime();
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public long getSubscribeTime() {
		return subscribeTime;
	}

	public void setSubscribeTime(long subscribeTime) {
		this.subscribeTime = subscribeTime;
	}

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
		return RoleType.valueOf(this.roleValue);
	}

	public void setRole(RoleType role) {
		this.role = role;
		this.roleValue = this.role.toString();
	}

	public String getReal_name() {
		return real_name;
	}

	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isSubscribe() {
		return subscribe;
	}

	public void setSubscribe(boolean subscribe) {
		this.subscribe = subscribe;
	}
    
}
