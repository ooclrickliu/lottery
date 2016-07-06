package cn.wisdom.lottery.payment.service.wx;

import cn.wisdom.lottery.payment.service.wx.event.MenuClickEvent;
import cn.wisdom.lottery.payment.service.wx.event.ScanEvent;
import cn.wisdom.lottery.payment.service.wx.event.SubscribeEvent;

public interface WXEnventListener {

	/**
	 * 关注关注事件
	 * 
	 * @param event
	 */
	void onSubscribe(SubscribeEvent event);
	
	/**
	 * 取消关注事件
	 * 
	 * @param event
	 */
	void onUnSubscribe(SubscribeEvent event);
	
	/**
	 * 扫码事件
	 * 
	 * @param event
	 */
	void onScan(ScanEvent event);
	
	/**
	 * 点击菜单事件
	 * 
	 * @param event
	 */
	void onMenuClick(MenuClickEvent event);
}
