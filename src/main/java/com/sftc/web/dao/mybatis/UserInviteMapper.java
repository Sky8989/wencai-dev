package com.sftc.web.dao.mybatis;

import com.sftc.web.model.UserInvite;
import org.springframework.stereotype.Repository;
@Repository
public interface UserInviteMapper {

    Integer save(UserInvite userInvite);




}
