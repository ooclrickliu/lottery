package cn.wisdom.lottery.payment.service;

import java.text.MessageFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.wisdom.lottery.payment.common.log.Logger;
import cn.wisdom.lottery.payment.common.log.LoggerFactory;
import cn.wisdom.lottery.payment.common.utils.DateTimeUtils;
import cn.wisdom.lottery.payment.dao.LotteryDao;
import cn.wisdom.lottery.payment.dao.constant.LotteryType;
import cn.wisdom.lottery.payment.dao.constant.TicketState;
import cn.wisdom.lottery.payment.dao.vo.AppProperty;
import cn.wisdom.lottery.payment.dao.vo.Lottery;
import cn.wisdom.lottery.payment.dao.vo.LotteryNumber;
import cn.wisdom.lottery.payment.dao.vo.User;
import cn.wisdom.lottery.payment.service.context.SessionContext;
import cn.wisdom.lottery.payment.service.exception.ServiceErrorCode;
import cn.wisdom.lottery.payment.service.exception.ServiceException;
import cn.wisdom.lottery.payment.service.remote.OVPaymentProxy;

import com.ovt.order.util.entity.Order;
import com.ovt.order.util.exception.PaymentSDKException;

@Service
public class LotteryServiceImpl implements LotteryService {

    @Autowired
    private AppProperty appProperties;

    @Autowired
    private OVPaymentProxy ovPaymentProxy;
    
    @Autowired
    private LotteryPrizeService lotteryPublishService;
    
    @Autowired
    private LotteryPriceService lotteryPriceService;
    
    @Autowired
    private LotteryDao lotteryDao;

    private Logger logger = LoggerFactory.getLogger(LotteryService.class.getName());
	
	@Override
	public Lottery createPrivateOrder(Lottery lottery) throws ServiceException {
		logger.debug("Receive lottery order: {}!", lottery);
        
        // 1. set owner for private lottery
		lottery.setCreateBy(SessionContext.getCurrentUser().getId());
		lottery.setOwner(SessionContext.getCurrentUser().getId());

        // 2. create order
        Order order = createOrder(lottery);
        
        // 3. save order
        try
        {
            if (appProperties.debugPay)
            {
                order.setOrderTotalFee(0.01f);
            }
            order = ovPaymentProxy.getOVPayment().createOrder(order);
            
            lottery.setOrderNo(order.getOrderNo());

            // 4. save lottery
            saveLottery(lottery);
        }
        catch (PaymentSDKException e)
        {
            String errMsg = MessageFormat.format("Failed to create lottery order {0}", lottery);
            throw new ServiceException(e.getErrorCode(), errMsg);
        }

        return lottery;
	}


	@Override
	public Lottery getLottery(String orderNo)
			throws ServiceException {
		return lotteryDao.getLottery(orderNo);
	}
	
	@Override
	public void distributeTicket(String orderNo, long merchantId) throws ServiceException {
		// change lottery ticket_state to 'Distributed' and set merchant
		Lottery lotteryProfile = lotteryDao.getLottery(orderNo);
		
		if (lotteryProfile.getTicketState() != TicketState.CanPrint) {
			String errMsg = MessageFormat.format("Ticket in state [{0}], can't be distributed!", 
					lotteryProfile.getTicketState());
			throw new ServiceException(ServiceErrorCode.INVALID_STATE, errMsg);
		}
		
		lotteryProfile.setTicketState(TicketState.Distributed);
		lotteryProfile.setMerchant(merchantId);
    	lotteryProfile.setDistributeTime(DateTimeUtils.getCurrentTimestamp());
		
		lotteryDao.updateDistributeState(lotteryProfile);
	}
	
	@Override
	public List<Lottery> queryLottery(LotteryType lotteryType, int period, long merchantId) {
		
		return lotteryDao.getLottery(lotteryType, period, merchantId);
	}

	@Override
    public void printTickets(List<String> orderNos, long merchantId) throws ServiceException {
    	// change lottery ticket_state to 'print' and set print time
		List<Lottery> lotteries = lotteryDao.getLottery(orderNos, false, false);
		for (Lottery lottery : lotteries) {
			if (lottery.getTicketState() != TicketState.Distributed) {
				String errMsg = MessageFormat.format("Ticket in state [{0}], can't be print!", 
						lottery.getTicketState());
				throw new ServiceException(ServiceErrorCode.INVALID_STATE, errMsg);
			}
			if (lottery.getMerchant() != merchantId) {
				throw new ServiceException(ServiceErrorCode.NO_PRIVILEGE, "No print privillidge!");
			}
			
			lottery.setTicketState(TicketState.Printed);
			lottery.setTicketPrintTime(DateTimeUtils.getCurrentTimestamp());
		}
		lotteryDao.updatePrintState(lotteries);
    }
    
    @Override
    public void fetchTicket(String orderNo, long userId) throws ServiceException {
    	
    	Lottery lottery = lotteryDao.getLottery(orderNo);
    	if (lottery.getTicketState() != TicketState.Printed) {
    		String errMsg = MessageFormat.format("Ticket in state [{0}], can't be fetched!", 
					lottery.getTicketState());
			throw new ServiceException(ServiceErrorCode.INVALID_STATE, errMsg);
		}
    	
    	if (lottery.getOwner() != userId) {
    		throw new ServiceException(ServiceErrorCode.NO_PRIVILEGE, "No fetch privillidge!");
		}
    	
    	lottery.setTicketFetchTime(DateTimeUtils.getCurrentTimestamp());
		lotteryDao.updateFetchState(lottery);
    }
	
	@Override
	public void onPaidSuccess(String userId, String orderNo)
			throws ServiceException {
		Lottery lottery = lotteryDao.getLottery(orderNo, false, false);
		
		lottery.setTicketState(TicketState.CanPrint);
		
		lotteryDao.updateTicketState(lottery);
		
		// default distribute to sb.
		this.distributeTicket(orderNo, 2);
		
		// notify merchants
	}

	@Override
	public List<Lottery> getPrintedLotteries(LotteryType lotteryType, int period)
			throws ServiceException {
		
		return lotteryDao.getPrintedLotteries(lotteryType, period);
	}

	@Override
	public void updatePrizeInfo(List<Lottery> prizeLotteries) {
		lotteryDao.updatePrizeInfo(prizeLotteries);
	}
	
	private void saveLottery(Lottery lottery) throws ServiceException {
		
		lotteryDao.saveLottery(lottery);
	}
	
	private Order createOrder(Lottery lottery) throws ServiceException {
		Order order = new Order();

		User user = SessionContext.getCurrentUser();
		order.setUserId("" + user.getId());
		order.setCreateBy("" + user.getId());

		String remark = "";
		for (LotteryNumber lotteryNumber : lottery.getNumbers()) {
			remark += lotteryNumber.getNumber() + ";";
		}
		order.setOrderRemark(remark);

		lotteryPriceService.calculateLotteryTotalFee(lottery);
		
		return order;
	}


	@Override
	public List<Lottery> getMyLottery(String openid,
			LotteryType lotteryType) {
		return lotteryDao.getLottery(orderNo, queryNumber, queryPeriod);
	}


}
