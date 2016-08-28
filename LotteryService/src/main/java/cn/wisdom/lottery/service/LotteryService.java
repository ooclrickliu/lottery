/**
 * OrderService.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * Dec 29, 2015
 */
package cn.wisdom.lottery.service;

import java.util.List;

import cn.wisdom.lottery.dao.constant.LotteryType;
import cn.wisdom.lottery.dao.constant.PrizeState;
import cn.wisdom.lottery.dao.vo.Lottery;
import cn.wisdom.lottery.dao.vo.LotteryPeriod;
import cn.wisdom.lottery.service.exception.ServiceException;

/**
 * OrderService
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public interface LotteryService
{

	/**
	 * Process user purchase request.
	 * 
	 * @param orderRequest
	 * @return
	 */
	Lottery createLottery(Lottery lottery)
			throws ServiceException;

	/**
	 * Get lottery by order.
	 * 
	 * @return
	 */
	Lottery getLottery(long lotteryId) throws ServiceException;

	/**
	 * Get lottery by period, so just return the specified period and not include other periods.
	 * 
	 * @param periodId
	 * @return
	 */
	Lottery getLotteryByPeriod(long periodId);

	
	/**
	 * Distribute ticket to merchant.
	 * 
	 * @param orderNo
	 * @param merchantId
	 */
	void distributeTicket(long lotteryId, long merchantId) throws ServiceException;
	
	/**
	 * Get all lotteries that distribute current merchant.
	 * 
	 * @param period
	 * @param merchantId
	 * @return
	 */
	List<Lottery> queryLottery(LotteryType lotteryType, int period, long merchantId);
	
	/**
	 * Merchants print tickets after receiving orders;
	 * 
	 * @param orderNo
	 * @throws ServiceException
	 */
	void printTicket(long periodId) throws ServiceException;
	
	/**
	 * Owner get ticket from merchant by self.
	 * 
	 * @param orderNo
	 */
	void fetchTicket(long userId, long periodId) throws ServiceException;

	/**
	 * Get paid lotteries.
	 * 
	 * @param lotteryType
	 * @param period
	 * @return
	 */
	List<Lottery> getPaidLotteries(LotteryType lotteryType, int period);
	
	/**
	 * Get all printed tickets by type and time.
	 * 
	 * @param lotteryType
	 * @param time
	 * @return
	 * @throws ServiceException
	 */
//	List<Lottery> getPrintedLotteries(LotteryType lotteryType, int period) throws ServiceException;

	/**
	 * Update prize state.
	 * 
	 * @param period
	 * @param prizeState
	 */
	void updatePrizeState(int period, PrizeState prizeState);
	
	/**
	 * Update lottery prize info.
	 * 
	 * @param prizeLotteries
	 */
	void updatePrizeInfo(List<LotteryPeriod> prizeLotteries);

	/**
	 * Get customer's latest lottery.
	 * 
	 * @param userId
	 * @return
	 */
	Lottery getMyLatestLottery(long userId);
	
	/**
	 * Get customer's lotteries.
	 * 
	 * @param owner
	 * @param limit
	 * @return
	 */
	List<Lottery> getLotteries(long owner);
	
    /**
     * Handle paid success order.
     * 
     * @return
     */
    void onPaidSuccess(String orderNo, String openid) throws ServiceException;

    /**
     * Redpack.
     * 
     * @param lotteryId
     * @return 
     * @throws ServiceException 
     */
	Lottery snatchRedpack(long lotteryId) throws ServiceException;

	/**
	 * Submit pay screenshot.
	 * 
	 * @param lotteryId
	 * @param payImgUrl
	 */
	void submitPayRequest(long lotteryId, String payImgUrl);

	/**
	 * Merchant confirm customer's pay request.
	 * 
	 * @param lotteryId
	 */
	void confirmPay(long lotteryId);

	void confirmPayFail(long lotteryId);

	/**
	 * Merchant upload ticket image after printed.
	 * 
	 * @param periodId
	 * @param ticketImgUrl
	 */
	String uploadTicket(long periodId, String ticketImgUrl);
	
}
