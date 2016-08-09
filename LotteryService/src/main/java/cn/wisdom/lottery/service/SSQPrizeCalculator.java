package cn.wisdom.lottery.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import cn.wisdom.lottery.common.exception.OVTException;
import cn.wisdom.lottery.common.utils.JsonUtils;
import cn.wisdom.lottery.common.utils.MathUtils;

/**
 * 双色球中奖计算器
 * 
 * @author zhi.liu
 *
 */
@Service
public class SSQPrizeCalculator implements LotteryPrizeCalculator {

	@Override
	public Map<String, Integer> getPrizeInfo(int rTotal, int bTotal,
			int rHits, int bHits) {
		
		Map<String, Integer> prizeHitsMap = new HashMap<String, Integer>();
		
		if (bHits == 0) {
			if (rHits == 4) {
				prizeHitsMap.putAll(bhit_0_rhit_4(rTotal, bTotal, rHits, bHits));
			}
			else if (rHits == 5) {
				prizeHitsMap.putAll(bhit_0_rhit_5(rTotal, bTotal, rHits, bHits));
			}
			else if (rHits == 6) {
				prizeHitsMap.putAll(bhit_0_rhit_6(rTotal, bTotal, rHits, bHits));
			}
		}
		else if (bHits == 1) {
			if (rHits < 3) {
				prizeHitsMap.putAll(bhit_1_rhit_less_3(rTotal));
			}
			else if (rHits == 3) {
				prizeHitsMap.putAll(bhit_3_rhit_3(rTotal, rHits));
			}
			else if (rHits == 4) {
				prizeHitsMap.putAll(bhit_1_rhit_4(rTotal, bTotal, rHits, bHits));
			}
			else if (rHits == 5) {
				prizeHitsMap.putAll(bhit_1_rhit_5(rTotal, bTotal, rHits, bHits));
			}
			else if (rHits == 6) {
				prizeHitsMap.putAll(bhit_1_rhit_6(rTotal, bTotal, rHits, bHits));
			}
		}
		
		removeZero(prizeHitsMap);
		
		System.out.println("Red: " + rHits + "/" + rTotal + "  Blue: " + bHits + "/" + bTotal);
		try {
			System.out.println("         " + JsonUtils.toJson(prizeHitsMap));
		} catch (OVTException e) {
		}
		
		return prizeHitsMap;
	}

	private void removeZero(Map<String, Integer> prizeHitsMap) {
		List<String> zeroPrizes = new ArrayList<String>();
		
		int hits;
		for (String prize : prizeHitsMap.keySet()) {
			hits = prizeHitsMap.get(prize);
			
			if (hits == 0) {
				zeroPrizes.add(prize);
			}
		}
		
		for (String zeroPrize : zeroPrizes) {
			prizeHitsMap.remove(zeroPrize);
		}
	}

	private Map<String, Integer> bhit_0_rhit_4(int rTotal, int bTotal, int rHits, int bHits) {
		Map<String, Integer> prizeHitsMap = new HashMap<String, Integer>();
		int prize;
		int hits;
		//*******************/
		prize = 5;
		
		//Cn-4,2
//		hits = MathUtils.Cn_m(rTotal - rHits, 2);
		hits = prize_5(rTotal, bTotal, rHits, bHits);
		prizeHitsMap.put("" + prize, hits);
		
		return prizeHitsMap;
	}

	private Map<String, Integer> bhit_0_rhit_5(int rTotal, int bTotal, int rHits, int bHits) {
		Map<String, Integer> prizeHitsMap = new HashMap<String, Integer>();
		int prize;
		int hits;
		//*******************/
		prize = 4;
		
		//Cn-5,1
//		hits = rTotal - 5;
		hits = prize_4(rTotal, bTotal, rHits, bHits);
		prizeHitsMap.put("" + prize, hits);

		//*******************/
		prize = 5;
		
		//C5,4*Cn-5,2
//		hits = 5 * MathUtils.Cn_m(rTotal - rHits, 2);
		hits = prize_5(rTotal, bTotal, rHits, bHits);
		prizeHitsMap.put("" + prize, hits);
		
		return prizeHitsMap;
	}

	private Map<String, Integer> bhit_0_rhit_6(int rTotal, int bTotal, int rHits, int bHits) {
		Map<String, Integer> prizeHitsMap = new HashMap<String, Integer>();
		int prize;
		int hits;
		//*******************/
		prize = 2;
		
		//bTotal
		hits = bTotal;
		prizeHitsMap.put("" + prize, hits);

		//*******************/
		prize = 4;
		
		//Crh,5*Cn-rh,1
		hits = prize_4(rTotal, bTotal, rHits, bHits);
		prizeHitsMap.put("" + prize, hits);

		//*******************/
		prize = 5;
		
		//Crh,4*Cn-rh,2
		hits = prize_5(rTotal, bTotal, rHits, bHits);
		prizeHitsMap.put("" + prize, hits);
		
		return prizeHitsMap;
	}

	private Map<String, Integer> bhit_1_rhit_less_3(int rTotal) {
		Map<String, Integer> prizeHitsMap = new HashMap<String, Integer>();
		int prize;
		int hits;
		//*******************/
		prize = 6;
		
		hits = MathUtils.Cn_m(rTotal, 6);
		prizeHitsMap.put("" + prize, hits);
		
		return prizeHitsMap;
	}

