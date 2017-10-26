package com.sftc.web.dao.jpa;


import com.sftc.web.model.UserInvite;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserInviteDao extends PagingAndSortingRepository<UserInvite, Long>, JpaSpecificationExecutor<UserInvite> {

}
