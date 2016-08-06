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
import cn.wisdom.lottery.dao.constant.TicketState;
import cn.wisdom.lottery.dao.threadpool.OVTask;
import cn.wisdom.lottery.dao.threadpool.OVThreadPoolExecutor;
import cn.wisdom.lottery.dao.vo.AppProperty;
import cn.wisdom.lottery.dao.vo.Lottery;
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
            lottery.setOwner(SessionContext.getCurrentUser().getId());
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
        Lottery lotteryProfile = lotteryDao.getLottery(lotteryId);

        if (lotteryProfile.getTicketState() != TicketState.Paid)
        {
            String errMsg = MessageFormat.format(
                    "Ticket in state [{0}], can't be distributed!",
                    lotteryProfile.getTicketState());
            throw new ServiceException(ServiceErrorCode.INVALID_STATE, errMsg);
        }

        lotteryProfile.setTicketState(TicketState.Distributed);
        lotteryProfile.setMerchant(merchantId);
        lotteryProfile.setDistributeTime(DateTimeUtils.getCurrentTimestamp());

        lotteryDao.updateDistributeState(lotteryProfile);
    }

    @Override
    public List<Lottery> queryLottery(LotteryType lotteryType, int period,
            long merchantId)
    {

        return lotteryDao.getLottery(lotteryType, period, merchantId);
    }

    @Override
    public void printTickets(List<Long> lotteryIds, long merchantId)
            throws ServiceException
    {
        // change lottery ticket_state to 'print' and set print time
        List<Lottery> lotteries = lotteryDao.getLottery(lotteryIds, false, false);
        for (Lottery lottery : lotteries)
        {
            if (lottery.getTicketState() != TicketState.Distributed)
            {
                String errMsg = MessageFormat.format(
                        "Ticket in state [{0}], can't be print!",
                        lottery.getTicketState());
                throw new ServiceException(ServiceErrorCode.INVALID_STATE,
                        errMsg);
            }
            if (lottery.getMerchant() != merchantId)
            {
                throw new ServiceException(ServiceErrorCode.NO_PRIVILEGE,
                        "No print privillidge!");
            }

            lottery.setTicketState(TicketState.Printed);
            lottery.setTicketPrintTime(DateTimeUtils.getCurrentTimestamp());
        }
        lotteryDao.updatePrintState(lotteries);
    }

    @Override
    public void fetchTicket(long lotteryId, long userId)
            throws ServiceException
    {

        Lottery lottery = lotteryDao.getLottery(lotteryId);
        if (lottery.getTicketState() != TicketState.Printed)
        {
            String errMsg = MessageFormat.format(
                    "Ticket in state [{0}], can't be fetched!",
                    lottery.getTicketState());
            throw new ServiceException(ServiceErrorCode.INVALID_STATE, errMsg);
        }

        if (lottery.getOwner() != userId)
        {
            throw new ServiceException(ServiceErrorCode.NO_PRIVILEGE,
                    "No fetch privillidge!");
        }

        lottery.setTicketFetchTime(DateTimeUtils.getCurrentTimestamp());
        lotteryDao.updateFetchState(lottery);
    }

    @Override
    public void onPaidSuccess(String orderNo, String openid)
            throws ServiceException
    {
    	logger.info("Receive order paid notification from wx: order[{}]", orderNo);
        final Lottery lottery = lotteryDao.getLotteryByOrder(orderNo);

        if (lottery != null && lottery.getTicketState() == TicketState.UnPaid) {
            lottery.setTicketState(TicketState.Paid);

            lotteryDao.updateTicketState(lottery);

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
    public List<Lottery> getPrintedLotteries(LotteryType lotteryType, int period)
            throws ServiceException
    {

        return lotteryDao.getPrintedLotteries(lotteryType, period);
    }

    @Override
    public void updatePrizeInfo(List<Lottery> prizeLotteries)
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
