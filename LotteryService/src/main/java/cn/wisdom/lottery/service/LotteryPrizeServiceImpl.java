package cn.wisdom.lottery.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.wisdom.lottery.common.utils.CollectionUtils;
import cn.wisdom.lottery.dao.PrizeLotteryDao;
import cn.wisdom.lottery.dao.constant.LotteryType;
import cn.wisdom.lottery.dao.vo.Lottery;
import cn.wisdom.lottery.dao.vo.LotteryNumber;
import cn.wisdom.lottery.dao.vo.PrizeLottery;
import cn.wisdom.lottery.dao.vo.PrizeLotterySSQ;
import cn.wisdom.lottery.service.exception.ServiceException;
import cn.wisdom.lottery.service.remote.response.LotteryOpenData;

/**
 * 中奖信息查询及维护
 * 
 * @author zhi.liu
 *
 */
@Service
public class LotteryPrizeServiceImpl implements LotteryPrizeService {

	/**
	 * Represents prize 1 or 2 that bonus amount is not fixed.
	 */
	private static final int BONUS_NOT_SURE = 999999;
	
	@Autowired
	private PrizeLotteryDao prizeLotteryDao;
	
	private Map<LotteryType, LotteryPrizeCalculator> 
		calculators = new HashMap<LotteryType, LotteryPrizeCalculator>();
	
	@PostConstruct
	public void init()
	{
		this.calculators.put(LotteryType.SSQ, new SSQPrizeCalculator());
	}

	@Override
	public LotteryOpenData getCurrentPeriod(LotteryType lotteryType)
			throws ServiceException {
		
		// current < open_time order by id limit 1; 
		
		return prizeLotteryDao.getCurrentPeriod(lotteryType);
	}
	
	@Override
	public List<Integer> getNextNPeriods(LotteryType lotteryType, int n) {
		
		return prizeLotteryDao.getNextNPeriods(lotteryType, n);
	}
	
	@Override
	public LotteryOpenData getLatestOpenInfo(LotteryType lotteryType) {

		return prizeLotteryDao.getLastestOpenInfo(lotteryType);
	}
	
	@Override
	public LotteryOpenData getOpenInfo(LotteryType lotteryType, int period) {
		
		return prizeLotteryDao.getOpenInfo(lotteryType, period);
	}

	@Override
	public String getPrizeNo(LotteryType lotteryType, int period)
			throws ServiceException {
		
		PrizeLottery prizeLottery = prizeLotteryDao.getPrizeLottery(lotteryType, period);
		return prizeLottery.getNumber();
	}

	@Override
	public void savePrizeLottery(PrizeLottery prizeLottery, LotteryType lotteryType) {
		prizeLotteryDao.saveOpenNumbers(prizeLottery, lotteryType);
	}

	@Override
	public Map<String, Map<String, Integer>> getPrizeInfo(Lottery lottery, PrizeLotterySSQ openLottery) {
		Map<String, Map<String, Integer>> prizeInfo = new HashMap<String, Map<String,Integer>>();

		if (lottery.getLotteryType() == LotteryType.SSQ) {
			Map<String, Integer> singlePrizeInfo;
			for (LotteryNumber number : lottery.getNumbers()) {
				singlePrizeInfo = new HashMap<String, Integer>();
				
				int rHits = 0;
				int bHits = 0;
				
				PrizeLotterySSQ rawLottery = new PrizeLotterySSQ(openLottery.getPeriod(), 
						number.getNumber());
				for (int r : rawLottery.getRed()) {
					if (openLottery.getRed().contains(r)) {
						rHits++;
					}
				}
				for (int b : rawLottery.getBlue()) {
					if (openLottery.getBlue().contains(b)) {
						bHits++;
					}
				}
				
				//prize hit map: key-prize, value-hits
				singlePrizeInfo = calculators.get(LotteryType.SSQ).getPrizeInfo(rawLottery.getRed().size(), 
						rawLottery.getBlue().size(), rHits, bHits);
				
				if (CollectionUtils.isNotEmpty(singlePrizeInfo)) {
					prizeInfo.put("" + number.getId(), singlePrizeInfo);
				}
			}
		}
		
		return prizeInfo;
	}

	@Override
	public int getPrizeBonus(Map<String, Map<String, Integer>> prizeInfo) {
		for (Map<String, Integer> singlePrizeInfo : prizeInfo.values()) {
			if (singlePrizeInfo.keySet().contains(1) ||
					singlePrizeInfo.keySet().contains(2)) {
				return BONUS_NOT_SURE;
			}
		}
		
		int bonus = 0;
		int pirzeBonus, prizeAmount;
		for (Map<String, Integer> singlePrizeInfo : prizeInfo.values()) {
			for (String prize : singlePrizeInfo.keySet()) {
				pirzeBonus = prizeBonusMap.get(prize);
				prizeAmount = singlePrizeInfo.get(prize);
				bonus += prizeAmount * pirzeBonus;
			}
		}
		
		return bonus;
	}
	
	private static final Map<String, Integer> prizeBonusMap = new HashMap<String, Integer>();
	static 
	{
		prizeBonusMap.put("3", 3000);
		prizeBonusMap.put("4", 200);
		prizeBonusMap.put("5", 10);
		prizeBonusMap.put("6", 5);
	}
}
