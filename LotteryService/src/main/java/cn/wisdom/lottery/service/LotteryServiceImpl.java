package cn.wisdom.lottery.service;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import me.chanjar.weixin.common.exception.WxErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.wisdom.lottery.api.response.CheckRedpackStateResponse;
import cn.wisdom.lottery.api.response.ValidRedpackLottery;
import cn.wisdom.lottery.common.exception.OVTRuntimeException;
import cn.wisdom.lottery.common.log.Logger;
import cn.wisdom.lottery.common.log.LoggerFactory;
import cn.wisdom.lottery.common.model.JsonDocument;
import cn.wisdom.lottery.common.utils.DataConvertUtils;
import cn.wisdom.lottery.common.utils.DateTimeUtils;
import cn.wisdom.lottery.common.utils.MathUtils;
import cn.wisdom.lottery.common.utils.NumberGeneratorUtil;
import cn.wisdom.lottery.dao.LotteryDao;
import cn.wisdom.lottery.dao.constant.BusinessType;
import cn.wisdom.lottery.dao.constant.LotteryType;
import cn.wisdom.lottery.dao.constant.PayState;
import cn.wisdom.lottery.dao.constant.PrizeState;
import cn.wisdom.lottery.dao.threadpool.Task;
import cn.wisdom.lottery.dao.threadpool.TaskExecutor;
import cn.wisdom.lottery.dao.vo.AppProperty;
import cn.wisdom.lottery.dao.vo.Lottery;
import cn.wisdom.lottery.dao.vo.LotteryPeriod;
import cn.wisdom.lottery.dao.vo.LotteryRedpack;
import cn.wisdom.lottery.dao.vo.PageInfo;
import cn.wisdom.lottery.dao.vo.User;
import cn.wisdom.lottery.service.context.SessionContext;
import cn.wisdom.lottery.service.exception.ServiceErrorCode;
import cn.wisdom.lottery.service.exception.ServiceException;
import cn.wisdom.lottery.service.remote.response.LotteryOpenData;
import cn.wisdom.lottery.service.wx.MessageNotifier;
import cn.wisdom.lottery.service.wx.WXService;

@Service
public class LotteryServiceImpl implements LotteryService
{

    @Autowired
    private AppProperty appProperties;

    @Autowired
    private LotteryPrizeService lotteryPublishService;

    @Autowired
    private LotteryPriceService lotteryPriceService;
    
    @Autowired
    private LotteryDistributeService lotteryDistributeService;
    
    @Autowired
    private MessageNotifier messageNotifier;

    @Autowired
    private LotteryDao lotteryDao;

	@Autowired
	private WXService wxService;
    
    @Autowired
    private TaskExecutor executor;
    
    private ConcurrentHashMap<Long, ReadWriteLock> lockMap = new ConcurrentHashMap<Long, ReadWriteLock>();

    private Logger logger = LoggerFactory.getLogger(LotteryService.class
            .getName());

    @Override
    public Lottery createLottery(Lottery lottery) throws ServiceException
    {
        logger.debug("Receive lottery order: {}!", lottery);

        if (!appProperties.wxPaydebug)
        {
            lottery.setTotalFee(lotteryPriceService
                    .calculateLotteryTotalFee(lottery));
        }
        else
        {
            lottery.setTotalFee(1);
        }

        lottery.setOrderNo(NumberGeneratorUtil
                .generateOrderNumber(DataConvertUtils.toString(lottery
                        .getCreateBy())));

        try
        {
            lotteryDao.saveLottery(lottery);
        }
        catch (OVTRuntimeException e)
        {
            String errMsg = MessageFormat.format(
                    "Failed to create lottery order {0}", lottery);
            throw new ServiceException(e.getErrorCode(), errMsg, e);
        }

        return lottery;
    }

    @Override
    public Lottery getLottery(long lotteryId) throws ServiceException
    {
        Lottery lottery = lotteryDao.getLottery(lotteryId);
        
        updateCanSend(lottery);
        
		return lottery;
    }
    
    @Override
    public Lottery getLottery(long lotteryId, boolean queryNumber,
    		boolean queryPeriod, boolean queryRedpack) {
    	
    	return lotteryDao.getLottery(lotteryId, queryNumber, queryPeriod, queryRedpack);
    }
    
    @Override
    public Lottery getLotteryByPeriod(long periodId) {
    	
    	return lotteryDao.getLotteryByPeriod(periodId);
    }

