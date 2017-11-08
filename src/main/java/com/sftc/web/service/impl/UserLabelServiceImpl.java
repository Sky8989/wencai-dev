package com.sftc.web.service.impl;

import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.dao.mybatis.UserLabelMapper;
import com.sftc.web.dao.redis.UserLabelsRedis;
import com.sftc.web.model.chen.Label;
import com.sftc.web.model.entity.LabelDetailsInfo;
import com.sftc.web.model.entity.LabelInfo;
import com.sftc.web.service.UserLabelService;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.sftc.tools.api.APIStatus.PARAM_ERROR;
import static com.sftc.tools.api.APIStatus.SUBMIT_FAIL;
import static com.sftc.tools.api.APIStatus.SUCCESS;

/**
 * Created by CatalpaFlat on 2017/11/6.
 */
@Service
public class UserLabelServiceImpl implements UserLabelService {

    private static final int SYSTEM_LABELID = -1;
    private static final String CUT_CHAT = "\\|";

//    @Autowired
//    private UserLabelsRedis userLabelsRedis;

    @Autowired
    private UserLabelMapper userLabelMapper;
    @Autowired
    private LabelInfo labelInfo;
    @Autowired
    private LabelDetailsInfo labelDetailsInfo;

    /**
     * 根据用户id获取用户所有标签
     * @param user_contact_id
     * @return
     */
    @Override
    public APIResponse getUserAllLabelByUCID(int user_contact_id) {
        if (user_contact_id<1){
            return APIUtil.getResponse(PARAM_ERROR, "用户关系id不能小于1");
        }
        Label label = userLabelMapper.queryUserAlllabelByUID(user_contact_id);
        if (label!=null){
            String str = label.getLabel();
            if (!StringUtils.isBlank(str)){
                String[] split = str.split(CUT_CHAT);
                labelInfo.setLabels(split);
            }else{
                labelInfo.setLabels(new String[0]);
            }

            labelInfo.setLabel_id(label.getId());
        }else {
            labelInfo.setLabel_id(null);
            labelInfo.setLabels(null);
        }
        return APIUtil.getResponse(SUCCESS, labelInfo);
    }

    /**
     * 根据标签id修改个人标签
     * @param label_id
     * @param labels
     * @param type
     * @return
     */
    @Override
    @Transactional
    public APIResponse updateUsrLabelByLID(int label_id, String labels, int type) {

        StringBuffer stf = new StringBuffer();

        if (!StringUtils.isBlank(labels)){
            JSONArray jsonArray = JSONArray.fromObject(labels);
            for (int i=0;i<jsonArray.size();i++){
                stf.append(jsonArray.get(i));
                if (i!=jsonArray.size()-1){
                    stf.append("|");
                }
            }
        }else {
            stf = null;
        }
        if (type==0){
            userLabelMapper.insertLabelByid(label_id,stf.toString());
        }else {
            int  result = -1;
            if(StringUtils.isBlank(stf.toString()))
                 result = userLabelMapper.updateLabelByID(label_id,null);
            else
                result = userLabelMapper.updateLabelByID(label_id,stf.toString());

            if (result<0)
                return APIUtil.getResponse(SUBMIT_FAIL, "修改标签错误");
        }
        return APIUtil.getResponse(SUCCESS,"");
    }

    /**
     * 根据用户好友关系id获取用户系统以及自定义标签
     * @param user_contact_id
     * @return
     */
    @Override
    public APIResponse getUserLabelDetailsByUCID(int user_contact_id) {
        if (user_contact_id<1){
            return APIUtil.getResponse(PARAM_ERROR, "用户关系id不能小于1");
        }
        Label label = userLabelMapper.queryUserAlllabelByUID(user_contact_id);
//        String[] userLabelsFromRedis = userLabelsRedis.getUserLabelsFromRedis();
        String[] userLabelsFromRedis = null;
        Label sysLabel = null;
        if (userLabelsFromRedis==null||userLabelsFromRedis.length==0){
             sysLabel = userLabelMapper.queryUserAlllabelByUID(SYSTEM_LABELID);
        }
        //装载系统标签
        if (sysLabel!=null){
            labelDetailsInfo.setSysLabels(sysLabel.getLabel().split(CUT_CHAT));
        }else {
            labelDetailsInfo.setSysLabels(userLabelsFromRedis);
        }
        //装载用户使用的系统标签，以及自定义标签
        List<String> user_SysLists = new ArrayList<>();
        List<String> usrLists = new ArrayList<>();
        if (label!=null){
            String[] sysLabels = userLabelsFromRedis;
            if (sysLabel!=null){
                if (!StringUtils.isBlank(sysLabel.getLabel())){
                    sysLabels = sysLabel.getLabel().split(CUT_CHAT);
                }
            }
            String userlabel1 = label.getLabel();
            if (!StringUtils.isBlank(userlabel1)){
                String[] finalSysLabels = sysLabels;
                Arrays.asList(userlabel1.split(CUT_CHAT)).forEach(str ->{
                    //二分搜索法来搜索指定数组
                    if (Arrays.binarySearch(finalSysLabels,str) >= 0){
                        user_SysLists.add(str);
                    }else {
                        usrLists.add(str);
                    }
                });
            }

        }
        labelDetailsInfo.setUserLabels(usrLists);
        labelDetailsInfo.setUserSyslabels(user_SysLists);
        return APIUtil.getResponse(SUCCESS, labelDetailsInfo);
    }
}
