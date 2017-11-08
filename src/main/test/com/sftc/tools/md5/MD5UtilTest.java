package com.sftc.tools.md5;

import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.tools.md5
 * @Description:
 * @date 2017/4/7
 * @Time 上午9:19
 */
public class MD5UtilTest {

    @Test
    public void MD5() throws Exception {
        System.out.println(MD5Util.MD5("123"));
    }

}