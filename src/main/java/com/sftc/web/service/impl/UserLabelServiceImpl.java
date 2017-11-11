package com.sftc.web.service.impl;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.dao.mybatis.UserLabelMapper;
import com.sftc.web.dao.redis.UserLabelsRedis;
import com.sftc.web.model.SwaggerRequestVO.UpdateUsrLabelVO;
import com.sftc.web.model.SwaggerRequestVO.UserLabelVO;
import com.sftc.web.model.chen.Label;
import com.sftc.web.model.entity.SystemLabel;
import com.sftc.web.service.UserLabelService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

import static com.sftc.tools.api.APIStatus.*;


@Service
public class UserLabelServiceImpl implements UserLabelService {

    private static final String CUT_CHAT = "\\|";


    @Resource
    private UserLabelMapper userLabelMapper;
    @Resource
    private UserLabelsRedis userLabelsRedis;

    /**
     * 根据用户id获取用户所有标签
     */
    @Override
    public APIResponse getUserAllLabelByUCID(APIRequest apiRequest) {
        UserLabelVO userLabelVO = (UserLabelVO)apiRequest.getRequestParam();
        if (userLabelVO ==null){
            return APIUtil.paramErrorResponse(PARAM_ERROR.getMessage());
        }
        int user_contact_id = userLabelVO.getUser_contact_id();
        JSONArray userLabelsFromRedis = userLabelsRedis.getUserLabelsFromRedis();
//        JSONArray userLabelsFromRedis = null;
        if (userLabelsFromRedis==null||userLabelsFromRedis.size()<1){
            List<SystemLabel> system_labels =  userLabelMapper.querySystemLable();
            userLabelsFromRedis = JSONArray.fromObject(system_labels);
        }
        Label label = userLabelMapper.queryUserAlllabelByUID(user_contact_id);
        JSONObject json = new JSONObject();

        setJSON(userLabelsFromRedis, label, json);
        return APIUtil.getResponse(SUCCESS, json);
    }

    private void setJSON(JSONArray userLabelsFromRedis, Label label, JSONObject json) {
        if (label!=null){
            String str = label.getSystem_label_ids();
            if (!StringUtils.isBlank(str)){
                String[] split = str.split(CUT_CHAT);
                JSONArray jsonArray = new JSONArray();
                Arrays.asList(userLabelsFromRedis.toArray()).forEach(sysLabel ->{
                    JSONObject sys_json = (JSONObject)sysLabel;
                    String id = sys_json.getString("id");
                    Integer integer = Integer.valueOf(id);
                    String system_label = sys_json.getString("system_label");
                    JSONObject jsonObject = new JSONObject();
                    if (Arrays.asList(split).contains(id) ){
                        jsonObject.put("is_selected",true);
                    }else{
                        jsonObject.put("is_selected",false);
                    }
                    jsonObject.put("id",integer);
                    jsonObject.put("system_label",system_label);
                    jsonArray.add(jsonObject);
                });
                json.put("system_labels",jsonArray);
            }else{
                json.put("system_labels","");
            }

            String custom_labels = label.getCustom_labels();
            JSONArray custom_json;
            if (!StringUtils.isBlank(custom_labels)){
                custom_json = JSONArray.fromObject(custom_labels);
                json.put("custom_labels",custom_json);
            }else
                json.put("custom_labels","");
            json.put("label_id",label.getId());
        }else {
            json.put("label_id","");
            json.put("system_labels","");
            json.put("custom_labels","");
        }
    }



    /**
     * 根据标签id修改个人标签
     */
    @Override
    @Transactional
    public APIResponse updateUsrLabelByLID(APIRequest apiRequest) {
        UpdateUsrLabelVO updateUsrLabelVO = (UpdateUsrLabelVO)apiRequest.getRequestParam();
        if (updateUsrLabelVO ==null){
            return APIUtil.paramErrorResponse(PARAM_ERROR.getMessage());
        }
        String system_labels = updateUsrLabelVO.getSystem_labels();
        StringBuilder stf = new StringBuilder();
        if(!StringUtils.isBlank(system_labels)){
            JSONArray jsonArray = JSONArray.fromObject(system_labels);
            for (int i=0;i<jsonArray.size();i++){
                stf.append(jsonArray.get(i));
                if (i!=jsonArray.size()-1){
                    stf.append("|");
                }
            }
            updateUsrLabelVO.setSystem_labels(stf.toString());
        }
        int type = updateUsrLabelVO.getType();
        int label_id = updateUsrLabelVO.getLabel_id();

        if (type==0){
            userLabelMapper.insertLabelByid(label_id,updateUsrLabelVO.getSystem_labels(),updateUsrLabelVO.getCustom_labels());
        }else {
            int  result;
            if(StringUtils.isBlank(stf.toString()))
                 result = userLabelMapper.updateLabelByID(label_id,null,updateUsrLabelVO.getCustom_labels());
            else
                result = userLabelMapper.updateLabelByID(label_id,stf.toString(),updateUsrLabelVO.getCustom_labels());

            if (result<0)
                return APIUtil.getResponse(PARAM_ERROR, "修改标签错误");
        }
        return APIUtil.getResponse(SUCCESS,"");
    }

    /**
     * 根据用户好友关系id获取用户系统以及自定义标签
     */
    @Override
    public APIResponse getUserLabelDetailsByUCID(APIRequest apiRequest) {
        UserLabelVO userLabelVO = (UserLabelVO)apiRequest.getRequestParam();
        if (userLabelVO ==null){
            return APIUtil.paramErrorResponse(PARAM_ERROR.getMessage());
        }
        int user_contact_id = userLabelVO.getUser_contact_id();
        JSONArray userLabelsFromRedis = userLabelsRedis.getUserLabelsFromRedis();
//        JSONArray userLabelsFromRedis = null;
        if (userLabelsFromRedis==null||userLabelsFromRedis.size()<1){
            List<SystemLabel> system_labels =  userLabelMapper.querySystemLable();
            userLabelsFromRedis = JSONArray.fromObject(system_labels);
        }
        JSONObject json = new JSONObject();
        //装载系统标签
//        json.put("sysLabels",userLabelsFromRedis);

        Label label = userLabelMapper.queryUserAlllabelByUID(user_contact_id);


        //装载用户使用的系统标签，以及自定义标签
        setJSON(userLabelsFromRedis, label, json);

        return APIUtil.getResponse(SUCCESS, json);
    }

}
