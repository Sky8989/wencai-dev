package com.sftc.web.mapper;

import com.sftc.web.model.Token;
import org.apache.ibatis.annotations.Param;

public interface TokenMapper {
    void addToken(Token token);

    void updateToken(Token token);

    Token getToken(int order_id);

    Token getTokenById(int user_id);

    Token getTokenByMobile(String mobile);

    int selectUserIdByToken(@Param("token") String token);


}
