/**
 * OrderServiceImpl.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * Dec 29, 2015
 *//*
package cn.wisdom.lottery.payment.service;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.wisdom.lottery.payment.common.log.Logger;
import cn.wisdom.lottery.payment.common.log.LoggerFactory;
import cn.wisdom.lottery.payment.common.utils.CollectionUtils;
import cn.wisdom.lottery.payment.common.utils.DataConvertUtils;
import cn.wisdom.lottery.payment.common.utils.DateTimeUtils;
import cn.wisdom.lottery.payment.common.utils.StringUtils;
import cn.wisdom.lottery.payment.dao.PrizeLotteryDao;
import cn.wisdom.lottery.payment.dao.constant.ActionType;
import cn.wisdom.lottery.payment.dao.constant.ActivateType;
import cn.wisdom.lottery.payment.dao.constant.MemberProfileStatus;
import cn.wisdom.lottery.payment.dao.constant.ResourceType;
import cn.wisdom.lottery.payment.dao.constant.TicketState;
import cn.wisdom.lottery.payment.dao.vo.PrizeLotterySSQ;

import com.ovt.order.util.entity.Order;

import cn.wisdom.lottery.payment.service.context.SessionContext;
import cn.wisdom.lottery.payment.service.exception.RemoteServiceException;
import cn.wisdom.lottery.payment.service.exception.ServiceErrorCode;
import cn.wisdom.lottery.payment.service.exception.ServiceException;
import cn.wisdom.lottery.payment.service.mo.LotteryDetail;
import cn.wisdom.lottery.payment.service.remote.OVPaymentProxy;
import cn.wisdom.lottery.payment.service.remote.ResourceProxy;
import cn.wisdom.lottery.payment.service.remote.UserProxy;
import cn.wisdom.lottery.payment.service.ssq.LotteryPublishService;

import com.ovt.order.util.entity.OrderItem;
import com.ovt.order.util.entity.RefundRequest;
import com.ovt.order.util.exception.PaymentSDKException;

*//**
 * OrderServiceImpl
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 *//*
@Service
public class OrderServiceImpl
{
    @Autowired
    private PrizeLotteryDao lotterySSQDao;
    
    @Autowired
    private LotteryPrizeService ssqPublishService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private LotteryPriceService priceService;

    @Autowired
    private MemberActionService memberActionService;

    @Autowired
    private OVPaymentProxy ovPaymentProxy;

    @Autowired
    private AppPropertiesService appProperties;

    @Autowired
    @Qualifier("ResourceProxy")
    private ResourceProxy resourceProxy;

    @Autowired
    @Qualifier("MockResourceProxy")
    private ResourceProxy mockResourceProxy;

    @Autowired
    @Qualifier("UserProxy")
    private UserProxy userProxy;

    @Autowired
    @Qualifier("MockUserProxy")
    private UserProxy mockUserProxy;

    private Logger logger = LoggerFactory.getLogger(LotteryService.class.getName());

    @PostConstruct
    private void init()
    {
        if (appProperties.isDebugMode())
        {
            userProxy = mockUserProxy;
            resourceProxy = mockResourceProxy;
        }
    }

    
     * (non-Javadoc)
     * 
     * @see
     * cn.wisdom.lottery.payment.service.OrderService#createOrder(cn.wisdom.lottery
     * .payment.dao.vo.OrderRequest)
     
    @Override
    public LotteryDetail createOrder(PrizeLotterySSQ lotterySSQ) throws ServiceException
    {
        logger.debug("Receive ssq order: {}!", lotterySSQ);

        LotteryDetail response = new LotteryDetail();
        response.setLotterySSQ(lotterySSQ);
        
        // 0. delete it, then create a new one, if is old order
        if (StringUtils.isNotBlank(lotterySSQ.getOrderNo()))
        {
//            this.cancelOrder(orderRequest.getDeviceId(), lotterySSQ.getOrderNo());
        }

        // 1. validate request
        checkOrderRequest(lotterySSQ);
        
        // 2. save lottery
        initPrivateLottery(lotterySSQ);
        lotterySSQDao.saveLotterySSQ(lotterySSQ);

        // 3. create order
        com.ovt.order.util.entity.Order paymentOrder = createSSQPaymentOrder(lotterySSQ);

        // 4. calculate fee
        priceService.calculateSSQOrderFee(lotterySSQ, paymentOrder);
        
        // 5. save order
        try
        {
            if (appProperties.isDebugPay())
            {
                paymentOrder.setOrderTotalFee(0.01f);
            }
            paymentOrder = ovPaymentProxy.getOVPayment().createOrder(paymentOrder);
        }
        catch (PaymentSDKException e)
        {
            String errMsg = MessageFormat.format("Failed to create ssq order {0}", lotterySSQ);
            throw new ServiceException(e.getErrorCode(), errMsg);
        }
        response.setOrder(paymentOrder);

        return response;
    }
    
    private void initPrivateLottery(PrizeLotterySSQ lotterySSQ) throws ServiceException
    {
    	lotterySSQ.setOwner(SessionContext.getCurrentUser().getId());
    	lotterySSQ.setTicketState(TicketState.NotPrint);
    	
    	int time = ssqPublishService.getCurrentTime();
    	lotterySSQ.setTime(time);
	}

	@Override
    public void printTicket(String orderNo, long mechantId) throws ServiceException {
    	// change lottery ticket_state to 'print' and set merchant and print time
    	
    }
    
    @Override
    public void fetchTicket(String orderNo) {
    	// check owner
    	
    	// change lottery ticket_state to 'FETCH_TICKET'
    	
    }

    *//**
     * @param orderRequest
     * @throws ServiceException
     *//*
    private void checkOrderRequest(PrizeLotterySSQ lotterySSQ) throws ServiceException
    {
    	// 1. red ball num >=6 & blue ball num >= 1  & all number is valid
    	
    	// 2. must be during allowed time
    	
        if (StringUtils.isBlank(orderRequest.getDeviceId()))
        {
            throw new ServiceException(ServiceErrorCode.NULL_DEVICE_ID,
                    "DeviceId can not be empty! ");
        }
        
    }

    private com.ovt.order.util.entity.Order createSSQPaymentOrder(PrizeLotterySSQ lotterySSQ)
            throws ServiceException
    {
        // payment order
        com.ovt.order.util.entity.Order order = new com.ovt.order.util.entity.Order();

        order.setCreateBy(currentUser.getUserId());
        order.setUserId(order.getCreateBy());

        if (order.getOrderItemList() == null)
        {
            order.setOrderItemList(new ArrayList<OrderItem>());
        }

        OrderItem ssq = new OrderItem();
        ssq.setProductNo("SSQ");
        ssq.setProductName("Shuang Se Qiu");
        ssq.setProductPrice(2);
        ssq.setProductNum(1); //?
        order.getOrderItemList().add(ssq);

        return order;
    }
    
    @Override
    public LotteryDetail getOrderDetailSSQ(String orderNo)
    		throws ServiceException {
    	LotteryDetail orderDetailSSQ = new LotteryDetail();
    	
    	Order order = this.getOrderDetail(orderNo);
    	orderDetailSSQ.setOrder(order);
    	
    	PrizeLotterySSQ lotterySSQ = lotterySSQDao.getLotterySSQ(orderNo);
    	orderDetailSSQ.setLotterySSQ(lotterySSQ);
    	
    	return null;
    }

    
     * (non-Javadoc)
     * 
     * @see
     * cn.wisdom.lottery.payment.service.OrderService#onPaidSuccess(java.lang
     * .String)
     
    @Override
    public void onPaidSuccess(String userId, String orderNo) throws ServiceException
    {
        logger.debug("Receive order paid notification, userId: {}, order: {}!", userId, orderNo);

        // 1. get member profile
        MemberProfile memberProfile = memberProfileService.getMemberProfile(deviceId);

        // 2. make sure the callback message is not duplicate
        if (isDuplicateOrder(memberProfile, orderNo))
        {
            logger.info("Received duplicate order paid callback from PaymentService: {}", orderNo);
            return;
        }

        // 2. get order
        com.ovt.order.util.entity.Order order = null;
        try
        {
            order = ovPaymentProxy.getOVPayment().getOrderDetail(orderNo);
        }
        catch (PaymentSDKException e)
        {
            String errMsg = MessageFormat.format("Failed to get detail of order [{0}]", orderNo);
            throw new ServiceException(e.getErrorCode(), errMsg);
        }

        // 3. create a subProfile
        SubMemberProfile subMemberProfile = this.newSubMemberProfile(order);
        memberProfile.getSubProfiles().add(subMemberProfile);

        // 4. log action
        MemberAction memberAction = this.createOrderMemberAction(order, subMemberProfile.getId());
        memberActionService.log(memberAction);

        // 5. notify business server
        List<MemberProfile> profiles = new ArrayList<MemberProfile>();
        profiles.add(memberProfile);
        this.resourceProxy.notifyResoucesChangeSync(profiles);

    }

    *//**
     * @param memberProfile
     * @param orderNo
     *//*
    private boolean isDuplicateOrder(MemberProfile memberProfile, String orderNo)
    {
        boolean isDuplicate = false;
        for (SubMemberProfile subMemberProfile : memberProfile.getSubProfiles())
        {
            if (StringUtils.equals(subMemberProfile.getOrderNo(), orderNo))
            {
                isDuplicate = true;
            }
        }

        return isDuplicate;
    }

    *//**
     * @param order
     * @param purchaseType
     * @return
     *//*
    private MemberAction createOrderMemberAction(com.ovt.order.util.entity.Order order,
            long profileId)
    {
        MemberAction memberAction = new MemberAction();
        memberAction.setUserId(order.getCreateBy());
        memberAction.setDeviceId(order.getUserId());
        memberAction.setMemberProfileId(profileId);
        memberAction.setType(ActionType.PURCHASE);
        memberAction.setTotalFee(order.getOrderTotalFee());
        memberAction.setReferenceId(order.getOrderNo());
        memberAction.setCreateTime(DateTimeUtils.getCurrentTimestamp());
        return memberAction;
    }

    *//**
     * New purchase, means old resources are default.
     * 
     * @param memberProfile
     * @param order
     * @throws ServiceException
     *//*
    private SubMemberProfile newSubMemberProfile(com.ovt.order.util.entity.Order order)
            throws ServiceException
    {
        List<OrderItem> items = order.getOrderItemList();

        SubMemberProfile subMemberProfile = new SubMemberProfile();
        int months = 0;

        if (items != null && items.size() > 0)
        {
            months = items.get(0).getProductNum();
        }

        ActivateType activateType = ActivateType.getValue(order.getExtra2());
        Range<String> validTime = Order.calculateValidTime(activateType, months);

        Timestamp startTime = DateTimeUtils.toTimestamp(DateTimeUtils.parseDate(
                validTime.getFrom(), DateTimeUtils.PATTERN_SQL_DATETIME_FULL));
        Timestamp expireTime = DateTimeUtils.toTimestamp(DateTimeUtils.parseDate(validTime.getTo(),
                DateTimeUtils.PATTERN_SQL_DATETIME_FULL));
        subMemberProfile.setStartTime(startTime);
        subMemberProfile.setExpireTime(expireTime);

        subMemberProfile.setDeviceId(order.getUserId());
        subMemberProfile.setOrderNo(order.getOrderNo());
        subMemberProfile.setMonth(months);
        if (DateTimeUtils.getCurrentTimestamp().after(startTime))
        {
            subMemberProfile.setStatus(MemberProfileStatus.ACTIVE);
        }
        else
        {
            subMemberProfile.setStatus(MemberProfileStatus.INACTIVE);
        }

        subMemberProfile.setCreateBy(order.getCreateBy());
        subMemberProfile.setCreateTime(DateTimeUtils.getCurrentTimestamp());
        subMemberProfile.setPurchaseWay(ActionType.PURCHASE);

        for (OrderItem orderItem : items)
        {
            Resource resource = resourceService.getResource(orderItem.getProductNo());

            subMemberProfile.addResource(resource);
        }

        memberProfileService.saveProfile(subMemberProfile);

        return subMemberProfile;

    }

    
     * (non-Javadoc)
     * 
     * @see
     * cn.wisdom.lottery.payment.service.OrderService#onRefundSuccess(java.lang
     * .String)
     
    @Override
    public void onRefundSuccess(Collection<Long> refundRequestIds) throws ServiceException
    {
        if (CollectionUtils.isNotEmpty(refundRequestIds))
        {
            try
            {
                Set<String> expiredProfileDevice = new HashSet<String>();

                final List<RefundRequest> refundRequests = ovPaymentProxy.getOVPayment()
                        .getRefundRequest(refundRequestIds);
                for (RefundRequest refundRequest : refundRequests)
                {
                    // 1. Get the sub member profile by order number
                    SubMemberProfile subMemberProfile = memberProfileService
                            .getSubMemberProfile(refundRequest.getOrderNo());

                    // 2. Update member profile status to Cancelled
                    memberProfileService.cancelProfile(subMemberProfile,
                            refundRequest.getCreateTime(), false);
                    if (subMemberProfile.getStatus() == MemberProfileStatus.EXPIRED)
                    {
                        expiredProfileDevice.add(subMemberProfile.getDeviceId());
                    }

                    // 3. log action
                    logRefundAction(refundRequest.getCreateBy(), subMemberProfile, refundRequest);
                }

                // 4. notify business server
                if (CollectionUtils.isNotEmpty(expiredProfileDevice))
                {
                    List<MemberProfile> profiles = new ArrayList<MemberProfile>();
                    for (String deviceId : expiredProfileDevice)
                    {
                        profiles.add(memberProfileService.getMemberProfile(deviceId));
                    }
                    this.resourceProxy.notifyResoucesChangeSync(profiles);
                }
            }
            catch (PaymentSDKException e)
            {
                String errMsg = MessageFormat.format("Failed to get refund requests {0}!",
                        refundRequestIds);
                throw new ServiceException(e.getErrorCode(), errMsg);
            }
        }
    }

    private void logRefundAction(String userId, SubMemberProfile subMemberProfile,
            RefundRequest refundRequest) throws ServiceException
    {
        MemberAction memberAction = new MemberAction();
        memberAction.setUserId(userId);
        memberAction.setDeviceId(subMemberProfile.getDeviceId());
        memberAction.setMemberProfileId(subMemberProfile.getId());
        memberAction.setType(ActionType.CANCEL);
        memberAction.setTotalFee(refundRequest.getRefundFee());
        memberAction.setReferenceId(DataConvertUtils.toString(refundRequest.getId()));
        memberAction.setCreateTime(DateTimeUtils.getCurrentTimestamp());

        memberActionService.log(memberAction);
    }

    
     * (non-Javadoc)
     * 
     * @see
     * cn.wisdom.lottery.payment.service.OrderService#getOrderDetail(java.lang
     * .String,java.lang.String)
     
    @Override
    public Order getOrderDetail(String orderNo) throws ServiceException
    {
    	com.ovt.order.util.entity.Order paymentOrder = null;
        try
        {
            paymentOrder = ovPaymentProxy.getOVPayment()
                    .getOrderDetail(orderNo);
        }
        catch (PaymentSDKException e)
        {
            String errMsg = MessageFormat.format("Failed to get detail of order [{0}]", orderNo);
            throw new ServiceException(e.getErrorCode(), errMsg);
        }

        return paymentOrder;
    }

    
     * (non-Javadoc)
     * 
     * @see
     * cn.wisdom.lottery.payment.service.OrderService#getOrderList(java.lang.
     * String)
     
    @Override
    public List<Order> getOrderList(String deviceId) throws ServiceException
    {
        List<Order> orders = new LinkedList<Order>();
        try
        {
            List<com.ovt.order.util.entity.Order> orderList = ovPaymentProxy.getOVPayment()
                    .getOrderList(deviceId);

            if (CollectionUtils.isNotEmpty(orderList))
            {
                Order order;
                for (com.ovt.order.util.entity.Order paymentOrder : orderList)
                {
                    order = this.createLotterySSQ(paymentOrder);
                    orders.add(order);
                }

                this.updateUserDeviceName(orders);
            }
        }
        catch (PaymentSDKException e)
        {
            String errMsg = MessageFormat.format("Failed to get orders of device [{0}]!", deviceId);
            throw new ServiceException(e.getErrorCode(), errMsg);
        }

        return orders;
    }

    *//**
     * @param orders
     * @throws ServiceException
     *//*
    private void updateUserDeviceName(Order order)
    {
        List<Long> userIds = new ArrayList<Long>();
        List<Long> deviceIds = new ArrayList<Long>();

        userIds.add(DataConvertUtils.toLong(order.getUserId()));
        deviceIds.add(DataConvertUtils.toLong(order.getDeviceId()));

        try
        {
            final Map<Long, String> userNameByIds = userProxy.getUserNameByIds(userIds);
            final Map<Long, String> deviceNameByIds = userProxy.getDeviceNameByIds(deviceIds);

            order.setUserName(DataConvertUtils.toString(userNameByIds.get(DataConvertUtils
                    .toLong(order.getUserId()))));
            order.setDeviceName(DataConvertUtils.toString(deviceNameByIds.get(DataConvertUtils
                    .toLong(order.getDeviceId()))));
        }
        catch (RemoteServiceException e)
        {
            String errMsg = MessageFormat.format(
                    "Failed to get user name or device name of order [{0}]!", order.getOrderNo());
            logger.error(errMsg, e);
        }
    }

    *//**
     * @param orders
     * @throws ServiceException
     *//*
    private void updateUserDeviceName(List<Order> orders) throws ServiceException
    {
        List<Long> userIds = new ArrayList<Long>();
        List<Long> deviceIds = new ArrayList<Long>();

        for (Order order : orders)
        {
            userIds.add(DataConvertUtils.toLong(order.getUserId()));
            deviceIds.add(DataConvertUtils.toLong(order.getDeviceId()));
        }

        try
        {
            final Map<Long, String> userNameByIds = userProxy.getUserNameByIds(userIds);
            final Map<Long, String> deviceNameByIds = userProxy.getDeviceNameByIds(deviceIds);

            for (Order order : orders)
            {
                order.setUserName(DataConvertUtils.toString(userNameByIds.get(DataConvertUtils
                        .toLong(order.getUserId()))));
                order.setDeviceName(DataConvertUtils.toString(deviceNameByIds.get(DataConvertUtils
                        .toLong(order.getDeviceId()))));
            }
        }
        catch (RemoteServiceException e)
        {
            logger.error("Failed to get user name or device name for orders!", e);
        }
    }

    *//**
     * @param deviceId
     * @param paymentOrder
     * @return
     * @throws ServiceException
     *//*
    public Order createLotterySSQ(com.ovt.order.util.entity.Order paymentOrder)
            throws ServiceException
    {
        Order order = new Order();
        order.setUserId(paymentOrder.getCreateBy());
        order.setDeviceId(paymentOrder.getUserId());
        order.setState(paymentOrder.getOrderState().toString());
        order.setTotalFee(paymentOrder.getOrderTotalFee());
        order.setOrderNo(paymentOrder.getOrderNo());
        order.setCreateTime(DateTimeUtils.formatSqlDateTime(paymentOrder.getCreateTime()));
        order.setRemark(paymentOrder.getOrderRemark());

        // space, flow, month
        List<OrderItem> orderItems = paymentOrder.getOrderItemList();
        if (CollectionUtils.isNotEmpty(orderItems))
        {
            order.setMonth((int) orderItems.get(0).getProductNum());

            for (OrderItem orderItem : orderItems)
            {
                Resource resource = resourceService.getResource(orderItem.getProductNo());

                if (resource.getType() == ResourceType.SPACE)
                {
                    order.setSpace(resource.getSize());
                }
                else if (resource.getType() == ResourceType.FLOW)
                {
                    order.setFlow(resource.getSize());
                }
            }
        }

        ActivateType activateType = ActivateType.getValue(paymentOrder.getExtra2());
        Range<String> validTime = Order.calculateValidTime(activateType, order.getMonth());
        order.setValidTime(validTime);
        order.setActivateType(activateType);

        return order;
    }

    
     * (non-Javadoc)
     * 
     * @see
     * cn.wisdom.lottery.payment.service.OrderService#getUpaidOrderList(java.
     * lang.String)
     
    @Override
    public List<Order> getUnpaidOrderList(String deviceId) throws ServiceException
    {
        List<Order> orders = new ArrayList<Order>();
        String userId = DataConvertUtils.toString(SessionContext.getCurrentUser().getId());
        try
        {
            List<com.ovt.order.util.entity.Order> orderList = ovPaymentProxy.getOVPayment()
                    .getUnPaidOrderList(deviceId, userId, true);

            if (CollectionUtils.isNotEmpty(orderList))
            {
                Order order;
                for (com.ovt.order.util.entity.Order paymentOrder : orderList)
                {
                    order = this.createLotterySSQ(paymentOrder);

                    orders.add(order);
                }

                this.updateUserDeviceName(orders);
            }
        }
        catch (PaymentSDKException e)
        {
            String errMsg = MessageFormat
                    .format("Failed to get unPaid orders of user [{0}] and device [{1}]!", userId,
                            deviceId);
            throw new ServiceException(e.getErrorCode(), errMsg);
        }

        return orders;
    }

    
     * (non-Javadoc)
     * 
     * @see
     * cn.wisdom.lottery.payment.service.OrderService#deleteOrder(java.lang.String
     * )
     
    @Override
    public boolean deleteOrder(String deviceId, String orderNo) throws ServiceException
    {
        boolean success = false;
        try
        {
            success = ovPaymentProxy.getOVPayment().deleteOrder(deviceId, orderNo);
        }
        catch (PaymentSDKException e)
        {
            String errMsg = MessageFormat.format("Failed to delete order [{0}] of device [{1}]!",
                    orderNo, deviceId);
            throw new ServiceException(e.getErrorCode(), errMsg);
        }
        return success;
    }

    
     * (non-Javadoc)
     * 
     * @see
     * cn.wisdom.lottery.payment.service.OrderService#cancelOrder(java.lang.String
     * , java.lang.String)
     
    @Override
    public boolean cancelOrder(String deviceId, String orderNo) throws ServiceException
    {
        boolean success = false;
        try
        {
            success = ovPaymentProxy.getOVPayment().cancelOrder(deviceId, orderNo);
        }
        catch (PaymentSDKException e)
        {
            String errMsg = MessageFormat.format("Failed to cancel order [{0}] of device [{1}]!",
                    orderNo, deviceId);
            throw new ServiceException(e.getErrorCode(), errMsg);
        }
        return success;

    }

    @Override
    public String asyncAliNotify(String orderNo, float totalFee) throws ServiceException
    {
        String ret = "";
        try
        {
            ret = ovPaymentProxy.getOVPayment().testPayAliNitify(orderNo, totalFee);
        }
        catch (PaymentSDKException e)
        {
            String errMsg = MessageFormat.format("Failed to test paid alinotify for orderNo: {0}!",
                    orderNo);
            throw new ServiceException(e.getErrorCode(), errMsg);
        }

        return ret;
    }

    @Override
    public String asyncAliNotify(String orderNo, float totalFee, String time)
            throws ServiceException
    {
        String ret = "";
        try
        {
            ret = ovPaymentProxy.getOVPayment().testPayAliNitify(orderNo, totalFee, time);
        }
        catch (PaymentSDKException e)
        {
            String errMsg = MessageFormat.format("Failed to test paid alinotify for orderNo: {0}!",
                    orderNo);
            throw new ServiceException(e.getErrorCode(), errMsg);
        }

        return ret;
    }

    
     * (non-Javadoc)
     * 
     * @see
     * cn.wisdom.lottery.payment.service.OrderService#setOrderAppPaid(java.lang
     * .String, java.lang.String)
     
    @Override
    public com.ovt.order.util.entity.Order appAliNotify(Map<String, String[]> parameterMap)
            throws ServiceException
    {
        com.ovt.order.util.entity.Order order = null;
        try
        {
            order = ovPaymentProxy.getOVPayment().setOrderAppPaid(parameterMap);
        }
        catch (PaymentSDKException e)
        {
            String errMsg = MessageFormat.format("Failed to set order app paid [{0}]]!",
                    parameterMap);
            throw new ServiceException(e.getErrorCode(), errMsg);
        }

        return order;
    }
}
*/