package com.sftc.web.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIStatus;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.dao.jpa.CommonQuestionDao;
import com.sftc.web.dao.mybatis.CommonQuestionMapper;
import com.sftc.web.dao.redis.CommonQuestionRedisDao;
import com.sftc.web.model.entity.CommonQuestion;
import com.sftc.web.service.CommonQuestionService;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service("commonQuestionService")
public class CommonQuestionServiceImpl implements CommonQuestionService {

    @Resource
    private CommonQuestionMapper commonQuestionMapper;

    @Resource
    private CommonQuestionDao commonQuestionDao;

    @Resource
    private CommonQuestionRedisDao commonQuestionRedisDao;

    // 获取常见问题
    public APIResponse getCommonQuestion() {
        // 尝试从redis缓存中获取数据
        List<CommonQuestion> commonQuestionList = commonQuestionRedisDao.getCommonQuestionsFromCache();
        if (commonQuestionList == null) {
            commonQuestionList = commonQuestionMapper.getCommonQuestion();
            commonQuestionRedisDao.setCommonQuestionsToCache(commonQuestionList);
        }

        return APIUtil.getResponse(APIStatus.SUCCESS, commonQuestionList);
    }


    /**
     * CMS 获取常见问题列表 条件查询+分页
     */
    public APIResponse selectList(APIRequest apiRequest) throws Exception {
        APIStatus status = APIStatus.SUCCESS;
        // 此处封装了 User的构造方法
        HttpServletRequest httpServletRequest = apiRequest.getRequest();
        CommonQuestion commonQuestion = new CommonQuestion(httpServletRequest);
        int pageNumKey = Integer.parseInt(httpServletRequest.getParameter("pageNumKey"));
        int pageSizeKey = Integer.parseInt(httpServletRequest.getParameter("pageSizeKey"));
        PageHelper.startPage(pageNumKey, pageSizeKey);
        List<CommonQuestion> commonQuestionList = commonQuestionMapper.selectByPage(commonQuestion);
       
        if (commonQuestionList.size() == 0) {
            return APIUtil.selectErrorResponse("搜索到的结果数为0，请检查查询条件", null);
        } else {
            return APIUtil.getResponse(status,  PageHelper.startPage(commonQuestionList));
        }
    }

    // 根据id查询常见问题
    public APIResponse selectListById(APIRequest request) throws Exception {
        HttpServletRequest httpServletRequest = request.getRequest();
        // 此处封装了 User的构造方法
        CommonQuestion commonQuestion = new CommonQuestion(httpServletRequest);
        CommonQuestion commonQuestion1 = commonQuestionMapper.selectListById(commonQuestion);
        if (commonQuestion1 == null) {
            return APIUtil.selectErrorResponse("搜索到的结果数为0，请检查查询条件", null);
        } else {
            return APIUtil.getResponse(APIStatus.SUCCESS, commonQuestion1);
        }
    }

    /**
     * CMS 添加常见问题信息
     */
    public APIResponse addCommonQuestion(CommonQuestion commonQuestion) throws Exception {
        commonQuestion.setCreate_time(Long.toString(System.currentTimeMillis()));
        commonQuestionDao.save(commonQuestion);
        commonQuestionRedisDao.clearCommonQuestionsCache();

        return APIUtil.getResponse(APIStatus.SUCCESS, commonQuestion);
    }

    /**
     * CMS 修改常见问题
     */
    public APIResponse updateCommonQuestion(CommonQuestion commonQuestion) throws Exception {
        if(commonQuestionMapper.updateCommonQuestion(commonQuestion) > 0 ){
        	commonQuestionRedisDao.clearCommonQuestionsCache();
        	return APIUtil.getResponse(APIStatus.SUCCESS, commonQuestion);
        }else{
        	return APIUtil.getResponse(APIStatus.PARAM_ERROR, "修改失败,不存在id="+commonQuestion.getId());
        }

    }
    /**
     * CMS 删除常见问题
     */
    public APIResponse deleteCommonQuestion(int id) throws Exception {
       if(commonQuestionMapper.deleteCommonQuestion(id) > 0){
    	   commonQuestionRedisDao.clearCommonQuestionsCache();
    	   return APIUtil.getResponse(APIStatus.SUCCESS, id);
       }else{
    	   return APIUtil.getResponse(APIStatus.PARAM_ERROR, "删除失败，不存在id="+id);
       }

    }
    /**
     * 查询常见问题List并分页
     */
	@Override
	public APIResponse selectListPaging(APIRequest apiRequest) {
		JSONObject paramObject = JSONObject.fromObject(apiRequest.getRequestParam());
		 	int pageNumKey = paramObject.getInt("pageNumKey");
	        int pageSizeKey = paramObject.getInt("pageSizeKey");
	        CommonQuestion commonQuestion = new CommonQuestion();
	        commonQuestion.setId(paramObject.getInt("id"));
	        commonQuestion.setTitle(paramObject.getString("title"));
	        commonQuestion.setContent(paramObject.getString("content"));
	       
	        //  使用lambab表达式 配合pageHelper实现对用户列表和查询相关信息的统一查询
	        PageInfo<Object> pageInfo = PageHelper.startPage(pageNumKey, pageSizeKey).doSelectPageInfo(() -> commonQuestionMapper.selectByPage(commonQuestion));
	        return APIUtil.getResponse(APIStatus.SUCCESS, pageInfo);
	        
	}

	
	/**
	 * id为0时 为新增操作  id 非0为修改操作
	 */
	@Override
	public APIResponse save(CommonQuestion commonQuestion) throws Exception {
		if (commonQuestion == null) {
			return APIUtil.getResponse(APIStatus.PARAM_ERROR, "save失败，传入对象为 =" + commonQuestion);
		}
		
		if(commonQuestion.getId() != 0)
			return updateCommonQuestion(commonQuestion);
		
		return addCommonQuestion(commonQuestion);
		
	}


	
}
