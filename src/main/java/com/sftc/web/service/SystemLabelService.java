package com.sftc.web.service;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.entity.SystemLabel;

public interface SystemLabelService {

	APIResponse getSystemLabelList(APIRequest apiRequest);

	APIResponse save(APIRequest apiRequest);

	APIResponse deleteSystemLable(APIRequest apiRequest);


	APIResponse addSystemLabel(SystemLabel systemLabel);

	APIResponse updateSystemLabel(SystemLabel systemLabel);

}
