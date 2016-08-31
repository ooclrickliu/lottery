/**
 * PageInfo.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 12, 2015
 */
package cn.wisdom.lottery.dao.vo;

import cn.wisdom.lottery.dao.constant.QueryDirection;


/**
 * PageInfo
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class PageInfo
{
	public static final String PARAM_START = "start";
	
	public static final String PARAM_COUNT = "count";
	
	public static final String PARAM_DIRECT = "direct";

    private long start = 0;

    private int count = 10;
    
    private QueryDirection direction = QueryDirection.DOWN;

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public QueryDirection getDirection() {
		return direction;
	}

	public void setDirection(QueryDirection direction) {
		this.direction = direction;
	}

}
