/**
 * AppProperty.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * 2016-2-17
 */
package cn.wisdom.lottery.payment.dao.vo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * AppProperty
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
@Component
public class AppProperty
{
	@Value("cookie_access_token_age")
	public int cookieAccessTokenAge;
	
	@Value("payment_service_url")
	public String paymentServiceUrl;
	
	@Value("debug_pay")
	public boolean debugPay;
}
