package com.sftc.web.service;

import com.sftc.tools.api.APIResponse;

public interface MessageService {
    APIResponse getMessage(Object object);
    APIResponse register(Object object);
    APIResponse getToken(Object object);
    APIResponse login(Object object);
}
