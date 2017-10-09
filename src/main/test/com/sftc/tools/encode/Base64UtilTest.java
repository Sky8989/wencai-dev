package com.sftc.tools.encode;

import org.junit.Test;


public class Base64UtilTest {

    @Test
    public void getBase64() throws Exception {
        String testStr = "hello";
        System.out.println(testStr + ": " + Base64Util.getBase64(testStr));
    }

    @Test
    public void getFromBase64() throws Exception {
        String testStr = "YWVzLTI1Ni1jZmI6SGNrRTJGQjhLYUA0Ny45MC41MS44OToxMjE4OA";
        System.out.println(testStr + ": " + Base64Util.getFromBase64(testStr));
    }
}