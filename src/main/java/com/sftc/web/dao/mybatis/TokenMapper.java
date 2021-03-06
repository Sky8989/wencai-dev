package com.sftc.web.dao.mybatis;

import com.sftc.web.model.entity.Token;
import com.sftc.web.model.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenMapper {
    void addToken(Token token);

    void updateToken(Token token);

    Token getToken(int order_id);

    Token getTokenById(int user_id);

    Token getTokenByMobile(String mobile);

    Token selectUserIdByToken(@Param("token") String token);

    List<Token> getTokenList(Token token);

    User tokenInterceptor(String token);

    Integer getUserIdByLocalToken(String token);
    
    void deleteTokenByUserId(Integer user_id);
}