	private Map<String, Integer> bhit_1_rhit_6(int rTotal, int bTotal, int rHits, int bHits) {
		Map<String, Integer> prizeHitsMap = new HashMap<String, Integer>();
		int prize;
		int hits;
		//*******************/
		prize = 1;
		hits = 1;
		prizeHitsMap.put("" + prize, hits);
		
		//*******************/
		prize = 2;
		
		//bTotal-1
		hits = bTotal-1;
		prizeHitsMap.put("" + prize, hits);
		
		//*******************/
		//Crh,5*Cn-rh,1
		prize = 3;
		hits = MathUtils.Cn_m(rHits, 5) * MathUtils.Cn_m(rTotal - rHits, 1);
		prizeHitsMap.put("" + prize, hits);
		
		//*******************/
		prize = 4;
		
		//Crh,4*Cn-rh,2
		hits = prize_4(rTotal, bTotal, rHits, bHits);
		prizeHitsMap.put("" + prize, hits);

		//*******************/
		prize = 5;
		hits = prize_5(rTotal, bTotal, rHits, bHits);
		prizeHitsMap.put("" + prize, hits);
		
		//*******************/
		prize = 6;
		
		hits = prize_6(rTotal, rHits);
		prizeHitsMap.put("" + prize, hits);
		
		return prizeHitsMap;
	}

	private Map<String, Integer> bhit_1_rhit_5(int rTotal, int bTotal, int rHits, int bHits) {
		Map<String, Integer> prizeHitsMap = new HashMap<String, Integer>();
		int prize;
		int hits;
		//*******************/
		prize = 3;
		hits = MathUtils.Cn_m(rTotal - rHits, 1);
		prizeHitsMap.put("" + prize, hits);
		
		//*******************/
		prize = 4;
		
		hits = prize_4(rTotal, bTotal, rHits, bHits);
		prizeHitsMap.put("" + prize, hits);

		//*******************/
		prize = 5;
		hits = prize_5(rTotal, bTotal, rHits, bHits);
		prizeHitsMap.put("" + prize, hits);
		
		//*******************/
		prize = 6;
		
		hits = prize_6(rTotal, rHits);
		prizeHitsMap.put("" + prize, hits);
		
		return prizeHitsMap;
	}

	private Map<String, Integer> bhit_1_rhit_4(int rTotal, int bTotal, int rHits, int bHits) {
		Map<String, Integer> prizeHitsMap = new HashMap<String, Integer>();
		int prize;
		int hits;
		//*******************/
		prize = 4;
		
		//Cn-4,2
		hits = MathUtils.Cn_m(rTotal - rHits, 2);
		prizeHitsMap.put("" + prize, hits);

		//*******************/
		prize = 5;
		hits = prize_5(rTotal, bTotal, rHits, bHits);
		prizeHitsMap.put("" + prize, hits);
		
		//*******************/
		prize = 6;
		
		hits = prize_6(rTotal, rHits);
		prizeHitsMap.put("" + prize, hits);
		
		return prizeHitsMap;
	}

	private Map<String, Integer> bhit_3_rhit_3(int rTotal, int rHits) {
		Map<String, Integer> prizeHitsMap = new HashMap<String, Integer>();
		int prize;
		int hits;
		//*******************/
		prize = 5;
		
		//Cn-3,3
		hits = MathUtils.Cn_m(rTotal - rHits, 3);
		prizeHitsMap.put("" + prize, hits);
		
		//*******************/
		prize = 6;
		
		hits = prize_6(rTotal, rHits);
		prizeHitsMap.put("" + prize, hits);
		
		return prizeHitsMap;
	}

	private int prize_6(int rTotal, int rHits) {
		int hits;
		//Crh,2*Cn-rh,4
		hits = (MathUtils.Cn_m(rHits, 2) * (MathUtils.Cn_m(rTotal - rHits, 4)));
		
		//Crh,1*Cn-rh,5
		hits += (MathUtils.Cn_m(rHits, 1) * (MathUtils.Cn_m(rTotal - rHits, 5)));
		
		//Cn-rh,6
		hits += MathUtils.Cn_m(rTotal - rHits, 6);
		return hits;
	}

	private int prize_5(int rTotal, int bTotal, int rHits, int bHits) {
		int hits;
		//3+1: Crh,3*Cn-rh,3  
		hits = MathUtils.Cn_m(rHits, 3) * MathUtils.Cn_m(rTotal - rHits, 3) * MathUtils.Cn_m(bHits, 1);
		//4+0: Crh,4*Cn-rh,2 * (bTotal-bHits)
		hits += MathUtils.Cn_m(rHits, 4) * MathUtils.Cn_m(rTotal - rHits, 2) * (bTotal-bHits);
		return hits;
	}

	private int prize_4(int rTotal, int bTotal, int rHits, int bHits) {
		int hits;
		//4+1: Crh,4*Cn-rh,2
		hits = MathUtils.Cn_m(rHits, 4) * (MathUtils.Cn_m(rTotal - rHits, 2)) * MathUtils.Cn_m(bHits, 1);
		//5+0: Crh,5*Cn-rh,1*(bTotal-bHits)
		hits += MathUtils.Cn_m(rHits, 5) * MathUtils.Cn_m(rTotal - rHits, 1) * (bTotal-bHits);
		return hits;
	}

}
