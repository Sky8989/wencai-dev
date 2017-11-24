package com.sftc.web.service;

import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.entity.SystemLabel;
import com.sftc.web.model.vo.swaggerRequest.DeleteSystemLableVO;
import com.sftc.web.model.vo.swaggerRequestVO.systemLabel.SystemLabelVo;

public interface SystemLabelService {

	APIResponse getSystemLabelList(SystemLabelVo systemLabel);

	APIResponse updateSystemLabel(SystemLabel systemLabel);

	APIResponse addSystemLabel(SystemLabel systemLabel);

	APIResponse deleteSystemLable(DeleteSystemLableVO deleteSystemLableVO);

	APIResponse save(SystemLabel systemLabel);
}
