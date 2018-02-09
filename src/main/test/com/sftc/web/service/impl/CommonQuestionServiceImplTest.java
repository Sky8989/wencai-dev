package com.sftc.web.service.impl;

import com.sftc.web.service.CommonQuestionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring/applicationContext.xml"})
public class CommonQuestionServiceImplTest {

    @Resource
    private CommonQuestionService commonQuestionService;

    @Test
    public void addCommonQuestion() throws Exception {
//        CommonQuestion commonQuestion = new CommonQuestion();
//        commonQuestion.setTitle("title");
//        commonQuestion.setContent("content");
//        commonQuestionService.addCommonQuestion(commonQuestion);
    }

}