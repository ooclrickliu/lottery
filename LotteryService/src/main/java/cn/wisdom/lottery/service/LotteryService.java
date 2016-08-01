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
import cn.wisdom.lottery.dao.vo.Lottery;
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
	Lottery createPrivateOrder(Lottery lottery)
			throws ServiceException;

	/**
	 * Get lottery by order.
	 * 
	 * @return
	 */
	Lottery getLottery(String orderNo) throws ServiceException;
	
	/**
	 * Distribute ticket to merchant.
	 * 
	 * @param orderNo
	 * @param merchantId
	 */
	void distributeTicket(String orderNo, long merchantId) throws ServiceException;
	
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
	void printTickets(List<String> orderNos, long merchantId) throws ServiceException;
	
	/**
	 * Owner get ticket from merchant by self.
	 * 
	 * @param orderNo
	 */
	void fetchTicket(String orderNo, long userId) throws ServiceException;
	
	/**
	 * Get all printed tickets by type and time.
	 * 
	 * @param lotteryType
	 * @param time
	 * @return
	 * @throws ServiceException
	 */
	List<Lottery> getPrintedLotteries(LotteryType lotteryType, int period) throws ServiceException;

	/**
	 * Update lottery prize info.
	 * 
	 * @param prizeLotteries
	 */
	void updatePrizeInfo(List<Lottery> prizeLotteries);

	/**
	 * Get customer's latest lottery.
	 * 
	 * @param openid
	 * @param lotteryType
	 * @return
	 */
	List<Lottery> getMyLottery(String openid, LotteryType lotteryType, int period, int limit);
	/*********************************************************************************/
//	
//    /**
//     * Get all orders.
//     * 
//     * @param deviceId
//     * @throws ServiceException
//     */
//    List<Order> getOrderList(String deviceId) throws ServiceException;
//
//    /**
//     * Get all unpaid orders of current user.
//     * 
//     * @param deviceId
//     * @return
//     */
//    List<Order> getUnpaidOrderList(String deviceId) throws ServiceException;
//
    /**
     * Handle paid success order.
     * 
     * @return
     */
    void onPaidSuccess(String userId, String orderNo) throws ServiceException;
//
//    /**
//     * Handle refund success order.
//     * 
//     * @return
//     */
//    void onRefundSuccess(Collection<Long> refundRequestIds) throws ServiceException;
//
//    /**
//     * Get order detail.
//     * 
//     * @return
//     */
//    Order getOrderDetail(String orderNo) throws ServiceException;
//
//    /**
//     * Delete order.
//     * 
//     * @return
//     */
//    boolean deleteOrder(String deviceId, String orderNo) throws ServiceException;
//
//    /**
//     * Cancel wait for pay order.
//     * 
//     * @param deviceId
//     * @param orderNo
//     */
//    boolean cancelOrder(String deviceId, String orderNo) throws ServiceException;
//
//    /**
//     * Convert payment order to doorbell order.
//     * 
//     * @param paymentOrder
//     * @return
//     * @throws ServiceException
//     */
//    Order createLotterySSQ(com.ovt.order.util.entity.Order paymentOrder) throws ServiceException;
//
//    /**
//     * 
//     * @param map
//     * @return
//     * @throws ServiceException
//     */
//    com.ovt.order.util.entity.Order appAliNotify(Map<String, String[]> map) throws ServiceException;
//
//    /**
//     * Just for test
//     * 
//     * @param orderNo
//     * @param totalFee
//     * @return
//     * @throws ServiceException
//     */
//    String asyncAliNotify(String orderNo, float totalFee) throws ServiceException;
//
//    /**
//     * Just for test
//     * 
//     * @param orderNo
//     * @param totalFee
//     * @param time
//     * @return
//     * @throws ServiceException
//     */
//    String asyncAliNotify(String orderNo, float totalFee, String time) throws ServiceException;


	
}
