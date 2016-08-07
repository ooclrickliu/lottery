package cn.wisdom.lottery.dao.vo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.wisdom.lottery.common.exception.OVTException;
import cn.wisdom.lottery.common.utils.DateTimeUtils;
import cn.wisdom.lottery.common.utils.JsonUtils;
import cn.wisdom.lottery.dao.annotation.Column;
import cn.wisdom.lottery.dao.constant.BusinessType;
import cn.wisdom.lottery.dao.constant.LotteryType;
import cn.wisdom.lottery.dao.constant.PayState;
import cn.wisdom.lottery.dao.constant.PrizeState;

/**
 * Lottery代表一张彩票，一张彩票可包含多组数，每组数可以是单式，也可以是复式. 例如： 02,04,11,16,27,29+14 <单式>
 * 05,06,10,17,19,23,25,29,32+03,18 <复式>
 * 
 * @author zhi.liu
 * 
 */
public class Lottery extends BaseEntity
{
    @Column("order_no")
    private String orderNo;

    @Column("total_fee")
    private float totalFee;

    @Column("remark")
    private String remark;

    @Column("lottery_type")
    private String lotteryTypeValue;
    private LotteryType lotteryType;

    // public(红包,无owner) , private(自己购买)
    @Column("business_type")
    private String businessTypeValue;
    private BusinessType businessType;

    private List<LotteryPeriod> periods = new ArrayList<LotteryPeriod>();

    @Column("times")
    private int times;

    private List<LotteryNumber> numbers = new ArrayList<LotteryNumber>();

    @Column("pay_state")
    private String payStateValue;
    private PayState payState;
    
    @Column("owner")
    private long owner;

    @Column("merchant")
    private long merchant;

    @Column("distribute_time")
    private Timestamp distributeTime;

    public static void main(String[] args)
    {
        Lottery lottery = new Lottery();
        lottery.setOrderNo("2016062910012001");
        lottery.setLotteryType(LotteryType.SSQ);
        lottery.setBusinessType(BusinessType.Private);
        lottery.setTimes(5);
        lottery.setOwner(10001);
        lottery.setMerchant(1203);
        lottery.setDistributeTime(DateTimeUtils.getCurrentTimestamp());

        List<LotteryPeriod> periods = new ArrayList<LotteryPeriod>();
        LotteryPeriod period = new LotteryPeriod();
        period.setPrizeState(PrizeState.NotOpen);
        period.setTicketPrintTime(DateTimeUtils.getCurrentTimestamp());
        period.setPrizeBonus(9200);
        period.setFetched(false);
        try
        {
            Map<Long, Map<Integer, Integer>> prizeInfo = new HashMap<Long, Map<Integer, Integer>>();
            Map<Integer, Integer> hitInfo = new HashMap<Integer, Integer>();
            hitInfo.put(3, 5);
            hitInfo.put(4, 16);
            prizeInfo.put(230L, hitInfo);
            period.setPrizeInfo(JsonUtils.toJson(prizeInfo));
        }
        catch (OVTException e)
        {
        }
        
        periods.add(period);
        lottery.setPeriods(periods);

        List<LotteryNumber> numbers = new ArrayList<LotteryNumber>();
        numbers.add(new LotteryNumber("08,10,11,20,21,27+11"));
        numbers.add(new LotteryNumber("06,14,15,19,24,28,32+05,10"));
        lottery.setNumbers(numbers);

        System.out.println(lottery);
    }

    public String getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    public float getTotalFee()
    {
        return totalFee;
    }

    public void setTotalFee(float totalFee)
    {
        this.totalFee = totalFee;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public long getOwner()
    {
        return owner;
    }

    public void setOwner(long owner)
    {
        this.owner = owner;
    }

    public BusinessType getBusinessType()
    {
        if (businessType == null)
        {
            businessType = BusinessType.valueOf(businessTypeValue);
        }
        return businessType;
    }

    public void setBusinessType(BusinessType businessType)
    {
        this.businessType = businessType;
        this.businessTypeValue = businessType.toString();
    }

    public Timestamp getDistributeTime()
    {
        return distributeTime;
    }

    public void setDistributeTime(Timestamp distributeTime)
    {
        this.distributeTime = distributeTime;
    }

    public long getMerchant()
    {
        return merchant;
    }

    public void setMerchant(long merchant)
    {
        this.merchant = merchant;
    }

    public List<LotteryNumber> getNumbers()
    {
        return numbers;
    }

    public void setNumbers(List<LotteryNumber> numbers)
    {
        this.numbers = numbers;
    }

    public int getTimes()
    {
        return times;
    }

    public void setTimes(int times)
    {
        this.times = times;
    }

    public LotteryType getLotteryType()
    {
        if (lotteryType == null)
        {
            lotteryType = LotteryType.valueOf(lotteryTypeValue);
        }
        return lotteryType;
    }

    public void setLotteryType(LotteryType lotteryType)
    {
        this.lotteryType = lotteryType;
        this.lotteryTypeValue = lotteryType.toString();
    }

	public List<LotteryPeriod> getPeriods() {
		return periods;
	}

	public void setPeriods(List<LotteryPeriod> periods) {
		this.periods = periods;
	}

	public PayState getPayState() {
		if (payState == null)
        {
			payState = PayState.valueOf(payStateValue);
        }
		return payState;
	}

	public void setPayState(PayState payState) {
		this.payState = payState;
		this.payStateValue = payState.toString();
	}
}
