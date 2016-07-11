package cn.wisdom.lottery.payment.dao.vo;

import cn.wisdom.lottery.payment.dao.annotation.Column;


public class Customer extends User {

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

	@Column("subscribe_time")
    private String subscribeTime	;

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

	public String getSubscribeTime() {
		return subscribeTime;
	}

	public void setSubscribeTime(String subscribeTime) {
		this.subscribeTime = subscribeTime;
	}
}
