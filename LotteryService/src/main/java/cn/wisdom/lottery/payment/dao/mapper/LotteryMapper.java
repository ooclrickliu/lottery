package cn.wisdom.lottery.payment.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import cn.wisdom.lottery.payment.dao.constant.DBConstants.TABLES.LOTTERY;
import cn.wisdom.lottery.payment.dao.constant.BusinessType;
import cn.wisdom.lottery.payment.dao.constant.LotteryType;
import cn.wisdom.lottery.payment.dao.constant.TicketState;
import cn.wisdom.lottery.payment.dao.vo.Lottery;

public class LotteryMapper implements RowMapper<Lottery>{

	@Override
	public Lottery mapRow(ResultSet rs, int rowNum) throws SQLException {
		Lottery lottery = new Lottery();
		lottery.setId(rs.getLong(LOTTERY.ID));
		lottery.setOrderNo(rs.getString(LOTTERY.ORDER_NO));
		lottery.setLotterType(LotteryType.valueOf(rs.getString(LOTTERY.LOTTER_TYPE)));
		lottery.setBusinessType(BusinessType.valueOf(rs.getString(LOTTERY.BUSINESS_TYPE)));
		lottery.setTimes(rs.getInt(LOTTERY.TIMES));
		lottery.setTicketState(TicketState.valueOf(rs.getString(LOTTERY.TICKET_STATE)));
		lottery.setOwner(rs.getLong(LOTTERY.OWNER));
		lottery.setMerchant(rs.getLong(LOTTERY.MERCHANT));
		lottery.setDistributeTime(rs.getTimestamp(LOTTERY.DISTRIBUTE_TIME));
		lottery.setTicketPrintTime(rs.getTimestamp(LOTTERY.TICKET_PRINT_TIME));
		lottery.setTicketFetchTime(rs.getTimestamp(LOTTERY.TICKET_FETCH_TIME));
		lottery.setPrizeInfo(rs.getString(LOTTERY.PRIZE_INFO));
		lottery.setPrizeBonus(rs.getInt(LOTTERY.PRIZE_BONUS));
		lottery.setCreateTime(rs.getTimestamp(LOTTERY.CREATE_TIME));
		lottery.setUpdateTime(rs.getTimestamp(LOTTERY.UPDATE_TIME));
		
		
		return lottery;
	}

}
