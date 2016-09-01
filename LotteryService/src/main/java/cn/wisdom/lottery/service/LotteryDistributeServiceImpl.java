package cn.wisdom.lottery.service;

import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.wisdom.lottery.common.log.Logger;
import cn.wisdom.lottery.common.log.LoggerFactory;
import cn.wisdom.lottery.common.utils.DateTimeUtils;
import cn.wisdom.lottery.dao.LotteryDao;
import cn.wisdom.lottery.dao.constant.PayState;
import cn.wisdom.lottery.dao.vo.AppProperty;
import cn.wisdom.lottery.dao.vo.Lottery;
import cn.wisdom.lottery.dao.vo.User;
import cn.wisdom.lottery.service.exception.ServiceErrorCode;
import cn.wisdom.lottery.service.exception.ServiceException;
import cn.wisdom.lottery.service.wx.MessageNotifier;

@Service
public class LotteryDistributeServiceImpl implements LotteryDistributeService {

    @Autowired
    private LotteryDao lotteryDao;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private MessageNotifier messageNotifier;
    
    @Autowired
    private AppProperty appProperties;
    
    private Logger logger = LoggerFactory.getLogger(LotteryDistributeServiceImpl.class.getName());
    
	@Override
	public void distribute(Lottery lottery) throws ServiceException {
		User merchant = this.getTargetMerchant(); 

		this.distribute(lottery, merchant);
	}

	@Override
	public void distribute(long lotteryId, long merchantId)
			throws ServiceException {
		Lottery lottery = lotteryDao.getLottery(lotteryId, true, true, false);
		User merchant = userService.getUserById(merchantId); 

		this.distribute(lottery, merchant);
	}
	
	private void distribute(Lottery lottery, User merchant) throws ServiceException
	{
		if (lottery.getPayState() != PayState.Paid)
        {
            String errMsg = MessageFormat.format(
                    "Ticket in state [{0}], can't be distributed!",
                    lottery.getPayState());
            throw new ServiceException(ServiceErrorCode.INVALID_STATE, errMsg);
        }
		logger.info("Distribute lottery[{}] to merchant[{}]", lottery.getId(), merchant.getId());
		
        lottery.setMerchant(merchant.getId());
        lottery.setDistributeTime(DateTimeUtils.getCurrentTimestamp());

        lotteryDao.updateDistributeState(lottery);
        
        // notify merchant
        messageNotifier.notifyMerchantDistributed(lottery, merchant.getOpenid());
	}

	private User getTargetMerchant()
	{
		return userService.getUserById(appProperties.defaultMerchant);
	}
}
