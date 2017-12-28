package com.sftc.web.model.vo.swaggerRequest;

import com.sftc.web.model.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xf on 2017/10/23.
 */
@ApiModel(value = "好友标星请求包装类")
public class FriendStarVO extends BaseVO {
    @Getter
    @Setter
    @ApiModelProperty(name = "user_uuid",value = "用户uuid",example = "10093",required = true,dataType="String",hidden = true)
    private String user_uuid;
    @Getter
    @Setter
    @ApiModelProperty(name = "friend_uuid",value = "好友uuid",example = "2c9a85895fddad58015fdde9f78d005a",dataType="String",required = true)
    private String friend_uuid;
    @Getter
    @Setter
    @ApiModelProperty(name = "is_star",value = "标星",example = "1",dataType = "int" ,required = true)
    private Integer is_star;
}
