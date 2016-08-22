package cn.wisdom.lottery.dao;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.wisdom.lottery.common.utils.DateTimeUtils;
import cn.wisdom.lottery.common.utils.StringUtils;
import cn.wisdom.lottery.dao.constant.BusinessType;
import cn.wisdom.lottery.dao.constant.LotteryType;
import cn.wisdom.lottery.dao.constant.PrizeState;
import cn.wisdom.lottery.dao.mapper.DaoRowMapper;
import cn.wisdom.lottery.dao.vo.Lottery;
import cn.wisdom.lottery.dao.vo.LotteryNumber;
import cn.wisdom.lottery.dao.vo.LotteryPeriod;
import cn.wisdom.lottery.dao.vo.LotteryRedpack;

@Repository
public class LotteryDaoImpl implements LotteryDao {

	@Autowired
	private DaoHelper daoHelper;

	private static final String SAVE_LOTTERY = "insert into lottery(order_no, total_fee, lottery_type, business_type, times, pay_state, owner, remark, create_by, redpack_count, update_time) "
			+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp)";

	private static final String SAVE_LOTTERY_NUMBER = "insert into lottery_number(lottery_id, number) "
			+ "values (?, ?)";
	
	private static final String SAVE_LOTTERY_PERIOD = "insert into lottery_period(lottery_id, period, prize_state, prize_open_time) "
			+ "values (?, ?, ?, ?)";
	
	private static final String SAVE_LOTTERY_REDPACK = "insert into lottery_redpack(lottery_id, user_id, rate, acquire_time) "
			+ "values (?, ?, ?, ?)";

	private static final String GET_LOTTERY_PREFIX = "select * from lottery";
	
	private static final String GET_LOTTERY_JOIN_PREFIX = "select l.* from lottery l join lottery_period p on l.id = p.lottery_id ";
	
	private static final String GET_PAID_LOTTERY_OF_PERIOD = GET_LOTTERY_JOIN_PREFIX
			+ " where lottery_type = ? and pay_state = 'Paid' and period = ?";

	private static final String GET_LOTTERY_BY_ID = GET_LOTTERY_PREFIX
			+ " where id in( {0})";
	
	private static final String GET_LOTTERY_NUMBER = "select * from lottery_number "
			+ " where lottery_id in({0}) order by id";
	
	private static final String GET_LOTTERY_REDPACK = "select * from lottery_redpack "
			+ " where lottery_id in({0}) order by id";
	
	private static final String GET_LOTTERY_PERIOD = "select * from lottery_period "
			+ " where lottery_id in({0}) order by id";
	
	private static final String GET_LOTTERY_PERIOD2 = "select * from lottery_period "
			+ " where lottery_id in({0}) and period = ? order by id";

	private static final String GET_LOTTERY_BY_MERCHANT = GET_LOTTERY_JOIN_PREFIX
			+ " where lottery_type = ? and period = ? and merchant = ?";
	
	private static final String GET_LOTTERY_BY_USER = GET_LOTTERY_PREFIX
			+ " where owner = ? and pay_state = 'Paid' order by id desc limit ?";
	
	private static final String GET_LOTTERY_BY_ORDER = GET_LOTTERY_PREFIX
			+ " where order_no = ? limit 1";
	
	private static final String GET_PERIOD_BY_ID = "select * from lottery_period "
			+ " where id = ? limit 1";

	private static final String UPDATE_LOTTERY_PAY_IMG = "update lottery set pay_img_url = ?, pay_state = ?, update_time = current_timestamp "
			+ "where id = ?";
	
	private static final String UPDATE_LOTTERY_PAY_STATE = "update lottery set pay_state = ?, update_time = current_timestamp "
			+ "where id = ?";
	
	private static final String UPDATE_LOTTERY_PRINT_STATE = "update lottery_period set "
			+ "ticket_print_time = current_timestamp "
			+ "where id = ?";
	
	private static final String UPDATE_LOTTERY_DISTRIBUTE_STATE = "update lottery set "
			+ "merchant = ?, distribute_time = current_timestamp, update_time = current_timestamp "
			+ "where id = ?";
	
	private static final String UPDATE_LOTTERY_FETCH_STATE = "update lottery_period set "
			+ "ticket_fetch_time = current_timestamp "
			+ "where id = ?";
	
	private static final String UPDATE_LOTTERY_PRIZE_STATE = "update lottery_period set prize_state = ?, prize_open_time = current_timestamp "
			+ "where period = ?";
	
	private static final String UPDATE_LOTTERY_PRIZE_INFO = "update lottery_period set prize_info = ?, "
			+ "prize_bonus = ?, prize_state = 'Win' "
			+ "where lottery_id = ? and period= ?";

    private static final DaoRowMapper<Lottery> lotteryMapper = new DaoRowMapper<Lottery>(Lottery.class);
	
    private static final DaoRowMapper<LotteryNumber> lotteryNumberMapper = new DaoRowMapper<LotteryNumber>(LotteryNumber.class);
	
    private static final DaoRowMapper<LotteryPeriod> lotteryPeriodMapper = new DaoRowMapper<LotteryPeriod>(LotteryPeriod.class);
    
    private static final DaoRowMapper<LotteryRedpack> lotteryRedpackMapper = new DaoRowMapper<LotteryRedpack>(LotteryRedpack.class);

	@Override
	public void saveLottery(Lottery lottery) {
		Object[] args = new Object[10];
		args[0] = lottery.getOrderNo();
		args[1] = lottery.getTotalFee();
		args[2] = lottery.getLotteryType().toString();
		args[3] = lottery.getBusinessType().toString();
		args[4] = lottery.getTimes();
		args[5] = lottery.getPayState().toString();
		args[6] = lottery.getOwner();
		args[7] = lottery.getRemark();
		args[8] = lottery.getCreateBy();
		args[9] = lottery.getRedpackCount();

		String errMsg = MessageFormat
				.format("Failed to save lottery!", lottery);
		long lotteryId = daoHelper.save(SAVE_LOTTERY, errMsg, true, args);
		
		lottery.setId(lotteryId);
		
		Timestamp current = DateTimeUtils.getCurrentTimestamp();
		lottery.setCreateTime(current);
		lottery.setUpdateTime(current);

		// lottery number
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (LotteryNumber lotteryNumber : lottery.getNumbers()) {
			args = new Object[2];
			args[0] = lotteryId;
			args[1] = lotteryNumber.getNumber();

			batchArgs.add(args);
		}

		errMsg = MessageFormat.format("Failed to save lottery number!", lottery);
		daoHelper.batchUpdate(SAVE_LOTTERY_NUMBER, batchArgs, errMsg);
		
		// lottery period
		batchArgs = new ArrayList<Object[]>();
		for (LotteryPeriod period : lottery.getPeriods()) {
			args = new Object[4];
			args[0] = lotteryId;
			args[1] = period.getPeriod();
			args[2] = period.getPrizeState().toString();
			args[3] = period.getPrizeOpenTime();
			
			batchArgs.add(args);
		}
		
		errMsg = MessageFormat.format("Failed to save lottery period!", lottery);
		daoHelper.batchUpdate(SAVE_LOTTERY_PERIOD, batchArgs, errMsg);

	}

	public void saveRedpack(LotteryRedpack redpack) {
		Object[] args = new Object[4];
		args[0] = redpack.getLotteryId();
		args[1] = redpack.getUser();
		args[2] = redpack.getRate();
		args[3] = redpack.getAcquireTime();

		String errMsg = MessageFormat
				.format("Failed to save lottery!", redpack);
		daoHelper.save(SAVE_LOTTERY_REDPACK, errMsg, false, args);
	}

	@Override
	public Lottery getLottery(long lotteryId) {
		
		return this.getLottery(lotteryId, true, true);
	}
	
	@Override
	public Lottery getLottery(long lotteryId, boolean queryNumber, boolean queryPeriod) {

		String sql = MessageFormat.format(GET_LOTTERY_BY_ID, "" + lotteryId);
		
		String errMsg = MessageFormat.format(
				"Failed to query lottery by orderNo [{0}]", lotteryId);
		Lottery lottery = daoHelper.queryForObject(sql,
				lotteryMapper, errMsg);
		
		if (lottery != null) {
			if (queryNumber) {
				sql = MessageFormat.format(GET_LOTTERY_NUMBER, "" + lotteryId);
				List<LotteryNumber> numbers = daoHelper.queryForList(sql, lotteryNumberMapper, errMsg);
				lottery.setNumbers(numbers);
			}
			
			if (queryPeriod) {
				sql = MessageFormat.format(GET_LOTTERY_PERIOD, "" + lotteryId);
				List<LotteryPeriod> periods = daoHelper.queryForList(sql, lotteryPeriodMapper, errMsg);
				lottery.setPeriods(periods);
			}
		}
		
		return lottery;
	}

	@Override
	public List<Lottery> getLottery(List<Long> lotteryIds, boolean queryNumber, boolean queryPeriod) {
		String lotteryIdsCSV = StringUtils.getCSV(lotteryIds);
		
		String sql = MessageFormat.format(GET_LOTTERY_BY_ID, lotteryIdsCSV);

		String errMsg = MessageFormat.format(
				"Failed to query lottery by ids [{0}]", lotteryIdsCSV);
		List<Lottery> lotteries = daoHelper.queryForList(sql,
				lotteryMapper, errMsg);

		if (queryNumber) {
			getLotteryNumbers(lotteries);
		}
		if (queryPeriod) {
			getLotteryPeriods(lotteries);
		}
		
		return lotteries;
	}

	private void getLotteryPeriods(List<Lottery> lotteries) {
		// lottery periods
		Map<Long, Lottery> lotteryMap = new HashMap<Long, Lottery>();
		for (Lottery lottery : lotteries) {
			lotteryMap.put(lottery.getId(), lottery);
		}
		String lotteryIdCSV = StringUtils.getCSV(lotteryMap.keySet());

		if (StringUtils.isNotBlank(lotteryIdCSV)) {
			String errMsg = MessageFormat.format(
					"Faild to get lottery periods, [{0}]", lotteryIdCSV);
			String sql = MessageFormat.format(GET_LOTTERY_PERIOD, lotteryIdCSV);
			List<LotteryPeriod> periods = daoHelper.queryForList(
					sql, lotteryPeriodMapper, errMsg);

			for (LotteryPeriod lotteryPeriod : periods) {
				lotteryMap.get(lotteryPeriod.getLotteryId()).getPeriods().add(lotteryPeriod);
			}
		}
	}
	
	private void getLotteryPeriods(List<Lottery> lotteries, int period) {
		// lottery periods
		Map<Long, Lottery> lotteryMap = new HashMap<Long, Lottery>();
		for (Lottery lottery : lotteries) {
			lotteryMap.put(lottery.getId(), lottery);
		}
		String lotteryIdCSV = StringUtils.getCSV(lotteryMap.keySet());
		
		if (StringUtils.isNotBlank(lotteryIdCSV)) {
			String errMsg = MessageFormat.format(
					"Faild to get lottery periods, [{0}]", lotteryIdCSV);
			String sql = MessageFormat.format(GET_LOTTERY_PERIOD2, lotteryIdCSV);
			List<LotteryPeriod> periods = daoHelper.queryForList(
					sql, lotteryPeriodMapper, errMsg, period);
			
			for (LotteryPeriod lotteryPeriod : periods) {
				lotteryMap.get(lotteryPeriod.getLotteryId()).getPeriods().add(lotteryPeriod);
			}
		}
	}

	private void getLotteryNumbers(List<Lottery> lotteries) {
		// lottery numbers
		Map<Long, Lottery> lotteryMap = new HashMap<Long, Lottery>();
		for (Lottery lottery : lotteries) {
			lotteryMap.put(lottery.getId(), lottery);
		}
		String lotteryIdCSV = StringUtils.getCSV(lotteryMap.keySet());
		
		if (StringUtils.isNotBlank(lotteryIdCSV)) {
			String errMsg = MessageFormat.format("Faild to get lottery numbers, [{0}]", lotteryIdCSV);
			String sql = MessageFormat.format(GET_LOTTERY_NUMBER, lotteryIdCSV);
			List<LotteryNumber> numbers = daoHelper.queryForList(sql, lotteryNumberMapper, errMsg);
			
			for (LotteryNumber lotteryNumber : numbers) {
				lotteryMap.get(lotteryNumber.getLotteryId()).getNumbers().add(lotteryNumber);
			}
		}
	}
	
	private void getLotteryRedpacks(List<Lottery> lotteries) {
		// lottery redpacks
		Map<Long, Lottery> lotteryMap = new HashMap<Long, Lottery>();
		for (Lottery lottery : lotteries) {
			if (lottery.getBusinessType() == BusinessType.RedPack) {
				lotteryMap.put(lottery.getId(), lottery);
			}
		}
		String lotteryIdCSV = StringUtils.getCSV(lotteryMap.keySet());
		
		if (StringUtils.isNotBlank(lotteryIdCSV)) {
			String errMsg = MessageFormat.format("Faild to get lottery numbers, [{0}]", lotteryIdCSV);
			String sql = MessageFormat.format(GET_LOTTERY_REDPACK, lotteryIdCSV);
			List<LotteryRedpack> redpacks = daoHelper.queryForList(sql, lotteryRedpackMapper, errMsg);
			
			for (LotteryRedpack lotteryRedpack : redpacks) {
				lotteryMap.get(lotteryRedpack.getLotteryId()).getRedpacks().add(lotteryRedpack);
			}
		}
	}

	@Override
	public List<Lottery> getLottery(LotteryType lotteryType, int period, long merchantId) {

		String errMsg = MessageFormat.format(
				"Failed to query lottery by merchant [{0}], peroid [{1}]",
				merchantId, period);
		List<Lottery> lotteries = daoHelper.queryForList(
				GET_LOTTERY_BY_MERCHANT, lotteryMapper, errMsg, lotteryType.toString(), period,
				merchantId);

		getLotteryNumbers(lotteries);
		
		getLotteryPeriods(lotteries, period);

		return lotteries;
	}
	
	@Override
	public Lottery getLotteryByOrder(String orderNo) {
		String errMsg = MessageFormat.format(
				"Failed to get lottery by orderNo [{0}]",
				orderNo);
		Lottery lottery = daoHelper.queryForObject(
				GET_LOTTERY_BY_ORDER, lotteryMapper, errMsg, orderNo);
		
		if (lottery != null) {
			List<Lottery> lotteries = new ArrayList<Lottery>();
			lotteries.add(lottery);
			
			getLotteryPeriods(lotteries);
			
			getLotteryNumbers(lotteries);
		}
		
		return lottery;
	}
	
	@Override
	public Lottery getLotteryByPeriod(long periodId) {
		String errMsg = MessageFormat.format(
				"Failed to get lottery by periodId [{0}]",
				periodId);
		LotteryPeriod lotteryPeriod = daoHelper.queryForObject(
				GET_PERIOD_BY_ID, lotteryPeriodMapper, errMsg, periodId);
		
		Lottery lottery = null;
		if (lotteryPeriod != null) {
			lottery = getLottery(lotteryPeriod.getLotteryId(), false, false);
			
			lottery.getPeriods().add(lotteryPeriod);
		}
		
		return lottery;
	}

	@Override
	public Lottery getLatestLottery(long userId) {
		String errMsg = MessageFormat.format(
				"Failed to query the last lottery by openid [{0}]",
				userId);
		Lottery lottery = daoHelper.queryForObject(
				GET_LOTTERY_BY_USER, lotteryMapper, errMsg, userId, 1);

		if (lottery != null) {
			List<Lottery> lotteries = new ArrayList<Lottery>();
			lotteries.add(lottery);
			
			getLotteryPeriods(lotteries);
			
			getLotteryNumbers(lotteries);
		}
		
		return lottery;
	}
	
	@Override
	public List<Lottery> getLotteries(long owner) {
		String errMsg = MessageFormat.format(
				"Failed to query lottery by openid [{0}]",
				owner);
		List<Lottery> lotteries = daoHelper.queryForList(
				GET_LOTTERY_BY_USER, lotteryMapper, errMsg, owner, 100);
		
		getLotteryPeriods(lotteries);
		
		getLotteryNumbers(lotteries);
		
		return lotteries;
	}
	
	@Override
	public void updatePayImg(Lottery lottery) {
		String errMsg = MessageFormat
				.format("Failed to update lottery payState to [{0}] by id [{1}]",
						lottery.getPayState(), lottery.getId());
		daoHelper.update(UPDATE_LOTTERY_PAY_IMG, errMsg, lottery.getPayImgUrl(), lottery
				.getPayState().toString(), lottery.getId());
	}

	@Override
	public void updatePayState(Lottery lottery) {

		String errMsg = MessageFormat
				.format("Failed to update lottery payState to [{0}] by id [{1}]",
						lottery.getPayState(), lottery.getId());
		daoHelper.update(UPDATE_LOTTERY_PAY_STATE, errMsg, lottery
				.getPayState().toString(), lottery.getId());
	}

	@Override
	public void updatePrintState(long periodId) {
		String errMsg = MessageFormat
				.format("Failed to update lottery print time: {0}",
						periodId);

		daoHelper.update(UPDATE_LOTTERY_PRINT_STATE, errMsg, periodId);
	}

	@Override
	public void updateDistributeState(Lottery lottery) {
		String errMsg = MessageFormat
				.format("Failed to update lottery distribute state [{0}]",
						lottery.getId());
		daoHelper.update(UPDATE_LOTTERY_DISTRIBUTE_STATE, errMsg, lottery
				.getMerchant(), lottery.getId());
	}

	@Override
	public void updateFetchState(LotteryPeriod lotteryPeriod) {
		String errMsg = MessageFormat
				.format("Failed to update lottery ticket fetch time [{0}]",
						lotteryPeriod.getId());
		daoHelper.update(UPDATE_LOTTERY_FETCH_STATE, errMsg, lotteryPeriod.getId());
	}
	
	@Override
	public List<Lottery> getPaidLotteries(LotteryType lotteryType, int period) {
		String errMsg = MessageFormat.format(
				"Failed to query paid lottery by lotteryType [{0}], peroid [{1}]",
				lotteryType, period);
		List<Lottery> lotteries = daoHelper.queryForList(
				GET_PAID_LOTTERY_OF_PERIOD, lotteryMapper, errMsg, lotteryType.toString(), period);

		getLotteryNumbers(lotteries);
		
		getLotteryRedpacks(lotteries);

		return lotteries;
	}

	@Override
	public void updatePrizeState(int period, PrizeState prizeState) {
		String errMsg = MessageFormat
				.format("Failed to update lottery prize state: period[{0}], state[{1}]",
						period, prizeState);
		daoHelper.update(UPDATE_LOTTERY_PRIZE_STATE, errMsg, prizeState.toString(), period);
	}

	@Override
	public void updatePrizeInfo(List<LotteryPeriod> prizeLotteries) {
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (LotteryPeriod lotteryPeriod : prizeLotteries) {
			Object[] args = new Object[4];
			args[0] = lotteryPeriod.getPrizeInfo();
			args[1] = lotteryPeriod.getPrizeBonus();
			args[2] = lotteryPeriod.getLotteryId();
			args[3] = lotteryPeriod.getPeriod();

			batchArgs.add(args);
		}
		
		String errMsg = "Failed to update lottery prize info.";

		daoHelper.batchUpdate(UPDATE_LOTTERY_PRIZE_INFO, batchArgs, errMsg);
	}
}
