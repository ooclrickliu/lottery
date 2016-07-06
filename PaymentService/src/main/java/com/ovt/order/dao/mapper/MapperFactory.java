/**
 * MapperFactory.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月21日
 */
package com.ovt.order.dao.mapper;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * MapperFactory
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See 
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
@Component
public class MapperFactory
{
    @Bean
    public PaymentMapper paymentMapper()
    {
        return new PaymentMapper();
    }

    @Bean
    public AliPaymentLogMapper aliPaymentLogMapper()
    {
        return new AliPaymentLogMapper();
    }
    
    @Bean
    public AppAliPaymentLogMapper appAliPaymentLogMapper()
    {
        return new AppAliPaymentLogMapper();
    }
    
    @Bean
    public RefundMapper refundMapper()
    {
        return new RefundMapper();
    }
    
    @Bean
    public RefundOrderMapper refundOrderMapper()
    {
        return new RefundOrderMapper();
    }
    
    @Bean
    public AliRefundLogMapper aliRefundLogMapper()
    {
        return new AliRefundLogMapper();
    }
    
    @Bean
    public OrderMapper orderMapper()
    {
        return new OrderMapper();
    }
    
    @Bean
    public OrderWithPaytimeMapper orderWithPaytimeMapper()
    {
        return new OrderWithPaytimeMapper();
    }
    
    @Bean
    public OrderWithRefundtimeMapper orderWithRefundtimeMapper()
    {
        return new OrderWithRefundtimeMapper();
    }
    
    @Bean
    public OrderItemMapper orderItemMapper()
    {
        return new OrderItemMapper();
    }
    
    @Bean
    public RefundRequestMapper refundRequestMapper()
    {
        return new RefundRequestMapper();
    }
    
    @Bean
    public RefundReasonMapper refundReasonMapper()
    {
        return new RefundReasonMapper();
    }
    
    @Bean
    public TransferRequestMapper transferRequestMapper()
    {
        return new TransferRequestMapper();
    }
    
    @Bean
    public AppPropertyMapper appPropertyMapper()
    {
        return new AppPropertyMapper();
    }
    
    @Bean
    public AccountCheckErrorMapper accountCheckErrorMapper()
    {
        return new AccountCheckErrorMapper();
    }
}
