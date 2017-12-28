package com.sftc.web.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sftc.tools.api.ApiResponse;
import com.sftc.tools.api.ApiUtil;
import com.sftc.web.dao.mybatis.UserLabelMapper;
import com.sftc.web.dao.redis.UserLabelsRedisDao;
import com.sftc.web.model.dto.LabelDTO;
import com.sftc.web.model.dto.SystemLabelDTO;
import com.sftc.web.model.entity.Label;
import com.sftc.web.model.entity.SystemLabel;
import com.sftc.web.model.vo.swaggerRequest.UpdateUserContactLabelVO;
import com.sftc.web.model.vo.swaggerRequest.UserLabelVO;
import com.sftc.web.service.UserLabelService;
import net.sf.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.sftc.tools.api.ApiStatus.SUCCESS;

/**
 * @author ： CatalpaFlat
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserLabelServiceImpl implements UserLabelService {

    private static final String CUT_CHAT = "\\|";

    @Resource
    private UserLabelMapper userLabelMapper;
    @Resource
    private UserLabelsRedisDao userLabelsRedisDao;

    /**
     * 根据用户id获取用户所有标签
     */
    @Override
    public ApiResponse getUserContactLabels(UserLabelVO userLabelVO) {
        if (userLabelVO == null) {
            return ApiUtil.error(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), "the request body is empty");
        }
        int userContactId = userLabelVO.getUser_contact_id();
        if (userContactId <= 0) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "Parameter `user_contact_id` missing.");
        }

        JSONObject responseObject = userLabelsRedisDao.getUserContactLabels(userContactId);
        responseObject = responseObject == null ? getUserContactLabelsByUCID(userContactId) : responseObject;

        return ApiUtil.getResponse(SUCCESS, responseObject);
    }

    /**
     * 根据标签id修改个人标签
     */
    @Override
    public ApiResponse updateUserContactLabels(UpdateUserContactLabelVO updateUserContactLabelVO) {
        if (updateUserContactLabelVO == null) {
            return ApiUtil.error(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), "the request body is empty");
        }
        int userContactId = updateUserContactLabelVO.getUser_contact_id();
        if (userContactId <= 0) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "Parameter `user_contact_id` missing.");
        }

        List<Integer> systemLabels = updateUserContactLabelVO.getSystem_labels();

        List<SystemLabel> systemLabelsRedis = userLabelsRedisDao.getSystemLabels();
        if (systemLabelsRedis == null) {
            systemLabelsRedis = userLabelMapper.querySystemLabels();
            if (systemLabelsRedis != null) {
                userLabelsRedisDao.setSystemLabelsCache(systemLabelsRedis);
            }
        }

        List<Integer> lists = new ArrayList<>();
        if (systemLabelsRedis != null) {
            for (SystemLabel systemLabel : systemLabelsRedis) {
                lists.add(systemLabel.getId());
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < systemLabels.size(); i++) {
            if (lists.contains(systemLabels.get(i))) {
                sb.append(systemLabels.get(i));
                if (i != systemLabels.size() - 1) {
                    sb.append("|");
                }
            }
        }

        String systemLabel = sb.toString();
        String customLabels = new Gson().toJson(updateUserContactLabelVO.getCustom_labels());

        Label label = userLabelMapper.queryUserAllLabelByUID(userContactId);
        if (label != null) {
            userLabelMapper.updateLabelByUCID(userContactId, systemLabel, customLabels);
            userLabelsRedisDao.removeUserContactLabelsCache(userContactId);
        } else {
            userLabelMapper.insertLabelByUCID(userContactId, systemLabel, customLabels);
        }

        JSONObject responseObject = getUserContactLabelsByUCID(userContactId);
        return ApiUtil.getResponse(SUCCESS, responseObject);
    }

    /**
     * 根据好友联系人ID查好友标签
     */
    private JSONObject getUserContactLabelsByUCID(int userContactId) {

        Label label = userLabelMapper.queryUserAllLabelByUID(userContactId);

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
            List<String> systemSelectedIds = label == null ? new ArrayList<>() : Arrays.asList(label.getSystem_label_ids().split(CUT_CHAT));
            for (SystemLabel systemLabel : systemLabels) {
                // entity to dto.
                SystemLabelDTO tmpLabel = new SystemLabelDTO();
                tmpLabel.setId(systemLabel.getId());
                tmpLabel.setName(systemLabel.getSystem_label());
                boolean isSeleted = systemSelectedIds.contains(systemLabel.getId() + "");
                tmpLabel.setSelected(isSeleted);
                if (isSeleted) {
                    selectedLabels.add(systemLabel.getSystem_label());
                }
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
        responseObject.put("user_contact_id", userContactId);
        responseObject.put("selected_labels", selectedLabels);
        responseObject.put("system_labels", sysLabels);
        responseObject.put("custom_labels", customLabels);
        userLabelsRedisDao.setUserContactLabelsCache(userContactId, responseObject);

        return responseObject;
    }
}

