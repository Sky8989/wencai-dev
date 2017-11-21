package com.sftc.web.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.dao.mybatis.UserLabelMapper;
import com.sftc.web.dao.redis.UserLabelsRedisDao;
import com.sftc.web.model.vo.swaggerRequest.UpdateUserContactLabelVO;
import com.sftc.web.model.vo.swaggerRequest.UserLabelVO;
import com.sftc.web.model.dto.SystemLabelDTO;
import com.sftc.web.model.entity.Label;
import com.sftc.web.model.dto.LabelDTO;
import com.sftc.web.model.entity.SystemLabel;
import com.sftc.web.service.UserLabelService;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.sftc.tools.api.APIStatus.PARAM_ERROR;
import static com.sftc.tools.api.APIStatus.SUCCESS;


@Service
public class UserLabelServiceImpl implements UserLabelService {

    private static final String CUT_CHAT = "\\|";

    @Resource
    private UserLabelMapper userLabelMapper;
    @Resource
    private UserLabelsRedisDao userLabelsRedisDao;
    /**
     * 根据用户id获取用户所有标签
     */
    public APIResponse getUserContactLabels(APIRequest apiRequest) {

        UserLabelVO userLabelVO = (UserLabelVO) apiRequest.getRequestParam();
        if (userLabelVO == null)
            return APIUtil.paramErrorResponse(PARAM_ERROR.getMessage());
        int user_contact_id = userLabelVO.getUser_contact_id();
        if (user_contact_id <= 0) return APIUtil.paramErrorResponse("参数无效");

        JSONObject responseObject = userLabelsRedisDao.getUserContactLabels(user_contact_id);
        responseObject = responseObject == null ? getUserContactLabelsByUCID(user_contact_id) : responseObject;

        return APIUtil.getResponse(SUCCESS, responseObject);
    }

    /**
     * 根据标签id修改个人标签
     */
    public APIResponse updateUserContactLabels(APIRequest apiRequest) {

        UpdateUserContactLabelVO updateUserContactLabelVO = (UpdateUserContactLabelVO) apiRequest.getRequestParam();
        if (updateUserContactLabelVO == null) {
            return APIUtil.paramErrorResponse(PARAM_ERROR.getMessage());
        }
        int user_contact_id = updateUserContactLabelVO.getUser_contact_id();
        if (user_contact_id <= 0) return APIUtil.paramErrorResponse("Parameter `user_contact_id` missing.");

        List<Integer> systemLabels = updateUserContactLabelVO.getSystem_labels();

        List<SystemLabel> systemLabelsRedis = userLabelsRedisDao.getSystemLabels();
        if (systemLabelsRedis == null) {
            systemLabelsRedis = userLabelMapper.querySystemLabels();
            if (systemLabelsRedis != null) {
                userLabelsRedisDao.setSystemLabelsCache(systemLabelsRedis);
            }
        }

        List<Integer>  lists = new ArrayList<>();
        if (systemLabelsRedis!=null){
            for(SystemLabel systemLabel:systemLabelsRedis){
                lists.add(systemLabel.getId());
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < systemLabels.size(); i++) {
            if (lists.contains(systemLabels.get(i))){
                sb.append(systemLabels.get(i));
                if (i != systemLabels.size() - 1) {
                    sb.append("|");
                }
            }
        }

        String system_labels = sb.toString();
        String custom_labels = new Gson().toJson(updateUserContactLabelVO.getCustom_labels());

        Label label = userLabelMapper.queryUserAllLabelByUID(user_contact_id);
        if (label != null) {
            userLabelMapper.updateLabelByUCID(user_contact_id, system_labels, custom_labels);
            userLabelsRedisDao.removeUserContactLabelsCache(user_contact_id);
        } else {
            userLabelMapper.insertLabelByUCID(user_contact_id, system_labels, custom_labels);
        }

        JSONObject responseObject = getUserContactLabelsByUCID(user_contact_id);
        return APIUtil.getResponse(SUCCESS, responseObject);
    }

    // 根据好友联系人ID查好友标签
    @Transactional
    private JSONObject getUserContactLabelsByUCID(int user_contact_id) {

        Label label = userLabelMapper.queryUserAllLabelByUID(user_contact_id);

        // 选中标签
        List<String> selectedLabels = new ArrayList<>();
        // 系统标签
        List<LabelDTO> sysLabels = new ArrayList<>();
        List<SystemLabel> systemLabels = userLabelsRedisDao.getSystemLabels();
        if (systemLabels == null) {
            systemLabels = userLabelMapper.querySystemLabels();
            if (systemLabels != null) {
                userLabelsRedisDao.setSystemLabelsCache(systemLabels);
            }
        }
        if (systemLabels != null) {
            List<String> system_selected_ids = label == null ? new ArrayList<>() : Arrays.asList(label.getSystem_label_ids().split(CUT_CHAT));
            for (SystemLabel systemLabel : systemLabels) {
                // entity to dto.
                SystemLabelDTO tmpLabel = new SystemLabelDTO();
                tmpLabel.setId(systemLabel.getId());
                tmpLabel.setName(systemLabel.getSystem_label());
                boolean isSeleted = system_selected_ids.contains(systemLabel.getId() + "");
                tmpLabel.setSelected(isSeleted);
                if (isSeleted)
                    selectedLabels.add(systemLabel.getSystem_label());
                sysLabels.add(tmpLabel);
            }
        }
        // 自定义标签
        List<LabelDTO> customLabels = label == null ? new ArrayList<>() : new Gson().fromJson(label.getCustom_labels(), new TypeToken<List<LabelDTO>>() {
        }.getType());
        for (LabelDTO tmpLabel : customLabels) {
            if (tmpLabel.isSelected()) {
                selectedLabels.add(tmpLabel.getName());
            }
        }

        JSONObject responseObject = new JSONObject();
        responseObject.put("user_contact_id", user_contact_id);
        responseObject.put("selected_labels", selectedLabels);
        responseObject.put("system_labels", sysLabels);
        responseObject.put("custom_labels", customLabels);
        userLabelsRedisDao.setUserContactLabelsCache(user_contact_id, responseObject);

        return responseObject;
    }
}
