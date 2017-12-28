package com.sftc.web.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sftc.tools.api.ApiRequest;
import com.sftc.tools.api.ApiResponse;
import com.sftc.tools.api.ApiStatus;
import com.sftc.tools.api.ApiUtil;
import com.sftc.web.dao.mybatis.SystemLabelMapper;
import com.sftc.web.model.entity.SystemLabel;
import com.sftc.web.model.vo.swaggerRequest.SystemLabelVo;
import com.sftc.web.service.SystemLabelService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service("systemLabelService")
public class SystemLabelServiceImpl implements SystemLabelService {

    @Resource
    SystemLabelMapper systemLabelMapper;

    /**
     * 查询系统List并分页
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse getSystemLabelList(ApiRequest apiRequest) {
        SystemLabelVo systemLabelVo = (SystemLabelVo) apiRequest.getRequestParam();
        if (systemLabelVo != null) {
            SystemLabel systemLabel = new SystemLabel();
            int pageNumKey = systemLabelVo.getPageNumKey();
            int pageSizeKey = systemLabelVo.getPageSizeKey();
            systemLabel.setId(systemLabelVo.getId());
            systemLabel.setSystem_label(systemLabelVo.getSystem_label());

            // 使用lambab表达式 配合pageHelper实现对用户列表和查询相关信息的统一查询
            PageInfo<Object> pageInfo = PageHelper.startPage(pageNumKey, pageSizeKey)
                    .doSelectPageInfo(() -> systemLabelMapper.getSystemLabelList(systemLabel));
            return ApiUtil.getResponse(ApiStatus.SUCCESS, pageInfo);
        }

        return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "查询条件为null");
    }

}
