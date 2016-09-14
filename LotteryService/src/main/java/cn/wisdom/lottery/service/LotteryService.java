/**
 * OrderService.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * Dec 29, 2015
 */
package cn.wisdom.lottery.service;

import java.util.List;

import cn.wisdom.lottery.api.response.CheckRedpackStateResponse;
import cn.wisdom.lottery.api.response.ValidRedpackLottery;
import cn.wisdom.lottery.dao.constant.LotteryType;
import cn.wisdom.lottery.dao.constant.PrizeState;
import cn.wisdom.lottery.dao.vo.Lottery;
import cn.wisdom.lottery.dao.vo.LotteryPeriod;
import cn.wisdom.lottery.dao.vo.LotteryRedpack;
import cn.wisdom.lottery.dao.vo.PageInfo;
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
	 * Get lottery by id.
	 * 
	 * @return
	 */
	Lottery getLottery(long lotteryId) throws ServiceException;

	Lottery getLottery(long lotteryId, boolean queryNumber,
			boolean queryPeriod, boolean queryRedpack);

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
	 * @param prizeLotteryRedpacks 
	 */
	void updatePrizeInfo(List<LotteryPeriod> prizeLotteries, List<LotteryRedpack> prizeLotteryRedpacks);

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
	 * @param pageInfo 
	 * @param limit
	 * @return
	 */
	List<Lottery> getLotteries(long owner, PageInfo pageInfo);
	
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
	int snatchRedpack(long lotteryId) throws ServiceException;

	/**
	 * Submit pay screenshot.
	 * 
	 * @param lotteryId
	 * @param payImgUrl
	 */
	String submitPayRequest(long lotteryId, String payImgUrl);

	/**
	 * Get user unpaid lottery.
	 * 
	 * @param owner
	 * @return
	 */
	List<Lottery> getUnPaidLotteries(long owner);

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

	/**
	 * Clear unpaid lottery.
	 * 
	 * @param period
	 */
	void clearUnpaidLottery();

	/**
	 * Delete lottery.
	 * 
	 * @param owner
	 * @param lotteryId
	 */
	void deleteLottery(long owner, long lotteryId);

	/**
	 * Share lottery as redpack.
	 * 
	 * @param lotteryId
	 * @param count
	 * @param wish 
	 * @throws ServiceException 
	 */
	void shareLotteryAsRedpack(long lotteryId, int count, String wish) throws ServiceException;

	/**
	 * Send redpack list.
	 * 
	 * @param sender
	 * @return
	 */
	List<Lottery> getSentRedpackList(long sender);

	/**
	 * Received redpack list.
	 * 
	 * @param receiver
	 * @return
	 */
	List<Lottery> getReceivedRedpackList(long receiver);

	/**
	 * Check redpack state.
	 * 
	 * @param lotteryId
	 * @param userId
	 * @throws ServiceException 
	 */
	CheckRedpackStateResponse checkRedpackState(long lotteryId, long userId) throws ServiceException;

	/**
	 * Get lotteries that can be shared as redpack.
	 * 
	 * @param userId
	 * @return
	 * @throws ServiceException 
	 */
	List<ValidRedpackLottery> getValidRedpackLotteries(long userId) throws ServiceException;

	/**
	 * Notify user help info once he/she forgot upload pay img.
	 * 
	 * @param openId
	 */
	void forgetSubmitPayRequest(String openId);

	/**
	 * Transfer lottery from one merchant to another.
	 * 
	 * @param period
	 * @param fromMerchant
	 * @param toMerchant
	 */
	void transferMerchant(int period, long fromMerchant, long toMerchant);
	
}
