package cn.wisdom.lottery.payment.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import cn.wisdom.lottery.payment.common.utils.MathUtils;
import cn.wisdom.lottery.payment.dao.constant.LotteryType;
import cn.wisdom.lottery.payment.dao.vo.Lottery;
import cn.wisdom.lottery.payment.dao.vo.LotteryNumber;
import cn.wisdom.lottery.payment.dao.vo.PrizeLotterySSQ;
import cn.wisdom.lottery.payment.service.exception.ServiceException;

@Service
public class LotteryPriceServiceImpl implements LotteryPriceService {

	interface LotteryPriceCalculator {

		int calculatePrice(Lottery lottery);
	}
	
	private class SSQPriceCalculator implements LotteryPriceCalculator {

		@Override
		public int calculatePrice(Lottery lottery) {
			
			int price = 0;
			for (LotteryNumber lotteryNumber : lottery.getNumbers()) {
				PrizeLotterySSQ ssq = new PrizeLotterySSQ(0, lotteryNumber.getNumber());
				
				int r = ssq.getRed().size();
				int b = ssq.getBlue().size();
				
				//red: Cn,6
				//blue: Cn,1 = n
				
				price += MathUtils.Cn_m(r, 6) * b;
			}
			return price * 2;
		}
	}

	private Map<LotteryType, LotteryPriceCalculator> 
		calculators = new HashMap<LotteryType, LotteryPriceServiceImpl.LotteryPriceCalculator>();
	
	@PostConstruct
	public void init()
	{
		this.calculators.put(LotteryType.SSQ, new SSQPriceCalculator());
	}

	@Override
	public int calculateLotteryTotalFee(Lottery lottery)
			throws ServiceException {
		LotteryPriceCalculator calculator = this.calculators.get(lottery.getLotteryType());
		
		int price = calculator.calculatePrice(lottery);
		
		return price;
	}
}
