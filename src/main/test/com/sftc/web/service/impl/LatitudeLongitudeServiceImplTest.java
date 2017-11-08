package com.sftc.web.service.impl;

import com.sftc.tools.constant.LLConstant;
import org.junit.Test;

import java.time.LocalTime;

import static org.junit.Assert.*;

/**
 * 经纬度的测试类
 * Created by huxingyue on 2017/9/6.
 */
public class LatitudeLongitudeServiceImplTest {


    @Test
    public void checkTimeIsLogicalTest() {
        LocalTime now = LocalTime.now();
        int now_hour = LocalTime.now().getHour();

        if (now_hour > LLConstant.END_HOUR|| now_hour < LLConstant.BEGIN_HOUR )
        {
            System.out.println("现在不是工作时间，应该没有小哥在路上");
            return;
        }
        System.out.println("现在是工作时间，小哥在路上");
    }
}