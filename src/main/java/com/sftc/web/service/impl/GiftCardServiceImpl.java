package com.sftc.web.service.impl;

import static com.sftc.tools.api.APIStatus.SUCCESS;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIStatus;
import com.sftc.tools.api.APIUtil;
import com.sftc.tools.sf.SFOrderHelper;
import com.sftc.web.dao.jpa.GiftCardDao;
import com.sftc.web.dao.mybatis.GiftCardMapper;
import com.sftc.web.model.entity.GiftCard;
import com.sftc.web.model.vo.displayMessage.GiftCardListVO;
import com.sftc.web.service.GiftCardService;
import com.sftc.web.service.QiniuService;

@Service
public class GiftCardServiceImpl implements GiftCardService {
	@Resource
	private GiftCardMapper giftCardMapper;
	@Resource
	private QiniuService qiniuService;
	@Resource
	private QiniuServiceImpl qiniuServiceImpl;
	@Resource
	private GiftCardDao giftCardDao;

	public APIResponse getGiftCardList(APIRequest request) {

		List<GiftCard> giftCards = giftCardMapper.giftCardList();
		List<GiftCardListVO> giftCardLists = new ArrayList<GiftCardListVO>();

		for (GiftCard giftCard : giftCards) {
			// 遍历当前的giftCardLists数组，判断这个type的GiftCardList是否已经存在，如果存在，取出这个type的giftCardList
			GiftCardListVO giftCardList = null;
			for (GiftCardListVO tempList : giftCardLists) {
				if (tempList.getType().equals(giftCard.getType())) {
					giftCardList = tempList;
					break;
				}
			}

			if (giftCardList != null) { // 已存在，GiftCardList对象的giftCards数组添加giftCard
				List<GiftCard> gitCards = giftCardList.getGiftCards();
				gitCards.add(giftCard);
			} else { // 不存在，新建一个GiftCardList，进行赋值，再add进giftCardLists数组
				GiftCardListVO list = new GiftCardListVO();
				List<GiftCard> gitCards = new ArrayList<GiftCard>();
				gitCards.add(giftCard);
				list.setType(giftCard.getType());
				list.setGiftCards(gitCards);
				giftCardLists.add(list);
			}
		}

		return APIUtil.getResponse(SUCCESS, giftCardLists);
	}

	/**
	 * CMS 系统 获取礼品卡列表 条件查询+分页
	 */
	public APIResponse selectList(APIRequest apiRequest) throws Exception {

		// 此处封装了 User的构造方法
		HttpServletRequest httpServletRequest = apiRequest.getRequest();
		GiftCard giftCard = new GiftCard(httpServletRequest);
		int pageNumKey = Integer.parseInt(httpServletRequest.getParameter("pageNumKey"));
		int pageSizeKey = Integer.parseInt(httpServletRequest.getParameter("pageSizeKey"));
		PageHelper.startPage(pageNumKey, pageSizeKey);
		List<GiftCard> giftCardList = giftCardMapper.selectByPage(giftCard);
		// 使用lambab表达式 配合pageHelper实现对用户列表和查询相关信息的统一查询
		PageInfo<Object> pageInfo = PageHelper.startPage(pageNumKey, pageSizeKey)
				.doSelectPageInfo(() -> giftCardMapper.selectByPage(giftCard));
		// 处理结果
		if (pageInfo.getList().size() == 0) {
			return APIUtil.selectErrorResponse("搜索到的结果数为0，请检查查询条件", null);
		} else {
			return APIUtil.getResponse(SUCCESS, pageInfo);
		}
	}

	/**
	 * CMS 系统 添加礼品卡信息
	 */
	public APIResponse addGiftCard(GiftCard giftCard) throws Exception {
		giftCard.setCreate_time(Long.toString(System.currentTimeMillis()));
		giftCardMapper.insertGiftCard(giftCard);
		return APIUtil.getResponse(SUCCESS, giftCard);
	}

	/**
	 * CMS 系统 修改礼品卡信息
	 */
	public APIResponse updateGiftCard(GiftCard giftCard) throws Exception {
		if(giftCardMapper.updateGiftCard(giftCard) > 0 )
		return APIUtil.getResponse(SUCCESS, giftCard);
		
		return APIUtil.getResponse(APIStatus.PARAM_ERROR, "修改失败，不存在id=" + giftCard.getId());
	}

	/**
	 * CMS 系统 删除礼品卡信息
	 */
	public APIResponse deleteGiftCard(int id) throws Exception {
		if(giftCardMapper.deleteGiftCard(id) > 0)
		return APIUtil.getResponse(SUCCESS, id);
		
		return APIUtil.getResponse(APIStatus.PARAM_ERROR, "删除失败，不存在id="+id);
	}
	

	/**
	 * 礼品卡保存操作， 当礼品卡id为0时 为新增礼品卡操作  礼品卡id 非0为修改操作
	 */
	@Override
	public APIResponse save(GiftCard giftCard) throws Exception {
		if (giftCard == null) {
			return APIUtil.getResponse(APIStatus.PARAM_ERROR, "save失败，传入对象为 =" + giftCard);
		}
		if (StringUtil.isNotEmpty(giftCard.getIcon())) {
			// 将已base64格式的图片路径 上传到七牛
			String imgPath = uploadImageWithBase64(giftCard.getIcon());

			if (imgPath == null)
				return APIUtil.getResponse(APIStatus.PARAM_ERROR, "上传图片失败，传入对象为 =" + giftCard);

			giftCard.setIcon(imgPath);
			
			if (giftCard.getId() != 0){
				return updateGiftCard(giftCard);
			}
			giftCard.setCreate_time(Long.toString(System.currentTimeMillis()));
			giftCardDao.save(giftCard);
			return APIUtil.getResponse(SUCCESS, giftCard);
		}
		return APIUtil.getResponse(APIStatus.PARAM_ERROR, "save失败，传入对象为 =" + giftCard);
	}

	/**
	 * 上传图片已base64的形式上传
	 * 
	 * @param base64Img
	 *            图片路径转换成base64格式的字符串
	 * @return
	 */
	public String uploadImageWithBase64(String base64Img) {
		try {
			return qiniuServiceImpl.putImage(base64Img, "giftCard/" + System.currentTimeMillis() + ".png");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
