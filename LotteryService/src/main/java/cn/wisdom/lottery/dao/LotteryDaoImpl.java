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
import cn.wisdom.lottery.dao.constant.LotteryType;
import cn.wisdom.lottery.dao.constant.TicketState;
import cn.wisdom.lottery.dao.mapper.DaoRowMapper;
import cn.wisdom.lottery.dao.vo.Lottery;
import cn.wisdom.lottery.dao.vo.LotteryNumber;
import cn.wisdom.lottery.dao.vo.LotteryPeriod;

@Repository
public class LotteryDaoImpl implements LotteryDao {

	@Autowired
	private DaoHelper daoHelper;

	private static final String SAVE_LOTTERY = "insert into lottery(order_no, total_fee, lottery_type, business_type, times, ticket_state, owner, remark, create_by, update_time) "
			+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp)";

	private static final String SAVE_LOTTERY_NUMBER = "insert into lottery_number(lottery_id, number) "
			+ "values (?, ?)";
	
	private static final String SAVE_LOTTERY_PERIOD = "insert into lottery_period(lottery_id, period) "
			+ "values (?, ?)";

	private static final String GET_LOTTERY_PREFIX = "select * from lottery";
	
	private static final String GET_LOTTERY_JOIN_PREFIX = "select l.* from lottery l join lottery_period p on l.id = p.lottery_id ";
	
	private static final String GET_LOTTERY_ID_PREFIX = "select l.id, l.lottery_type from lottery_period p join lottery l on p.lottery_id = l.id ";
	
	private static final String GET_LOTTERY_BY_TICKET_STATE = GET_LOTTERY_ID_PREFIX
			+ " where period = ? and lottery_type = ? and ticket_state = ?";

	private static final String GET_LOTTERY_BY_ID = GET_LOTTERY_PREFIX
			+ " where id in( {0})";
	
	private static final String GET_LOTTERY_NUMBER = "select id, lottery_id, number from lottery_number "
			+ " where lottery_id in(?) order by id";
	
	private static final String GET_LOTTERY_PERIOD = "select id, lottery_id, period from lottery_period "
			+ " where lottery_id in(?) order by id";

	private static final String GET_LOTTERY_BY_MERCHANT = GET_LOTTERY_JOIN_PREFIX
			+ " where lottery_type = ? and period = ? and merchant = ?";
	
	private static final String GET_LOTTERY_BY_USER = GET_LOTTERY_JOIN_PREFIX
			+ " where owner = ? and ticket_state <> 'UnPaid' group by l.id order by id desc limit ?";
	
	private static final String GET_LOTTERY_BY_ORDER = GET_LOTTERY_PREFIX
			+ " where order_no = ? limit 1";

	private static final String UPDATE_LOTTERY_TICKET_STATE = "update lottery set ticket_state = ?, update_time = current_timestamp "
			+ "where order_no = ?";
	
	private static final String UPDATE_LOTTERY_PRINT_STATE = "update lottery set ticket_state = 'Printed', "
			+ "ticket_print_time = current_timestamp, update_time = current_timestamp "
			+ "where order_no = ?";
	
	private static final String UPDATE_LOTTERY_DISTRIBUTE_STATE = "update lottery set ticket_state = 'Distributed', "
			+ "merchant = ?, distribute_time = current_timestamp, update_time = current_timestamp "
			+ "where order_no = ?";
	
	private static final String UPDATE_LOTTERY_FETCH_STATE = "update lottery set "
			+ "ticket_fetch_time = current_timestamp, update_time = current_timestamp "
			+ "where order_no = ?";
	
	private static final String UPDATE_LOTTERY_PRIZE_INFO = "update lottery set prize_info = ?, "
			+ "prize_bonus = ?, update_time = current_timestamp "
			+ "where id = ?";

    private static final DaoRowMapper<Lottery> lotteryMapper = new DaoRowMapper<Lottery>(Lottery.class);
	
    private static final DaoRowMapper<LotteryNumber> lotteryNumberMapper = new DaoRowMapper<LotteryNumber>(LotteryNumber.class);
	
    private static final DaoRowMapper<LotteryPeriod> lotteryPeriodMapper = new DaoRowMapper<LotteryPeriod>(LotteryPeriod.class);

	@Override
	public void saveLottery(Lottery lottery) {
		Object[] args = new Object[9];
		args[0] = lottery.getOrderNo();
		args[1] = lottery.getTotalFee();
		args[2] = lottery.getLotteryType().toString();
		args[3] = lottery.getBusinessType().toString();
		args[4] = lottery.getTimes();
		args[5] = lottery.getTicketState().toString();
		args[6] = lottery.getOwner();
		args[7] = lottery.getRemark();
		args[8] = lottery.getCreateBy();

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
		for (int period : lottery.getPeriods()) {
			args = new Object[2];
			args[0] = lotteryId;
			args[1] = period;
			
			batchArgs.add(args);
		}
		
		errMsg = MessageFormat.format("Failed to save lottery period!", lottery);
		daoHelper.batchUpdate(SAVE_LOTTERY_PERIOD, batchArgs, errMsg);

	}

	@Override
	public Lottery getLottery(long lotteryId) {
		
		return this.getLottery(lotteryId, true, true);
	}
	
	@Override
	public Lottery getLottery(long lotteryId, boolean queryNumber, boolean queryPeriod) {

		String sql = MessageFormat.format(GET_LOTTERY_BY_ID, lotteryId);
		
		String errMsg = MessageFormat.format(
				"Failed to query lottery by orderNo [{0}]", lotteryId);
		Lottery lottery = daoHelper.queryForObject(sql,
				lotteryMapper, errMsg);
		
		if (lottery != null) {
			if (queryNumber) {
				List<LotteryNumber> numbers = daoHelper.queryForList(GET_LOTTERY_NUMBER, lotteryNumberMapper, errMsg, lottery.getId());
				lottery.setNumbers(numbers);
			}
			
			if (queryPeriod) {
				List<LotteryPeriod> periods = daoHelper.queryForList(GET_LOTTERY_PERIOD, lotteryPeriodMapper, errMsg, lottery.getId());
				
				List<Integer> periodList = new ArrayList<Integer>();
				for (LotteryPeriod lotteryPeriod : periods) {
					periodList.add(lotteryPeriod.getPeriod());
				}
				lottery.setPeriods(periodList);
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
		// lottery numbers
		List<Long> lotteryIds = new ArrayList<Long>();
		Map<Long, Lottery> lotteryMap = new HashMap<Long, Lottery>();
		for (Lottery lottery : lotteries) {
			lotteryIds.add(lottery.getId());
			lotteryMap.put(lottery.getId(), lottery);
		}
		String lotteryIdCSV = StringUtils.getCSV(lotteryIds);

		if (StringUtils.isNotBlank(lotteryIdCSV)) {
			String errMsg = MessageFormat.format(
					"Faild to get lottery periods, [{0}]", lotteryIdCSV);
			List<LotteryPeriod> periods = daoHelper.queryForList(
					GET_LOTTERY_PERIOD, lotteryPeriodMapper, errMsg,
					lotteryIdCSV);

			for (LotteryPeriod lotteryPeriod : periods) {
				lotteryMap.get(lotteryPeriod.getLotteryId()).getPeriods().add(lotteryPeriod.getPeriod());
			}
		}
	}

	private void getLotteryNumbers(List<Lottery> lotteries) {
		// lottery numbers
		List<Long> lotteryIds = new ArrayList<Long>();
		Map<Long, Lottery> lotteryMap = new HashMap<Long, Lottery>();
		for (Lottery lottery : lotteries) {
			lotteryIds.add(lottery.getId());
			lotteryMap.put(lottery.getId(), lottery);
		}
		String lotteryIdCSV = StringUtils.getCSV(lotteryIds);
		
		if (StringUtils.isNotBlank(lotteryIdCSV)) {
			String errMsg = MessageFormat.format("Faild to get lottery numbers, [{0}]", lotteryIdCSV);
			List<LotteryNumber> numbers = daoHelper.queryForList(GET_LOTTERY_NUMBER, lotteryNumberMapper, errMsg, lotteryIdCSV);
			
			for (LotteryNumber lotteryNumber : numbers) {
				lotteryMap.get(lotteryNumber.getLotteryId()).getNumbers().add(lotteryNumber);
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

		return lotteries;
	}
	
	@Override
	public Lottery getLotteryByOrder(String orderNo) {
		String errMsg = MessageFormat.format(
				"Failed to get lottery by orderNo [{0}]",
				orderNo);
		Lottery lottery = daoHelper.queryForObject(
				GET_LOTTERY_BY_ORDER, lotteryMapper, errMsg, orderNo);
		
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
	public void updateTicketState(Lottery lottery) {

		String errMsg = MessageFormat
				.format("Failed to update lottery ticketState to [{0}] by orderNo [{1}]",
						lottery.getTicketState(), lottery.getOrderNo());
		daoHelper.update(UPDATE_LOTTERY_TICKET_STATE, errMsg, lottery
				.getTicketState().toString(), lottery.getOrderNo());
	}

	@Override
	public void updatePrintState(List<Lottery> lotteries) {
		List<String> orderNos = new ArrayList<String>();
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (Lottery lottery : lotteries) {
			Object[] args = new Object[1];
			args[0] = lottery.getOrderNo();

			batchArgs.add(args);
			orderNos.add(lottery.getOrderNo());
		}
		
		String errMsg = MessageFormat
				.format("Failed to update lottery ticketState to [Printed], orderNos: {0}",
						orderNos);

		daoHelper.batchUpdate(UPDATE_LOTTERY_PRINT_STATE, batchArgs, errMsg);
	}

	@Override
	public void updateDistributeState(Lottery lottery) {
		String errMsg = MessageFormat
				.format("Failed to update lottery ticketState to [Distributed] by orderNo [{1}]",
						lottery.getTicketState(), lottery.getOrderNo());
		daoHelper.update(UPDATE_LOTTERY_DISTRIBUTE_STATE, errMsg, lottery
				.getMerchant(), lottery.getOrderNo());
	}

	@Override
	public void updateFetchState(Lottery lottery) {
		String errMsg = MessageFormat
				.format("Failed to update lottery ticket fetch time by orderNo [{1}]",
						lottery.getTicketState(), lottery.getOrderNo());
		daoHelper.update(UPDATE_LOTTERY_FETCH_STATE, errMsg, lottery.getOrderNo());
	}

	@Override
	public List<Lottery> getPrintedLotteries(LotteryType lotteryType, int period) {
		String errMsg = MessageFormat.format(
				"Failed to query printed lottery by lotteryType [{0}], peroid [{1}]",
				lotteryType, period);
		List<Lottery> lotteries = daoHelper.queryForList(
				GET_LOTTERY_BY_TICKET_STATE, lotteryMapper, errMsg, period, lotteryType.toString(), TicketState.Printed.toString());

		getLotteryNumbers(lotteries);

		return lotteries;
	}

	@Override
	public void updatePrizeInfo(List<Lottery> prizeLotteries) {
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (Lottery lottery : prizeLotteries) {
			Object[] args = new Object[3];
			args[0] = lottery.getPrizeInfo();
			args[1] = lottery.getPrizeBonus();
			args[2] = lottery.getId();

			batchArgs.add(args);
		}
		
		String errMsg = "Failed to update lottery prize info.";

		daoHelper.batchUpdate(UPDATE_LOTTERY_PRIZE_INFO, batchArgs, errMsg);
	}

}
