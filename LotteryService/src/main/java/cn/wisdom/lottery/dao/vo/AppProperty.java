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
	@Value("${cookie.access_token.age}")
	public int cookieAccessTokenAge;
	
	@Value("${wxPay.debug}")
	public boolean wxPaydebug;
	
	@Value("${wxPay.notify_url}")
    public String wxPayNotifyUrl;
	
	@Value("${lottery.default.merchant}")
	public long defaultMerchant;
}
