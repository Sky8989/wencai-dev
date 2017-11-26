package com.sftc.web.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIStatus;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.dao.jpa.SystemLabelDao;
import com.sftc.web.dao.mybatis.SystemLabelMapper;
import com.sftc.web.model.entity.SystemLabel;
import com.sftc.web.model.vo.swaggerRequest.DeleteSystemLabelVo;
import com.sftc.web.model.vo.swaggerRequest.SystemLabelVo;
import com.sftc.web.service.SystemLabelService;
@Transactional
@Service("systemLabelService")
public class SystemLabelServiceImpl implements SystemLabelService {

	@Resource
	SystemLabelMapper systemLabelMapper;

	/**
	 * 查询系统List并分页
	 */
	@Override
	public APIResponse getSystemLabelList(APIRequest apiRequest) {
		SystemLabelVo systemLabelVo  = (SystemLabelVo) apiRequest.getRequestParam();
		if (systemLabelVo != null) {
			SystemLabel systemLabel = new SystemLabel();
			int pageNumKey = systemLabelVo.getPageNumKey();
			int pageSizeKey = systemLabelVo.getPageSizeKey();
			systemLabel.setId(systemLabelVo.getId());
			systemLabel.setSystem_label(systemLabelVo.getSystem_label());

			// 使用lambab表达式 配合pageHelper实现对用户列表和查询相关信息的统一查询
			PageInfo<Object> pageInfo = PageHelper.startPage(pageNumKey, pageSizeKey)
					.doSelectPageInfo(() -> systemLabelMapper.getSystemLabelList(systemLabel));
			return APIUtil.getResponse(APIStatus.SUCCESS, pageInfo);
		}

		return APIUtil.getResponse(APIStatus.PARAM_ERROR, "查询条件为null");
	}
}
