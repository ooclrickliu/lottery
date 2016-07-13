package cn.wisdom.lottery.payment.dao.vo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.wisdom.lottery.payment.common.exception.OVTException;
import cn.wisdom.lottery.payment.common.utils.DateTimeUtils;
import cn.wisdom.lottery.payment.common.utils.JsonUtils;
import cn.wisdom.lottery.payment.dao.annotation.Column;
import cn.wisdom.lottery.payment.dao.constant.BusinessType;
import cn.wisdom.lottery.payment.dao.constant.LotteryType;
import cn.wisdom.lottery.payment.dao.constant.TicketState;

/**
 * Lottery代表一张彩票，一张彩票可包含多组数，每组数可以是单式，也可以是复式.
 * 例如：
 * 02,04,11,16,27,29+14  <单式>
 * 05,06,10,17,19,23,25,29,32+03,18  <复式>
 * 
 * @author zhi.liu
 *
 */
public class Lottery extends BaseEntity {

	@Column("order_no")
	private String orderNo;

	@Column("lotter_type")
	private String lotteryTypeValue;
	private LotteryType lotteryType;

	@Column("business_type")
	private String businessTypeValue;
	private BusinessType businessType;

	private List<Integer> periods = new ArrayList<Integer>();

	@Column("times")
	private int times;
	
	private List<LotteryNumber> numbers = new ArrayList<LotteryNumber>();

	@Column("ticket_state")
	private String ticketStateValue;
	private TicketState ticketState;

	private boolean fetched;

	@Column("owner")
	private long owner;

	@Column("merchant")
	private long merchant;

	@Column("distribute_time")
	private Timestamp distributeTime;

	@Column("ticket_print_time")
	private Timestamp ticketPrintTime;

	@Column("ticket_fetch_time")
	private Timestamp ticketFetchTime;

	@Column("prize_info")
	private String prizeInfo;

	@Column("prize_bonus")
	private int prizeBonus;
	
	public static void main(String[] args) {
		Lottery lottery = new Lottery();
		lottery.setOrderNo("2016062910012001");
		lottery.setLotteryType(LotteryType.SSQ);
		lottery.setBusinessType(BusinessType.Private);
		lottery.setTimes(5);
		lottery.setTicketState(TicketState.CanPrint);
		lottery.setFetched(false);
		lottery.setOwner(10001);
		lottery.setMerchant(1203);
		lottery.setDistributeTime(DateTimeUtils.getCurrentTimestamp());
		lottery.setTicketPrintTime(DateTimeUtils.getCurrentTimestamp());
		lottery.setPrizeBonus(9200);
		
		List<Integer> periods = new ArrayList<Integer>();
		periods.add(2016067);
		lottery.setPeriods(periods);
		
		List<LotteryNumber> numbers = new ArrayList<LotteryNumber>();
		numbers.add(new LotteryNumber("08,10,11,20,21,27+11"));
		numbers.add(new LotteryNumber("06,14,15,19,24,28,32+05,10"));
		lottery.setNumbers(numbers);
		
		try {
			Map<Long, Map<Integer, Integer>> prizeInfo = new HashMap<Long, Map<Integer,Integer>>();
			Map<Integer, Integer> hitInfo = new HashMap<Integer, Integer>();
			hitInfo.put(3, 5);
			hitInfo.put(4, 16);
			prizeInfo.put(230L, hitInfo);
			lottery.setPrizeInfo(JsonUtils.toJson(prizeInfo));
		} catch (OVTException e) {
		}
		
		System.out.println(lottery);
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public TicketState getTicketState() {
		if (ticketState == null) {
			ticketState = TicketState.valueOf(ticketStateValue);
		}
		
		return ticketState;
	}

	public void setTicketState(TicketState ticketState) {
		this.ticketState = ticketState;
		this.ticketStateValue = ticketState.toString();
	}

	public long getOwner() {
		return owner;
	}

	public void setOwner(long owner) {
		this.owner = owner;
	}

	public Timestamp getTicketPrintTime() {
		return ticketPrintTime;
	}

	public void setTicketPrintTime(Timestamp ticketPrintTime) {
		this.ticketPrintTime = ticketPrintTime;
	}

	public int getPrizeBonus() {
		return prizeBonus;
	}

	public void setPrizeBonus(int prizeBonus) {
		this.prizeBonus = prizeBonus;
	}

	public BusinessType getBusinessType() {
		if (businessType == null) {
			businessType = BusinessType.valueOf(businessTypeValue);
		}
		return businessType;
	}

	public void setBusinessType(BusinessType businessType) {
		this.businessType = businessType;
		this.businessTypeValue = businessType.toString();
	}

	public Timestamp getDistributeTime() {
		return distributeTime;
	}

	public void setDistributeTime(Timestamp distributeTime) {
		this.distributeTime = distributeTime;
	}

	public Timestamp getTicketFetchTime() {
		return ticketFetchTime;
	}

	public void setTicketFetchTime(Timestamp ticketFetchTime) {
		this.ticketFetchTime = ticketFetchTime;
		this.fetched = true;
	}

	public boolean isFetched() {
		if (!fetched && ticketFetchTime != null) {
			fetched = true;
		}
		
		return fetched;
	}

	public void setFetched(boolean fetched) {
		this.fetched = fetched;
	}

	public long getMerchant() {
		return merchant;
	}

	public void setMerchant(long merchant) {
		this.merchant = merchant;
	}

	public String getPrizeInfo() {
		return prizeInfo;
	}

	public void setPrizeInfo(String prizeInfo) {
		this.prizeInfo = prizeInfo;
	}

	public List<LotteryNumber> getNumbers() {
		return numbers;
	}

	public void setNumbers(List<LotteryNumber> numbers) {
		this.numbers = numbers;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public List<Integer> getPeriods() {
		return periods;
	}

	public void setPeriods(List<Integer> periods) {
		this.periods = periods;
	}

	public LotteryType getLotteryType() {
		if (lotteryType == null) {
			lotteryType = LotteryType.valueOf(lotteryTypeValue);
		}
		return lotteryType;
	}

	public void setLotteryType(LotteryType lotteryType) {
		this.lotteryType = lotteryType;
		this.lotteryTypeValue = lotteryType.toString();
	}
}
