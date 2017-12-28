package com.sftc.web.dao.mybatis;

import com.sftc.web.model.entity.SystemLabel;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SystemLabelMapper {

	List<SystemLabel> getSystemLabelList(SystemLabel systemLabel);

}
