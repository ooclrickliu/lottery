package cn.wisdom.lottery.service;

import java.text.MessageFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.wisdom.lottery.common.exception.OVTRuntimeException;
import cn.wisdom.lottery.common.log.Logger;
import cn.wisdom.lottery.common.log.LoggerFactory;
import cn.wisdom.lottery.common.utils.DataConvertUtils;
import cn.wisdom.lottery.common.utils.DateTimeUtils;
import cn.wisdom.lottery.common.utils.MathUtils;
import cn.wisdom.lottery.common.utils.NumberGeneratorUtil;
import cn.wisdom.lottery.dao.LotteryDao;
import cn.wisdom.lottery.dao.constant.BusinessType;
import cn.wisdom.lottery.dao.constant.LotteryType;
import cn.wisdom.lottery.dao.constant.PayState;
import cn.wisdom.lottery.dao.constant.PrizeState;
import cn.wisdom.lottery.dao.threadpool.OVTask;
import cn.wisdom.lottery.dao.threadpool.OVThreadPoolExecutor;
import cn.wisdom.lottery.dao.vo.AppProperty;
import cn.wisdom.lottery.dao.vo.Lottery;
import cn.wisdom.lottery.dao.vo.LotteryPeriod;
import cn.wisdom.lottery.dao.vo.LotteryRedpack;
import cn.wisdom.lottery.dao.vo.User;
import cn.wisdom.lottery.service.context.SessionContext;
import cn.wisdom.lottery.service.exception.ServiceErrorCode;
import cn.wisdom.lottery.service.exception.ServiceException;
import cn.wisdom.lottery.service.wx.MessageNotifier;

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
    private OVThreadPoolExecutor executor;

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
            lottery.setTotalFee(0.01f);
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
        return lotteryDao.getLottery(lotteryId);
    }

    @Override
    public void distributeTicket(long lotteryId, long merchantId)
            throws ServiceException
    {
        // change lottery ticket_state to 'Distributed' and set merchant
        Lottery lottery = lotteryDao.getLottery(lotteryId);

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
            messageNotifier.notifyCustomerPaidSuccess(lottery, openid);
		}
    }

	private void distribute(final Lottery lottery) {
		// distribute to merchant.
		executor.submitTask(new OVTask() {
			
			@Override
			public void run() {
		        try {
					lotteryDistributeService.distribute(lottery);
				} catch (ServiceException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public String getDescption() {
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
    public void updatePrizeInfo(List<LotteryPeriod> prizeLotteries)
    {
        lotteryDao.updatePrizeInfo(prizeLotteries);
    }

    @Override
    public Lottery getMyLatestLottery(long userId)
    {
         return lotteryDao.getLatestLottery(userId);
    }

    @Override
    public List<Lottery> getLotteries(long owner) {
    	
    	return lotteryDao.getLotteries(owner);
    }

	@Override
	public Lottery snatchRedpack(long lotteryId) throws ServiceException {
		User user = SessionContext.getCurrentUser();
		
		// 1. how to get outer user info
		// A: customerOAuthAccessInterceptor
		
		// 2. assert lottery is redpack type
		Lottery lottery = this.getLottery(lotteryId);
		if (lottery.getBusinessType() != BusinessType.RedPack) {
			throw new ServiceException(ServiceErrorCode.ERROR_BUSINESS_TYPE, "Error lottery business type.");
		}
		
		// 3. check redpack remain count
		if (lottery.getRedpacks().size() >= lottery.getRedpackCount())
		{
			throw new ServiceException(ServiceErrorCode.REDPACK_EMPTY, "Redpack is empty.");
		}
		
		// 4. get all redpack user info
		
		// 5. record snatched redpack info: rate, user
		// TODO use reids and calculate it safely and quickly
		int rate = generateRedpackRate(lottery.getRedpackCount(), lottery.getRedpacks());
		LotteryRedpack redpack = new LotteryRedpack();
		redpack.setLotteryId(lotteryId);
		redpack.setUserId(user.getId());
		redpack.setRate(rate);
		redpack.setAcquireTime(DateTimeUtils.getCurrentTimestamp());
		redpack.setUser(user);
		lottery.getRedpacks().add(redpack);
		
		lotteryDao.saveRedpack(redpack);
		
		return lottery;
	}

	private int generateRedpackRate(int redpackCount,
			List<LotteryRedpack> redpacks) {
		int rate = 0;
		
		int remainCount = redpackCount - redpacks.size();
		int remainRate = 100;
		
		for (LotteryRedpack lotteryRedpack : redpacks) {
			remainRate -= lotteryRedpack.getRate();
		}
		
		if (remainCount > 1) {
			rate = MathUtils.rand(remainRate);
		}
		else if (remainCount == 1) {
			rate = remainRate;
		}
		
		return rate;
	}
}
