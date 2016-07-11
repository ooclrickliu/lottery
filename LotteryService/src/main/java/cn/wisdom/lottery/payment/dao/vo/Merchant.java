package cn.wisdom.lottery.payment.dao.vo;

import cn.wisdom.lottery.payment.dao.annotation.Column;

public class Merchant extends User {

	@Column("address")
    private String address;

	@Column("cert_no")
    private String cert_no;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCert_no() {
		return cert_no;
	}

	public void setCert_no(String cert_no) {
		this.cert_no = cert_no;
	}
}
