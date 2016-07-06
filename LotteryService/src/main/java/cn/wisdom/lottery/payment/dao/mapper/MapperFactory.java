/**
 * MapperFactory.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 11, 2015
 */
package cn.wisdom.lottery.payment.dao.mapper;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import cn.wisdom.lottery.payment.dao.mapper.PrizeLotteryMapper.SSQPeriodInfoMapper;
import cn.wisdom.lottery.payment.dao.mapper.PrizeLotteryMapper.SSQPrizeInfoMapper;

/**
 * MapperFactory
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[DAO] 1.0
 */
@Service
public class MapperFactory
{
    @Bean
    public LotteryMapper createLotteryMapper()
    {
        return new LotteryMapper();
    }
    
    @Bean
    public LotteryNumberMapper createLotteryNumberMapper()
    {
    	return new LotteryNumberMapper();
    }
    
    @Bean
    public LotteryPeriodMapper createLotteryPeriodMapper()
    {
    	return new LotteryPeriodMapper();
    }
    
    @Bean
    public SSQPrizeInfoMapper createSSQPrizeInfoMapper()
    {
    	PrizeLotteryMapper prizeLotteryMapper = new PrizeLotteryMapper();
    	
    	return prizeLotteryMapper.new SSQPrizeInfoMapper();
    }
    
    @Bean
    public SSQPeriodInfoMapper createSSQPeriodInfoMapper()
    {
    	PrizeLotteryMapper prizeLotteryMapper = new PrizeLotteryMapper();
    	
    	return prizeLotteryMapper.new SSQPeriodInfoMapper();
    }
    
    @Bean
    public UserMapper createUserMapper()
    {
    	return new UserMapper();
    }

    @Bean
    public PermissionMapper createPermissionMapper()
    {
        return new PermissionMapper();
    }

    @Bean
    public UserPermissionMapper createUserPermissionMapper()
    {
        return new UserPermissionMapper();
    }

    @Bean
    public AppPropertyMapper createAppPropertyMapper()
    {
        return new AppPropertyMapper();
    }
}
