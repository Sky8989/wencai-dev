package com.sftc.web.model.vo.swaggerOrderVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "订单支付包装类")
public class OrderPayVO {
    @ApiModelProperty(name = "local_token",value = "蛋壳local_token",example = "ffa1d5557565e283caba67",required = true)
    private String local_token;
    @ApiModelProperty(name = "uuid",value = "快递uuid",example = "2c9a85895f24480d015f299b99c4588a",required = true)
    private String uuid;
    @ApiModelProperty(name = "access_token",value = "顺丰access_token",example = "EyMivbd44I124lcddrBG",required = true)
    private String access_token;

    public String getLocal_token() {return local_token;}

    public void setLocal_token(String local_token) {this.local_token = local_token;}

    public String getUuid() {return uuid;}

    public void setUuid(String uuid) {this.uuid = uuid;}

    public String getAccess_token() {return access_token;}

    public void setAccess_token(String access_token) {this.access_token = access_token;}
}
