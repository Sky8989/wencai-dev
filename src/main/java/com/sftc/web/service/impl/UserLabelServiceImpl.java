package com.sftc.web.service.impl;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.dao.mybatis.UserLabelMapper;
import com.sftc.web.dao.redis.UserLabelsRedis;
import com.sftc.web.model.SwaggerRequestVO.UpdateUsrLabelVO;
import com.sftc.web.model.SwaggerRequestVO.UserLabelVO;
import com.sftc.web.model.chen.Label;
import com.sftc.web.model.entity.LabelDetailsInfo;
import com.sftc.web.model.entity.SystemLabel;
import com.sftc.web.service.UserLabelService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.sftc.tools.api.APIStatus.*;

/**
 * Created by CatalpaFlat on 2017/11/6.
 */
@Service
public class UserLabelServiceImpl implements UserLabelService {

    private static final int SYSTEM_LABELID = -1;
    private static final String CUT_CHAT = "\\|";


    @Autowired
    private UserLabelMapper userLabelMapper;
    @Autowired
    private UserLabelsRedis userLabelsRedis;
    /**
     * 根据用户id获取用户所有标签
     * @param apiRequest
     */
    @Override
    public APIResponse getUserAllLabelByUCID(APIRequest apiRequest) {
        UserLabelVO userLabelVO = (UserLabelVO)apiRequest.getRequestParam();
        if (userLabelVO ==null){
            return APIUtil.paramErrorResponse(PARAM_ERROR.getMessage());
        }
        int user_contact_id = userLabelVO.getUser_contact_id();
        JSONArray userLabelsFromRedis = userLabelsRedis.getUserLabelsFromRedis();
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
                String[] split = str.split(CUT_CHAT);;
                Map<String,String> sys_map = new HashMap<>();
                Arrays.asList(userLabelsFromRedis.toArray()).forEach(sysLabel ->{
                    JSONObject sys_json = (JSONObject)sysLabel;
                    String id = sys_json.getString("id");
                    if (Arrays.binarySearch(split,id) >= 0 ){
                        String system_label = sys_json.getString("system_label");
                        sys_map.put(id,system_label);
                    }
                });
                json.put("system_labels",sys_map);
            }else{
                json.put("system_labels","");
            }

            String custom_labels = label.getCustom_labels();
            JSONObject custom_json = null;
            if (!StringUtils.isBlank(custom_labels)){
                custom_json = JSONObject.fromObject(custom_labels);
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
     * @param apiRequest
     */
    @Override
    @Transactional
    public APIResponse updateUsrLabelByLID(APIRequest apiRequest) {
        UpdateUsrLabelVO updateUsrLabelVO = (UpdateUsrLabelVO)apiRequest.getRequestParam();
        if (updateUsrLabelVO ==null){
            return APIUtil.paramErrorResponse(PARAM_ERROR.getMessage());
        }
        String system_labels = updateUsrLabelVO.getSystem_labels();
        if(StringUtils.isBlank(system_labels)){
            
        }

//        String labels = updateUsrLabelVO.getLabels();
//        int label_id = updateUsrLabelVO.getLabel_id();
//        int type = updateUsrLabelVO.getType();
//
//        StringBuffer stf = new StringBuffer();
//
//        if (!StringUtils.isBlank(labels)){
//            JSONArray jsonArray = JSONArray.fromObject(labels);
//            for (int i=0;i<jsonArray.size();i++){
//                stf.append(jsonArray.get(i));
//                if (i!=jsonArray.size()-1){
//                    stf.append("|");
//                }
//            }
//        }else {
//            stf = null;
//        }
//        if (type==0){
//            userLabelMapper.insertLabelByid(label_id,stf.toString());
//        }else {
//            int  result;
//            if(StringUtils.isBlank(stf.toString()))
//                 result = userLabelMapper.updateLabelByID(label_id,null);
//            else
//                result = userLabelMapper.updateLabelByID(label_id,stf.toString());
//
//            if (result<0)
//                return APIUtil.getResponse(PARAM_ERROR, "修改标签错误");
//        }
        return APIUtil.getResponse(SUCCESS,"");
    }

    /**
     * 根据用户好友关系id获取用户系统以及自定义标签
     * @param apiRequest
     * @return
     */
    @Override
    public APIResponse getUserLabelDetailsByUCID(APIRequest apiRequest) {
        UserLabelVO userLabelVO = (UserLabelVO)apiRequest.getRequestParam();
        if (userLabelVO ==null){
            return APIUtil.paramErrorResponse(PARAM_ERROR.getMessage());
        }
        int user_contact_id = userLabelVO.getUser_contact_id();
        JSONArray userLabelsFromRedis = userLabelsRedis.getUserLabelsFromRedis();
        if (userLabelsFromRedis==null||userLabelsFromRedis.size()<1){
            List<SystemLabel> system_labels =  userLabelMapper.querySystemLable();
            userLabelsFromRedis = JSONArray.fromObject(system_labels);
        }
        JSONObject json = new JSONObject();
        //装载系统标签
        json.put("sysLabels",userLabelsFromRedis);

        Label label = userLabelMapper.queryUserAlllabelByUID(user_contact_id);


        //装载用户使用的系统标签，以及自定义标签
        setJSON(userLabelsFromRedis, label, json);

        return APIUtil.getResponse(SUCCESS, json);
    }

    @Test
    public void test(){
//        List<SystemLabel> system_labels =  new ArrayList<>();
//        SystemLabel systemLabel1 = new SystemLabel();
//        systemLabel1.setId("1");
//        systemLabel1.setSystem_label("我是1");
//        system_labels.add(systemLabel1);
//        SystemLabel systemLabel2 = new SystemLabel();
//        systemLabel2.setId("2");
//        systemLabel2.setSystem_label("我是2");
//        system_labels.add(systemLabel2);
//        SystemLabel systemLabel3 = new SystemLabel();
//        systemLabel3.setId("3");
//        systemLabel3.setSystem_label("我是3");
//        system_labels.add(systemLabel3);
//        SystemLabel systemLabel4 = new SystemLabel();
//        systemLabel4.setId("4");
//        systemLabel4.setSystem_label("我是4");
//        system_labels.add(systemLabel4);
//
//        JSONArray userLabelsFromRedis = JSONArray.fromObject(system_labels);
////        Arrays.asList(userLabelsFromRedis.toArray()).forEach(sysLabel ->{
////                JSONObject sys_json = (JSONObject)sysLabel;
////            System.out.println(sys_json.getString("system_label"));
////         });
//        JSONObject json = new JSONObject();
//        String sys_ids = "1|2|3";
//        if (!StringUtils.isBlank(sys_ids)) {
//            String[] split = sys_ids.split(CUT_CHAT);
//            Map<String,String> sys_map = new HashMap<>();
//            Arrays.asList(userLabelsFromRedis.toArray()).forEach(sysLabel ->{
//                JSONObject sys_json = (JSONObject)sysLabel;
//                String id = sys_json.getString("id");
//                if (Arrays.binarySearch(split,id) >= 0 ){
//                    String system_label = sys_json.getString("system_label");
//                    sys_map.put(id,system_label);
//                    System.out.println("id:"+id+",system_label:"+system_label);
//                }
//            });
//            json.put("system_labels",sys_map);
//            System.out.println(json.toString());
//        }
    }
}
