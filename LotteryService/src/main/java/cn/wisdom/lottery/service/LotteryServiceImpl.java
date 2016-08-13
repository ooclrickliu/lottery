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

        // 1. set owner for private lottery
        lottery.setCreateBy(SessionContext.getCurrentUser().getId());
        if (lottery.getBusinessType().equals(BusinessType.Private))
        {
            lottery.setOwner(lottery.getCreateBy());
        }

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
}
