package cn.wisdom.lottery.payment.dao;

import java.text.MessageFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import cn.wisdom.lottery.payment.dao.constant.LotteryType;
import cn.wisdom.lottery.payment.dao.mapper.PrizeLotteryMapper.SSQPeriodInfoMapper;
import cn.wisdom.lottery.payment.dao.mapper.PrizeLotteryMapper.SSQPrizeInfoMapper;
import cn.wisdom.lottery.payment.dao.vo.PrizeLottery;
import cn.wisdom.lottery.payment.dao.vo.PrizeLotterySSQ;
import cn.wisdom.lottery.payment.service.remote.response.LotteryOpenData;

@Repository
public class PrizeLotteryDaoImpl implements PrizeLotteryDao {

	private static final String GET_SSQ_PRIZE = "select * from prize_lottery_ssq where period = ?";
	
	private static final String GET_SSQ_CURRENT_PERIOD = "select period, open_time from prize_lottery_ssq where open_time > current_timestamp limit 1";
	
	private static final String GET_SSQ_NEXT_N_PERIOD = "select period from prize_lottery_ssq where open_time > current_timestamp limit ?";
	
	private static final String GET_SSQ_LAST_PERIOD = "select * from prize_lottery_ssq where open_time < current_timestamp order by id desc limit 1";
	
	private static final String UPDATE_SSQ_OPEN_INFO = "update prize_lottery_ssq set number = ?, update_time = current_timestamp "
			+ " where period = ?";
	
	@Autowired
	private DaoHelper daoHelper;
	
	@Autowired
	private SSQPrizeInfoMapper ssqPrizeInfoMapper;
	
	@Autowired
	private SSQPeriodInfoMapper ssqPeriodInfoMapper;
	
	@Override
	public PrizeLottery getPrizeLottery(LotteryType lotteryType, int period) {
		
		String errMsg = MessageFormat.format("Failed to query {0}-[{1}] prize lottery info.", lotteryType, period);
		PrizeLottery prizeLottery = daoHelper.queryForObject(GET_SSQ_PRIZE, getRowMapper(lotteryType), errMsg, period);
		
		return prizeLottery;
	}

	@Override
	public void saveOpenNumbers(PrizeLottery lottery, LotteryType lotteryType) {
		
		String sql = "";
		Object[] args = null;
		if (lotteryType == LotteryType.SSQ) {
			PrizeLotterySSQ ssq = (PrizeLotterySSQ) lottery;
			sql = UPDATE_SSQ_OPEN_INFO;
			
			args = new Object[8];
			args[0] = ssq.getRed().get(0);
			args[1] = ssq.getRed().get(1);
			args[2] = ssq.getRed().get(2);
			args[3] = ssq.getRed().get(3);
			args[4] = ssq.getRed().get(4);
			args[5] = ssq.getRed().get(5);
			args[6] = ssq.getBlue().get(0);
			args[7] = ssq.getPeriod();
		}
		String errMsg = MessageFormat.format("Failed to genearte period for year [{0}]", lottery);
		daoHelper.update(sql, errMsg, args);
	}

	@Override
	public LotteryOpenData getCurrentPeriod(LotteryType lotteryType) {
		String errMsg = MessageFormat.format("Failed to query {0} current period info.", lotteryType);
		PrizeLottery prizeLottery = daoHelper.queryForObject(GET_SSQ_CURRENT_PERIOD, ssqPeriodInfoMapper, errMsg);
		
		return new LotteryOpenData(prizeLottery);
	}

	@Override
	public List<Integer> getNextNPeriods(LotteryType lotteryType, int n) {
		String errMsg = MessageFormat.format("Failed to query {0} next {1} period.", lotteryType, n);
		List<Integer> periods = daoHelper.queryForList(GET_SSQ_NEXT_N_PERIOD, Integer.class, errMsg, n);
		
		return periods;
	}

	@Override
	public LotteryOpenData getLastestOpenInfo(LotteryType lotteryType) {
		String errMsg = MessageFormat.format("Failed to query {0} current period info.", lotteryType);
		PrizeLottery prizeLottery = daoHelper.queryForObject(GET_SSQ_LAST_PERIOD, getRowMapper(lotteryType), errMsg);
		
		return new LotteryOpenData(prizeLottery);
	}
	
	private RowMapper<? extends PrizeLottery> getRowMapper(LotteryType lotteryType) {
		if (lotteryType == LotteryType.SSQ) {
			return ssqPrizeInfoMapper;
		}
		else {
			return ssqPrizeInfoMapper;
		}
	}
}