    @Override
    public void distributeTicket(long lotteryId, long merchantId)
            throws ServiceException
    {
        // change lottery ticket_state to 'Distributed' and set merchant
        Lottery lottery = lotteryDao.getLottery(lotteryId, false, false, false);

        if (lottery.getPayState() != PayState.Paid)
        {
            String errMsg = MessageFormat.format(
                    "Ticket in state [{0}], can't be distributed!",
                    lottery.getPayState());
            throw new ServiceException(ServiceErrorCode.INVALID_STATE, errMsg);
        }

        lottery.setMerchant(merchantId);
        lottery.setDistributeTime(DateTimeUtils.getCurrentTimestamp());

        lotteryDao.updateDistributeState(lottery);
    }

    @Override
    public List<Lottery> queryLottery(LotteryType lotteryType, int period,
            long merchantId)
    {

        return lotteryDao.getLottery(lotteryType, period, merchantId);
    }

    @Override
    public void printTicket(long periodId)
            throws ServiceException
    {
        // set lottery print time
        lotteryDao.updatePrintState(periodId);
    }

    @Override
    public void fetchTicket(long userId, long periodId)
            throws ServiceException
    {

        Lottery lottery = lotteryDao.getLotteryByPeriod(periodId);
        if (lottery.getPayState() != PayState.Paid)
        {
            String errMsg = "Lottery is unpaid, can't fetch!";
            throw new ServiceException(ServiceErrorCode.INVALID_STATE, errMsg);
        }

        if (lottery.getOwner() != userId)
        {
            throw new ServiceException(ServiceErrorCode.NO_PRIVILEGE,
                    "Not owner!");
        }

        List<LotteryPeriod> periods = lottery.getPeriods();
        LotteryPeriod lotteryPeriod = periods.get(0);
        lotteryPeriod.setTicketFetchTime(DateTimeUtils.getCurrentTimestamp());
        lotteryDao.updateFetchState(lotteryPeriod);
    }
    
    @Override
    public String submitPayRequest(long lotteryId, String payImgUrl) {

    	logger.info("Receive order pay request: lottery[{}]", lotteryId);

    	String returnUrl = "";
        final Lottery lottery = lotteryDao.getLottery(lotteryId, false, false, false);
    	if (lottery != null && lottery.getPayState() == PayState.UnPaid) {
    		try {
    			File payImg = wxService.getWxMpService().mediaDownload(
    					payImgUrl);
    			lottery.setPayImgUrl(payImg.getName());
    			lottery.setPayState(PayState.PaidApproving);

    			lotteryDao.updatePayImg(lottery);

    			messageNotifier.notifyMerchantNewPayRequest(lottery);
    		} catch (WxErrorException e) {
    			logger.error("failed to upload payImg", e);
    			lottery.setPayImgUrl(payImgUrl);
    		}
    		
    		returnUrl = lottery.getPayImgUrl();
    	}
    	
    	return returnUrl;
    }
    
    @Override
    public void forgetSubmitPayRequest(String openId) {
    	
    	messageNotifier.notifyCustomerHelpOfForgetSubmitPayImg(openId);
    }
    
    @Override
    public void confirmPay(long lotteryId) {
    	final Lottery lottery = lotteryDao.getLottery(lotteryId);

        if (lottery != null && lottery.getPayState() == PayState.PaidApproving) {
            lottery.setPayState(PayState.Paid);

            lotteryDao.updatePayState(lottery);

            // distribute
            distribute(lottery);
            
            // notify customer
            messageNotifier.notifyCustomerPaidSuccess(lottery);
		}
    }
    
    @Override
    public void confirmPayFail(long lotteryId) {
    	final Lottery lottery = lotteryDao.getLottery(lotteryId, false, false, false);
    	
    	if (lottery != null && lottery.getPayState() == PayState.PaidApproving) {
    		lottery.setPayState(PayState.PaidFail);
    		
    		lotteryDao.updatePayState(lottery);
    		
    		// notify customer
    		messageNotifier.notifyCustomerPaidFail(lottery);
    	}
    }
    
    @Override
    public String uploadTicket(long periodId, String ticketImgUrl) {
    	
    	String returUrl = null;
    	try {
			File ticketImg = wxService.getWxMpService().mediaDownload(
					ticketImgUrl);

			returUrl = ticketImg.getName();
			lotteryDao.updateTicketImage(periodId, ticketImg.getName());
			
			messageNotifier.notifyCustomerTicketPrinted(periodId);
		} catch (WxErrorException e) {
			logger.error("failed to upload ticketImg", e);
		}
    	
    	return returUrl;
    }

    @Override
    public void onPaidSuccess(String orderNo, String openid)
            throws ServiceException
    {
    	logger.info("Receive order paid notification from wx: order[{}]", orderNo);
        final Lottery lottery = lotteryDao.getLotteryByOrder(orderNo);

        if (lottery != null && lottery.getPayState() == PayState.UnPaid) {
            lottery.setPayState(PayState.Paid);

            lotteryDao.updatePayState(lottery);

            // distribute
            distribute(lottery);
            
            // notify customer
            messageNotifier.notifyCustomerPaidSuccess(lottery);
		}
    }

