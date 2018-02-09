package com.sftc.web.dao.mybatis;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sftc.web.model.entity.SystemLabel;


@Repository
public interface SystemLabelMapper {

	int deleteSystemLable(int id);

	int updateSystemLabel(SystemLabel systemLabel);

	List<SystemLabel> getSystemLabelList(SystemLabel systemLabel);


}
