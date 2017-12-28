package com.sftc.web.service;

import com.sftc.tools.api.ApiResponse;
import com.sftc.web.model.vo.swaggerRequest.DeleteTokenVO;

/**
 * Author:hxy starmoon1994
 * Description: token机制的总入口
 * Date:10:28 2017/8/8
 */
public interface TokenService {

	ApiResponse deleteToken(DeleteTokenVO body) throws Exception;
}