	private void distribute(final Lottery lottery) {
		// distribute to merchant.
		executor.submitTask(new Task() {
			
			@Override
			public void execute() {
		        try {
					lotteryDistributeService.distribute(lottery);
				} catch (ServiceException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public String getName() {
				return "LotteryDistribute";
			}
		});
	}
	
	@Override
	public List<Lottery> getPaidLotteries(LotteryType lotteryType, int period) {
		
		return lotteryDao.getPaidLotteries(lotteryType, period);
	}

    @Override
    public void updatePrizeState(int period, PrizeState prizeState)
	{
    	lotteryDao.updatePrizeState(period, prizeState);
	}

    @Override
    public void updatePrizeInfo(List<LotteryPeriod> prizeLotteries, List<LotteryRedpack> prizeLotteryRedpacks)
    {
        lotteryDao.updatePrizeInfo(prizeLotteries, prizeLotteryRedpacks);
    }

    @Override
    public Lottery getMyLatestLottery(long userId)
    {
         return lotteryDao.getLatestLottery(userId);
    }

    @Override
    public List<Lottery> getLotteries(long owner, PageInfo pageInfo) {
    	
    	List<Lottery> lotteries = lotteryDao.getLotteries(owner, pageInfo);
    	for (Lottery lottery : lotteries) {
			updateCanSend(lottery);
		}
    	
		return lotteries;
    }

	private void updateCanSend(Lottery lottery) {
		boolean canSend = true;
		if (lottery.getPayState() != PayState.Paid) {
			canSend = false;
		}
		//表明已经变成红包了，UI中再点应该直接进红包详情页面，可继续分享.
		if (lottery.getBusinessType() == BusinessType.RedPack_Bonus || 
				lottery.getBusinessType() == BusinessType.RedPack_Number) {
			canSend = false;
		}
		if (lottery.getPeriodNum() > 1) {
			canSend = false;
		}
		if (lottery.getPeriods().get(0).getPrizeState() != PrizeState.NotOpen) {
			canSend = false;
		}
		
		lottery.setCanSend(canSend);
	}
    
    @Override
    public List<Lottery> getUnPaidLotteries(long owner) {
    	
    	return lotteryDao.getUnPaidLotteries(owner);
    }
    
    @Override
    public void shareLotteryAsRedpack(long lotteryId, int count, String wish) throws ServiceException {
    	
    	if (count < 0 || count > appProperties.redpackLimitMax) {
			throw new ServiceException(ServiceErrorCode.ERROR_BUSINESS_TYPE, 
					MessageFormat.format("Redpack number must be (1-{0}): {1}", 
							appProperties.redpackLimitMax, count));
		}
    	Lottery lottery = lotteryDao.getLottery(lotteryId, false, true, false);
    	
    	checkIfCanShare(lottery);
    	
    	lottery.setBusinessType(BusinessType.RedPack_Bonus);
    	lottery.setRedpackCount(count);
    	lottery.setWish(wish);
    	
    	lotteryDao.updateAsRedpack(lottery);
    }
    
    private void checkIfCanShare(Lottery lottery) throws ServiceException {
    	updateCanSend(lottery);
		
    	if (!lottery.isCanSend()) {
			throw new ServiceException(ServiceErrorCode.CANNOT_SHARE, "This lottery can't be shared!");
		}
	}

	@Override
    public List<Lottery> getSentRedpackList(long sender) {
    	
    	List<Lottery> lotteries = lotteryDao.getRedpacksBySender(sender);
    	
    	return lotteries;
    }
    
    @Override
    public List<Lottery> getReceivedRedpackList(long receiver) {
    	
    	List<Lottery> lotteries = lotteryDao.getRedpacksByReceiver(receiver);
    	
    	return lotteries;
    }
    
    @Override
    public CheckRedpackStateResponse checkRedpackState(long lotteryId, long userId) throws ServiceException {
    	
    	CheckRedpackStateResponse response = new CheckRedpackStateResponse();
    	
		Lottery lottery = this.getLottery(lotteryId, false, true, true);
    	
    	try {
			checkRedpack(lottery, userId);
			response.setStatus(JsonDocument.STATE_SUCCESS);
		} catch (ServiceException e) {
			response.setStatus(e.getErrorCode());
		} finally {
			response.setSender(lottery.getOwner());
			response.setWish(lottery.getWish());
		}
    	
		return response;
    }
    
    @Override
    public List<ValidRedpackLottery> getValidRedpackLotteries(long userId) throws ServiceException {
    	
    	LotteryOpenData currentPeriod = lotteryPublishService.getCurrentPeriod(LotteryType.SSQ);
    	
    	List<Lottery> lotteries = lotteryDao.getValidRedpackLotteries(userId, DataConvertUtils.toInt(currentPeriod.getExpect()));
    	
    	List<ValidRedpackLottery> list = new ArrayList<ValidRedpackLottery>();
    	for (Lottery lottery : lotteries) {
    		ValidRedpackLottery validRedpackLottery = new ValidRedpackLottery(lottery, currentPeriod);
    		list.add(validRedpackLottery);
		}
		return list;
    }

	private void checkRedpack(Lottery lottery, long userId)
			throws ServiceException {
    	// snatched
    	for (LotteryRedpack redpack : lottery.getRedpacks()) {
			if (redpack.getUserId() == userId) {
				throw new ServiceException(ServiceErrorCode.SNATCHED, "You have snatched this redpack!");
			}
		}
    	
    	// expired
    	LotteryPeriod period = lottery.getPeriods().get(0);
		if (DateTimeUtils.getCurrentTimestamp().after(period.getPrizeOpenTime())) {
			throw new ServiceException(ServiceErrorCode.REDPACK_EXPIRED, "Redpack has expired.");
		}
    	
    	// empty
    	if (lottery.getRedpackCount() <= lottery.getSnatchedNum()) {
			throw new ServiceException(ServiceErrorCode.REDPACK_EMPTY, "Redpack is empty!");
		}
	}

	@Override
	public int snatchRedpack(long lotteryId) throws ServiceException {
		// check state
		User user = SessionContext.getCurrentUser();
		
		int rate = 0;
		
		ReadWriteLock readWriteLock = lockMap.get(lotteryId);
		if (readWriteLock == null) {
			readWriteLock = new ReentrantReadWriteLock();
			lockMap.putIfAbsent(lotteryId, readWriteLock);
		}
		Lock writeLock = readWriteLock.writeLock();
		writeLock.lock();
		
		Lottery lottery = null;
		try {
			lottery = this.getLottery(lotteryId, false, true, true);
			
			checkRedpack(lottery, user.getId());
			
			if (lottery.getBusinessType() == BusinessType.RedPack_Bonus) {
				rate = snatchBonusRedpack(lottery);
			}
			else {  // (lottery.getBusinessType() == BusinessType.RedPack_Number) 
				rate = snatchNumberRedpack(lottery);
			}
//			else {
//				throw new ServiceException(ServiceErrorCode.ERROR_BUSINESS_TYPE, "Error lottery business type.");
//			}
			
			lotteryDao.increaseSnatchNum(lotteryId);
			
			messageNotifier.notifySenderRedpackSnatched(lottery.getOwner(), user.getNickName());
		} finally
		{
			writeLock.unlock();
		}
		
		return rate;
	}

	private int snatchNumberRedpack(Lottery lottery) {
		
		return 0;
	}

	private int snatchBonusRedpack(Lottery lottery)
			throws ServiceException {
		User user = SessionContext.getCurrentUser();
		
		// record snatched redpack info: rate, user
		int rate = generateRedpackRate(lottery.getRedpackCount(), lottery.getRedpacks());
		LotteryRedpack redpack = new LotteryRedpack();
		redpack.setLotteryId(lottery.getId());
		redpack.setUserId(user.getId());
		redpack.setRate(rate);
		redpack.setAcquireTime(DateTimeUtils.getCurrentTimestamp());
		lottery.getRedpacks().add(redpack);
		
		lotteryDao.saveRedpack(redpack);
		
		return rate;
	}

	private int generateRedpackRate(int redpackCount,
			List<LotteryRedpack> redpacks) {
		int rate = 0;
		
		int remainCount = redpackCount - redpacks.size();
		int remainRate = 100;
		
		for (LotteryRedpack lotteryRedpack : redpacks) {
			remainRate -= lotteryRedpack.getRate();
		}
		
		rate = randomRate(remainCount, remainRate);
		
		return rate;
	}

	private int randomRate(int remainCount, int remainRate) {
		int rate = 0;
		if (remainCount > 1) {
			int min = 1;
			int max = remainRate / remainCount * 2;
			rate = MathUtils.rand(max);
			rate = rate <= min ? min : rate;
		}
		else if (remainCount == 1) {
			rate = remainRate;
		}
		return rate;
	}
	
	
	
	@Override
	public void clearUnpaidLottery() {
		
		lotteryDao.deleteUnPaidLottery();
		
	}
	
	@Override
	public void deleteLottery(long owner, long lotteryId) {
		
		lotteryDao.deleteLottery(owner, lotteryId);
	}
}
