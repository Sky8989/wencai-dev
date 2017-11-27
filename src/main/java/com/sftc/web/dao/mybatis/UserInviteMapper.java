package com.sftc.web.dao.mybatis;

import com.sftc.web.model.entity.UserInvite;
import org.springframework.stereotype.Repository;
@Repository
public interface UserInviteMapper {

    Integer save(UserInvite userInvite);
}
