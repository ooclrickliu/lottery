package cn.wisdom.lottery.service.wx.message;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutNewsMessage;
import me.chanjar.weixin.mp.bean.outxmlbuilder.NewsBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.wisdom.lottery.common.utils.DataConvertUtils;
import cn.wisdom.lottery.dao.vo.AppProperty;

@Component
public class HelpMessageBuilder implements MessageBuilder {
	
	@Autowired
	private WxMpEventHandler wxMpEventHandler;
	
	private WxMpXmlOutNewsMessage.Item title;
	
	private WxMpXmlOutNewsMessage.Item menu;
	
	private WxMpXmlOutNewsMessage.Item note;

	@Autowired
	private AppProperty appProperty;
	
	@PostConstruct
	private void init() {
		
		initArticle();
		
		initSubMenu();
		
		wxMpEventHandler.registerMessageBuilder(this);
	}

	private void initArticle() {
		title = new WxMpXmlOutNewsMessage.Item();
		title.setTitle("欢迎光临千彩慧友");
		
		menu = new WxMpXmlOutNewsMessage.Item();
		String content = "1. 平台简介\n";
		content += "2. 购买\n";
		content += "3. 兑奖\n";
		content += "4. 红包\n";
		content += "5. 开奖查询";
		menu.setTitle(content);
		menu.setPicUrl(appProperty.imgServerUrl + "img/help.png");

		note = new WxMpXmlOutNewsMessage.Item();
		content = "回复对应数字查看使用说明";
		note.setTitle(content);
	}

	@Override
	public WxMpXmlOutMessage buildMessage(WxMpXmlMessage wxMessage) {
		NewsBuilder news = WxMpXmlOutMessage.NEWS();
		news.addArticle(title);
		news.addArticle(menu);
		news.addArticle(note);
		
		return news.fromUser(wxMessage.getToUserName()).toUser(wxMessage.getFromUserName()).build();
	}

	@Override
	public String getMenuKey() {
		return "help";
	}

	private static Map<Integer, String> helpMenu = new HashMap<Integer, String>();
	static {
		helpMenu.put(1, "本公众号是与线下实体彩票站合作运营，我们承诺通过本公众号购买的每一注彩票都会及时出票并上传实体彩票照片，您可以通过\"我的-购买记录\"查看彩票照片。");
		helpMenu.put(21, "双色球每期投注截止时间为每周二、周四、周日晚上7:00，7:00以后投注的为下一期的彩票。");
		helpMenu.put(22, "目前支持通过识别二维码支付，支付完成后需要您截取并上传支付成功界面。");
		helpMenu.put(23, "第一步：在微信\"我\" -> \"钱包\" -> 右上角子菜单\"交易记录\"中找到支付记录并截图;\n\n第二步：回到\"千彩慧友\"公众号，在\"我的\" -> \"购买记录\" -> \"待支付\"中找到对应彩票，点击\"上传支付凭证\"选择第一步中的截图，完成即可。");
		helpMenu.put(24, "公众号在彩票出票后会给您发通知，您可点击出票通知或我的-购买记录-彩票详情中查看实体彩票照片；由于每天我们需要打印的彩票非常多，我们会在每天12点和19点对所有的彩票集中出票，请您耐心等待。");
		helpMenu.put(31, "当您的彩票中奖时，我们的客服会及时联系您\n(1) 1万元以下的奖金：支持通过微信/支付宝/银行卡红包/转账方式兑奖；\n(2) 1万元以上的奖金：可选择我们帮您代领，再通过微信/支付宝/银行卡转账给您；也可选择邮寄彩票(邮费到付，保费协商)，您自行兑奖。");
		helpMenu.put(32, "我们不收费。当然，您的打赏会让小二更加有激情。");
		helpMenu.put(41, "奖金红包指发送者发送的红包中包含一组或多组数字，抢红包者每人抢到的是中奖金额的份额，例如A抢到了10%，B抢到了6%，若中奖则奖金按比例分配。");
		helpMenu.put(42, "数字红包指发送者发送的红包中包含一组或多组数字，红包数量为数字组数，抢红包者每人可以抢一组数字，每组数字的中奖金额为抢到者所得。");
		helpMenu.put(43, "为了保证我们的客服能联系到红包中奖者，领红包需先关注公众号，奖金发放方式同兑奖方式(请输入31了解如何兑奖)");
		helpMenu.put(44, "在\"我的\" -> \"红包\"页面选择不同的红包类型可发送和查看红包");
		helpMenu.put(5, "请输入期数查询开奖号码，例如：2016099");
	}
	private static Map<Integer, NewsBuilder> subMenu = new HashMap<Integer, NewsBuilder>();

	private void initSubMenu() {

		// 2
		NewsBuilder gm = WxMpXmlOutMessage.NEWS();
		WxMpXmlOutNewsMessage.Item title = new WxMpXmlOutNewsMessage.Item();
		title.setTitle("购买");
		WxMpXmlOutNewsMessage.Item menu = new WxMpXmlOutNewsMessage.Item();
		String content = "21. 投注截止时间\n";
		content += "22. 支付\n";
		content += "23. 忘记上传支付截图\n";
		content += "24. 出票";
		menu.setTitle(content);
		menu.setPicUrl(appProperty.imgServerUrl + "img/pay.png");
		WxMpXmlOutNewsMessage.Item note = new WxMpXmlOutNewsMessage.Item();
		content = "回复对应数字查看使用说明";
		note.setTitle(content);
		gm.addArticle(title);
		gm.addArticle(menu);
		gm.addArticle(note);
		subMenu.put(2, gm);
		
		// 3
		NewsBuilder dj = WxMpXmlOutMessage.NEWS();
		title = new WxMpXmlOutNewsMessage.Item();
		title.setTitle("兑奖");
		menu = new WxMpXmlOutNewsMessage.Item();
		content = "31. 如何兑奖\n";
		content += "32. 兑奖是否收费";
		menu.setTitle(content);
		menu.setPicUrl(appProperty.imgServerUrl + "img/bonus.png");
		dj.addArticle(title);
		dj.addArticle(menu);
		dj.addArticle(note);
		subMenu.put(3, dj);
		
		// 4
		NewsBuilder hb = WxMpXmlOutMessage.NEWS();
		title = new WxMpXmlOutNewsMessage.Item();
		title.setTitle("红包");
		menu = new WxMpXmlOutNewsMessage.Item();
		content = "41. 什么是奖金红包\n";
		content += "42. 什么是数字红包\n";
		content += "43. 如何领红包\n";
		content += "44. 如何发红包";
		menu.setTitle(content);
		menu.setPicUrl(appProperty.imgServerUrl + "img/redpack.png");
		hb.addArticle(title);
		hb.addArticle(menu);
		hb.addArticle(note);
		subMenu.put(4, hb);
	}
	
	public WxMpXmlOutMessage buildHelpMessage(WxMpXmlMessage wxMessage) {
		WxMpXmlOutMessage response = null;
		
		int code = DataConvertUtils.toInt(wxMessage.getContent());
		String helpMessage = helpMenu.get(code);
		NewsBuilder subHelpMenu = subMenu.get(code);
		if (helpMessage != null) {
			response = WxMpXmlOutMessage.TEXT().content(helpMessage).fromUser(wxMessage.getToUserName()).toUser(wxMessage.getFromUserName()).build();
		}
		else if (subHelpMenu != null) {
			response = subHelpMenu.fromUser(wxMessage.getToUserName()).toUser(wxMessage.getFromUserName()).build();
		}
		return response;
	}
	
	public String getHelpMessage(int code) {
		return helpMenu.get(code);
	}

}
