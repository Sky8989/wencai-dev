package com.sftc.web.mapper;

import com.sftc.web.model.Order;
import com.sftc.web.model.Token;

public interface TokenMapper {
    void addToken(Token token);
    Token getToken(int order_id);
    Token getTokenByMobile(String mobile);
}
