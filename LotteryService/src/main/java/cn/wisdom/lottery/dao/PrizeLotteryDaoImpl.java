package cn.wisdom.lottery.dao;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.wisdom.lottery.dao.constant.LotteryType;
import cn.wisdom.lottery.dao.mapper.DaoRowMapper;
import cn.wisdom.lottery.dao.vo.PrizeLottery;
import cn.wisdom.lottery.dao.vo.PrizeLotterySSQ;
import cn.wisdom.lottery.service.remote.response.LotteryOpenData;

@Repository
public class PrizeLotteryDaoImpl implements PrizeLotteryDao {

	private static final String GET_SSQ_PRIZE = "select * from prize_no_ssq where period = ?";
	
	private static final String GET_SSQ_CURRENT_PERIOD = "select period, open_time from prize_no_ssq where open_time > current_timestamp limit 1";
	
	private static final String GET_SSQ_NEXT_N_PERIOD = "select period, open_time from prize_no_ssq where open_time > current_timestamp limit ?";
	
	private static final String GET_SSQ_LAST_PERIOD = "select id, period, open_time, number from prize_no_ssq where open_time < current_timestamp order by id desc limit 1";
	
	private static final String GET_SSQ_PERIOD = "select id, period, open_time, number from prize_no_ssq where period = ? limit 1";
	
	private static final String UPDATE_SSQ_OPEN_INFO = "update prize_no_ssq set number = ?, update_time = current_timestamp "
			+ " where period = ?";
	
	@Autowired
	private DaoHelper daoHelper;
	
    private static final DaoRowMapper<PrizeLotterySSQ> ssqPrizeInfoMapper = new DaoRowMapper<PrizeLotterySSQ>(PrizeLotterySSQ.class);
	
	@Override
	public PrizeLottery getPrizeLottery(LotteryType lotteryType, int period) {
		
		String errMsg = MessageFormat.format("Failed to query {0}-[{1}] prize lottery info.", lotteryType, period);
		PrizeLottery prizeLottery = daoHelper.queryForObject(GET_SSQ_PRIZE, ssqPrizeInfoMapper, errMsg, period);
		
		return prizeLottery;
	}

	@Override
	public void saveOpenNumbers(PrizeLottery lottery, LotteryType lotteryType) {
		
		String sql = "";
		Object[] args = null;
		if (lotteryType == LotteryType.SSQ) {
			PrizeLotterySSQ ssq = (PrizeLotterySSQ) lottery;
			sql = UPDATE_SSQ_OPEN_INFO;
			
			args = new Object[2];
			args[0] = ssq.getNumber();
			args[1] = ssq.getPeriod();
		}
		String errMsg = MessageFormat.format("Failed to save open numbers of {0} - [{1}]", lotteryType, lottery);
		daoHelper.update(sql, errMsg, args);
	}

	@Override
	public LotteryOpenData getCurrentPeriod(LotteryType lotteryType) {
		String errMsg = MessageFormat.format("Failed to query {0} current period info.", lotteryType);
		PrizeLottery prizeLottery = daoHelper.queryForObject(GET_SSQ_CURRENT_PERIOD, ssqPrizeInfoMapper, errMsg);
		
		if (prizeLottery != null && prizeLottery.getOpenTime() != null) {
			unifyOpenTime(prizeLottery.getOpenTime());
		}
		return new LotteryOpenData(prizeLottery);
	}

	@Override
	public List<PrizeLotterySSQ> getNextNPeriods(LotteryType lotteryType, int n) {
		String errMsg = MessageFormat.format("Failed to query {0} next {1} period.", lotteryType, n);
		List<PrizeLotterySSQ> periods = daoHelper.queryForList(GET_SSQ_NEXT_N_PERIOD, ssqPrizeInfoMapper, errMsg, n);
		
		return periods;
	}

	@Override
	public LotteryOpenData getLastestOpenInfo(LotteryType lotteryType) {
		String errMsg = MessageFormat.format("Failed to query {0} latest open info.", lotteryType);
		PrizeLottery prizeLottery = daoHelper.queryForObject(GET_SSQ_LAST_PERIOD, ssqPrizeInfoMapper, errMsg);

		if (prizeLottery != null && prizeLottery.getOpenTime() != null) {
			unifyOpenTime(prizeLottery.getOpenTime());
		}
		return new LotteryOpenData(prizeLottery);
	}

	@SuppressWarnings("deprecation")
	private void unifyOpenTime(Timestamp openTime) {
		openTime.setHours(21);
		openTime.setMinutes(15);
	}

	@Override
	public LotteryOpenData getOpenInfo(LotteryType lotteryType, int period) {
		String errMsg = MessageFormat.format("Failed to query {0} open info of period {1}.", lotteryType, period);
		PrizeLottery prizeLottery = daoHelper.queryForObject(GET_SSQ_PERIOD, ssqPrizeInfoMapper, errMsg, period);
		
		LotteryOpenData result = null;
		if (prizeLottery != null && prizeLottery.getOpenTime() != null) {
			unifyOpenTime(prizeLottery.getOpenTime());
			
			result = new LotteryOpenData(prizeLottery);
		}
		
		return result;
	}
}
