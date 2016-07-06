/**
 * RestTemplateConfig.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * 2016年1月11日
 */
package com.ovt.order.service.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplateConfig
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See 
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
@Component
public class RestTemplateFactory
{
    @Bean
    public RestTemplate restTemplate()
    {
        return new RestTemplate();
    }
}
