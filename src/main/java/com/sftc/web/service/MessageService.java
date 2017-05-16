package com.sftc.web.service;

import com.sftc.tools.api.APIResponse;
import org.codehaus.jackson.map.util.JSONPObject;

/**
 * Created by Administrator on 2017/5/15.
 */
public interface MessageService {
    APIResponse getMessage(Object object);
    APIResponse register(Object object);
    APIResponse getToken(Object object);
    APIResponse login(Object object);
}
