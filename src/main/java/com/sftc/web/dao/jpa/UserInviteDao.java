package com.sftc.web.dao.jpa;


import com.sftc.web.model.entity.UserInvite;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserInviteDao extends PagingAndSortingRepository<UserInvite, Integer>, JpaSpecificationExecutor<UserInvite> {

}
