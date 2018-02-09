package com.sftc.web.model.vo.swaggerRequest;


import com.sftc.web.model.dto.LabelDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@ApiModel(value = "增加用户标签存储")
public class AddUserContactLabelVO {

	@ApiModelProperty(value = "主键",hidden=true) 
    @Getter @Setter
    private int id;
	
    @ApiModelProperty(value = "用户好友关系id", example = "156", required = true)
    @Getter @Setter
    private int user_contact_id;

    @ApiModelProperty(name = "system_labels", value = "系统标签id数组", required = true)
    @Getter @Setter
    private List<Integer> system_labels;

    @ApiModelProperty(name = "custom_labels", value = "自定义标签", required = true)
    @Getter @Setter
    private List<LabelDTO> custom_labels;
    
    @ApiModelProperty(name = "创建时间",hidden=true)
    @Getter @Setter
    private String create_time;
    
    @ApiModelProperty(name = "修改时间",hidden=true)
    @Getter @Setter
    private String update_time;

   
}
