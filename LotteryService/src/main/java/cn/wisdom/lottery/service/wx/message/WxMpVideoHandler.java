package cn.wisdom.lottery.service.wx.message;

import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSession;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpCustomMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.wisdom.lottery.common.log.Logger;
import cn.wisdom.lottery.common.log.LoggerFactory;
import cn.wisdom.lottery.common.utils.DataConvertUtils;
import cn.wisdom.lottery.dao.vo.AppProperty;
import cn.wisdom.lottery.dao.vo.User;
import cn.wisdom.lottery.service.LotteryServiceFacade;
import cn.wisdom.lottery.service.UserService;
import cn.wisdom.lottery.service.wx.WXService;

@Component
public class WxMpVideoHandler extends AbstractWxMpHandler {

	private static final String PARAM_USER = "user";

	@Autowired
	private AppProperty appProperty;

	@Autowired
	private LotteryServiceFacade lotteryServiceFacade;

	@Autowired
	private UserService userService;

	@Autowired
	private WXService wxService;

	private Logger logger = LoggerFactory.getLogger(WxMpVideoHandler.class
			.getName());

	@Override
	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
			Map<String, Object> context, WxMpService wxMpService,
			WxSessionManager sessionManager) throws WxErrorException {
		WxMpXmlOutMessage response = null;

		buildKfResponse(wxMessage, wxMpService, sessionManager);

		return response;
	}

	private void buildKfResponse(WxMpXmlMessage wxMessage,
			WxMpService wxMpService, WxSessionManager sessionManager) {
		try {
			// download the video
			logger.debug("Start to download vedio...");
			File video = wxService.getWxMpService().mediaDownload(
					wxMessage.getMediaId());

			logger.debug("Download vedio done!");
			
			// convert to images
			if (video != null) {
				long start = System.currentTimeMillis();
				logger.debug("Start converting vedio to images...");
				int frameNum = video2Images(video);
				logger.debug("Complete converting vedio to images.");
				long end = System.currentTimeMillis();

				logger.debug("Extract Images Cost : " + (end - start));

//				video.delete();
			}

		} catch (WxErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int video2Images(File vedioFile) {
		int frameNum = 0;
		File outFile = null;
		FileOutputStream fos = null;

		String framesDir = mkFrameDir(vedioFile);

		try {
			FFmpegFrameGrabber ocg = FFmpegFrameGrabber
					.createDefault(vedioFile);
			ocg.start();

			Java2DFrameConverter converter = new Java2DFrameConverter();
			while (true) {
				Frame frame = ocg.grabImage();
				if (frame == null) {
					break;
				}
				BufferedImage bufferedImage = converter.getBufferedImage(frame);
				if (bufferedImage == null) {
					break;
				}

				++frameNum;
				outFile = new File(framesDir + File.pathSeparator + frameNum + ".jpg");
				fos = new FileOutputStream(outFile);
				ImageIO.write(bufferedImage, "jpg", fos);
			}

			ocg.stop();
		} catch (Exception e) {
			logger.error("Failed convert video to images!", e);

			close(fos);
		} finally {
			close(fos);
		}
		return frameNum;
	}

	public static String mkFrameDir(File vedioFile) {
		String dirName = vedioFile.getAbsolutePath().substring(0, vedioFile.getAbsolutePath().lastIndexOf(".mp4"));
		System.out.println(dirName);
		File frameDir = new File(dirName);
		frameDir.mkdir();
		return dirName;
	}

	private void close(Closeable obj) {
		if (obj != null) {
			try {
				obj.close();
			} catch (IOException localIOException) {
			}
		}
	}
	
	public static void main(String[] args) {
		File video = new File("C:\\Users\\zhi.liu\\Desktop\\Lottery\\vedio\\60_15paNjlOWFoOWe_iDE4z55cXYsXvwZ_f35TsIZ8nykwWuJJG75PEscENl2TMP4565491582740427244.mp4");
		mkFrameDir(video);
	}

}
