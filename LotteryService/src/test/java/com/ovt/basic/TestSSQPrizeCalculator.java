package com.ovt.basic;

import cn.wisdom.lottery.service.SSQPrizeCalculator;

public class TestSSQPrizeCalculator {
	public static void main(String[] args) {
		SSQPrizeCalculator calculator = new SSQPrizeCalculator();

		int rTotal, bTotal, rHits, bHits;

		// -----------------单注-------------------//
		// 1. 6+1
		rTotal = 6;
		bTotal = 1;

		// 1.1 blue: 1 red: 0
		bHits = 1;
		rHits = 0;
		calculator.getPrizeInfo(rTotal, bTotal, rHits, bHits);

		// 1.1 blue: 1 red: 1
		bHits = 1;
		rHits = 1;
		calculator.getPrizeInfo(rTotal, bTotal, rHits, bHits);

		// 1.1 blue: 1 red: 2
		bHits = 1;
		rHits = 2;
		calculator.getPrizeInfo(rTotal, bTotal, rHits, bHits);

		// 1.1 blue: 1 red: 3
		bHits = 1;
		rHits = 3;
		calculator.getPrizeInfo(rTotal, bTotal, rHits, bHits);

		// 1.1 blue: 1 red: 4
		bHits = 1;
		rHits = 4;
		calculator.getPrizeInfo(rTotal, bTotal, rHits, bHits);

		// 1.1 blue: 1 red: 5
		bHits = 1;
		rHits = 5;
		calculator.getPrizeInfo(rTotal, bTotal, rHits, bHits);

		// 1.1 blue: 1 red: 6
		bHits = 1;
		rHits = 6;
		calculator.getPrizeInfo(rTotal, bTotal, rHits, bHits);

		// 1.2 blue: 0 red: 1
		bHits = 0;
		rHits = 1;
		calculator.getPrizeInfo(rTotal, bTotal, rHits, bHits);

		// 1.2 blue: 0 red: 2
		bHits = 0;
		rHits = 2;
		calculator.getPrizeInfo(rTotal, bTotal, rHits, bHits);

		// 1.2 blue: 0 red: 3
		bHits = 0;
		rHits = 3;
		calculator.getPrizeInfo(rTotal, bTotal, rHits, bHits);

		// 1.2 blue: 0 red: 4
		bHits = 0;
		rHits = 4;
		calculator.getPrizeInfo(rTotal, bTotal, rHits, bHits);

		// 1.2 blue: 0 red: 5
		bHits = 0;
		rHits = 5;
		calculator.getPrizeInfo(rTotal, bTotal, rHits, bHits);

		// 1.2 blue: 0 red: 6
		bHits = 0;
		rHits = 6;
		calculator.getPrizeInfo(rTotal, bTotal, rHits, bHits);

		// -----------------复式-------------------//
		// 2. 8+2
		// rTotal = 9; bTotal = 3;
		//
		// // 2.1 blue: 1 red: 0
		// bHits = 1; rHits = 0;
		// calculator.getPrizeInfo(rTotal, bTotal, rHits, bHits);
		//
		// // 2.1 blue: 1 red: 1
		// bHits = 1; rHits = 1;
		// calculator.getPrizeInfo(rTotal, bTotal, rHits, bHits);
		//
		// // 2.1 blue: 1 red: 2
		// bHits = 1; rHits = 2;
		// calculator.getPrizeInfo(rTotal, bTotal, rHits, bHits);
		//
		// // 2.1 blue: 1 red: 3
		// bHits = 1; rHits = 3;
		// calculator.getPrizeInfo(rTotal, bTotal, rHits, bHits);
		//
		// // 2.1 blue: 1 red: 4
		// bHits = 1; rHits = 4;
		// calculator.getPrizeInfo(rTotal, bTotal, rHits, bHits);
		//
		// // 2.1 blue: 1 red: 5
		// bHits = 1; rHits = 5;
		// calculator.getPrizeInfo(rTotal, bTotal, rHits, bHits);
		//
		// // 2.1 blue: 1 red: 6
		// bHits = 1; rHits = 6;
		// calculator.getPrizeInfo(rTotal, bTotal, rHits, bHits);
		//
		// // 2.2 blue: 0 red: 1
		// bHits = 0; rHits = 1;
		// calculator.getPrizeInfo(rTotal, bTotal, rHits, bHits);
		//
		// // 2.2 blue: 0 red: 2
		// bHits = 0; rHits = 2;
		// calculator.getPrizeInfo(rTotal, bTotal, rHits, bHits);
		//
		// // 2.2 blue: 0 red: 3
		// bHits = 0; rHits = 3;
		// calculator.getPrizeInfo(rTotal, bTotal, rHits, bHits);
		//
		// // 2.2 blue: 0 red: 4
		// bHits = 0; rHits = 4;
		// calculator.getPrizeInfo(rTotal, bTotal, rHits, bHits);
		//
		// // 2.2 blue: 0 red: 5
		// bHits = 0; rHits = 5;
		// calculator.getPrizeInfo(rTotal, bTotal, rHits, bHits);
		//
		// // 2.2 blue: 0 red: 6
		// bHits = 0; rHits = 6;
		// calculator.getPrizeInfo(rTotal, bTotal, rHits, bHits);
	}
}
