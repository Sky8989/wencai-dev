package com.sftc.web.service;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.entity.SystemLabel;

public interface SystemLabelService {

	APIResponse getSystemLabelList(APIRequest apiRequest);
}
