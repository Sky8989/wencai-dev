package com.sftc.web.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIStatus;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.dao.jpa.SystemLabelDao;
import com.sftc.web.dao.mybatis.SystemLabelMapper;
import com.sftc.web.model.entity.SystemLabel;
import com.sftc.web.model.vo.swaggerRequestVO.systemLabel.SystemLabelVo;
import com.sftc.web.service.SystemLabelService;

@Service("systemLabelService")
public class SystemLabelServiceImpl implements SystemLabelService {

	@Resource
	SystemLabelDao systemLabelDao;

	@Resource
	SystemLabelMapper systemLabelMapper;


	/**
	 * 修改系统标签 
	 */
	@Override
	public APIResponse updateSystemLabel(SystemLabel systemLabel) {
		if (systemLabel != null) {
			systemLabel.setUpdate_time(Long.toString(System.currentTimeMillis()));
		if (systemLabelMapper.updateSystemLabel(systemLabel) > 0) {
			return APIUtil.getResponse(APIStatus.SUCCESS, systemLabel);
		} else {
			return APIUtil.getResponse(APIStatus.PARAM_ERROR, "修改系统标签失败 id不存在 = " + systemLabel.getId());
		}
		}
		return APIUtil.getResponse(APIStatus.PARAM_ERROR, "修改系统标签对象为空");
	}

	/**
	 * 新增系统标签
	 */
	@Override
	public APIResponse addSystemLabel(SystemLabel systemLabel) {
		if(systemLabel != null){
			systemLabel.setCreate_time(Long.toString(System.currentTimeMillis()));
			systemLabel.setUpdate_time(Long.toString(System.currentTimeMillis()));
			systemLabelDao.save(systemLabel);
			return APIUtil.getResponse(APIStatus.SUCCESS, systemLabel);
		}
			return APIUtil.getResponse(APIStatus.PARAM_ERROR,"对象为空");
	}

	/**
	 * 删除系统标签
	 */
	@Override
	public APIResponse deleteSystemLable(int id) {
		 if(systemLabelMapper.deleteSystemLable(id) > 0){
	    	   return APIUtil.getResponse(APIStatus.SUCCESS, id);
	       }else{
	    	   return APIUtil.getResponse(APIStatus.PARAM_ERROR, "删除失败，不存在id="+id);
	       }
	}

	/**
	 * 查询系统List并分页
	 */
	@Override
	public APIResponse getSystemLabelList(SystemLabelVo systemLabelVo) {
		if(systemLabelVo != null){
		SystemLabel systemLabel = new SystemLabel();
		int pageNumKey = systemLabelVo.getPageNumKey();
		int pageSizeKey = systemLabelVo.getPageSizeKey();
		systemLabel.setId(systemLabelVo.getId());
		systemLabel.setSystem_label(systemLabelVo.getSystem_label());
		
		//  使用lambab表达式 配合pageHelper实现对用户列表和查询相关信息的统一查询
		PageInfo<Object> pageInfo = PageHelper.startPage(pageNumKey, pageSizeKey).doSelectPageInfo(() -> systemLabelMapper.getSystemLabelList(systemLabel));
		  return APIUtil.getResponse(APIStatus.SUCCESS, pageInfo);
		}
		
		return APIUtil.getResponse(APIStatus.PARAM_ERROR, "查询条件为null");
	}

}
