/**
 * AppProperty.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * 2016-2-17
 */
package cn.wisdom.lottery.dao.vo;

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
	@Value("${cookie.access_token.hour.age}")
	public int cookieAccessTokenHourAge;
	
	@Value("${wxPay.debug}")
	public boolean wxPaydebug;
	
	@Value("${wxPay.notify_url}")
    public String wxPayNotifyUrl;
	
	@Value("${img.server.url}")
	public String imgServerUrl;
	
	@Value("${lottery.default.operator}")
	public String defaultOperator;
	
	@Value("${lottery.default.kf}")
	public String defaultKf;
	
	@Value("${lottery.default.merchant}")
	public long defaultMerchant;
	
	@Value("${lottery.redpack.limit.max}")
	public long redpackLimitMax;
}
