package cn.wisdom.lottery.dao.vo;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import cn.wisdom.lottery.common.exception.OVTException;
import cn.wisdom.lottery.common.utils.CollectionUtils;
import cn.wisdom.lottery.common.utils.JsonUtils;
import cn.wisdom.lottery.common.utils.StringUtils;
import cn.wisdom.lottery.dao.annotation.Column;
import cn.wisdom.lottery.dao.constant.PrizeState;

public class LotteryPeriod extends BaseEntity {
	
	@Column("lottery_id")
	private long lotteryId;

	@Column("period")
	private int period;

    @Column("prize_state")
    private String prizeStateValue;
    private PrizeState prizeState;

    @Column("ticket_print_time")
    private Timestamp ticketPrintTime;

    @Column("ticket_fetch_time")
    private Timestamp ticketFetchTime;

    @Column("prize_info")
    private String prizeInfo;
    private Map<String, Map<String, Integer>> prizeInfoMap; 

    @Column("prize_bonus")
    private int prizeBonus;

    private boolean fetched;

	public long getLotteryId() {
		return lotteryId;
	}

	public void setLotteryId(long lotteryId) {
		this.lotteryId = lotteryId;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

    public PrizeState getPrizeState()
    {
        if (prizeState == null && StringUtils.isNotBlank(prizeStateValue))
        {
        	prizeState = PrizeState.valueOf(prizeStateValue);
        }

        return prizeState;
    }

    public void setPrizeState(PrizeState prizeState)
    {
        this.prizeState = prizeState;
        this.prizeStateValue = prizeState.toString();
    }

    public Timestamp getTicketPrintTime()
    {
        return ticketPrintTime;
    }

    public void setTicketPrintTime(Timestamp ticketPrintTime)
    {
        this.ticketPrintTime = ticketPrintTime;
    }

    public int getPrizeBonus()
    {
        return prizeBonus;
    }

    public void setPrizeBonus(int prizeBonus)
    {
        this.prizeBonus = prizeBonus;
    }

    public Timestamp getTicketFetchTime()
    {
        return ticketFetchTime;
    }

    public void setTicketFetchTime(Timestamp ticketFetchTime)
    {
        this.ticketFetchTime = ticketFetchTime;
        this.fetched = true;
    }

    public boolean isFetched()
    {
        if (!fetched && ticketFetchTime != null)
        {
            fetched = true;
        }

        return fetched;
    }

    public void setFetched(boolean fetched)
    {
        this.fetched = fetched;
    }

    public String getPrizeInfo()
    {
        return prizeInfo;
    }

    public void setPrizeInfo(String prizeInfo)
    {
        this.prizeInfo = prizeInfo;
    }

	@SuppressWarnings("unchecked")
	public Map<String, Map<String, Integer>> getPrizeInfoMap() {
		if (prizeInfoMap == null && StringUtils.isNotBlank(prizeInfo)) {
			try {
				prizeInfoMap = JsonUtils.fromJson(prizeInfo, Map.class);
			} catch (OVTException e) {
				e.printStackTrace();
			}
		}
		
		return prizeInfoMap;
	}

	public void setPrizeInfoMap(Map<String, Map<String, Integer>> prizeInfoMap) {
		this.prizeInfoMap = prizeInfoMap;
		try {
			this.prizeInfo = JsonUtils.toJson(prizeInfoMap);
		} catch (OVTException e) {
			e.printStackTrace();
		}
	}

	public Map<String, Integer> getPrizeMap() {
		Map<String, Integer> prizeMap = new HashMap<String, Integer>();
		
		Map<String, Map<String, Integer>> prizeInfoMap = this.getPrizeInfoMap();
		if (CollectionUtils.isNotEmpty(prizeInfoMap)) {
			for (String numberId : prizeInfoMap.keySet()) {
				Map<String, Integer> map = prizeInfoMap.get(numberId);
				for (String rank : map.keySet()) {
					Integer count = prizeMap.get(rank);
					if (count == null) {
						count = 0;
					}
					count += map.get(rank);
					
					prizeMap.put(rank, count);
				}
			}
		}
		return prizeMap;
	}

//	public void setPrizeMap(Map<Long, Map<Integer, Integer>> prizeMap) {
//		this.prizeMap = prizeMap;
//	}
}
