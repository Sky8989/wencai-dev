package com.sftc.web.dao.mybatis;

import com.sftc.web.model.Token;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TokenMapper {
    void addToken(Token token);

    void updateToken(Token token);

    Token getToken(int order_id);

    Token getTokenById(int user_id);

    Token getTokenByMobile(String mobile);

    Token selectUserIdByToken(@Param("token") String token);

    List<Token> getTokenList(Token token);
}
