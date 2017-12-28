package com.sftc.web.dao.mybatis;

import com.sftc.web.model.entity.Token;
import com.sftc.web.model.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenMapper {
    void addToken(Token token);

    void updateToken(Token token);

    Token getToken(int order_id);

    Token getTokenByUUId(String user_uuid);

    User tokenInterceptor(String token);

    String getUserIdByLocalToken(String token);
    
    void deleteTokenByUserId(String user_uuid);
}
