package com.sftc.web.service.impl;


import com.alibaba.druid.util.Base64;
import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIStatus;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.service.EncryptionDecryptionService;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.Security;

import static com.sftc.tools.encode.Base64Util.getBase64;

@Service
public class EncryptionDecryptionServiceImpl implements EncryptionDecryptionService {

    public APIResponse encryptedData(APIRequest apiRequest) throws Exception {
        JSONObject paramOBJ = JSONObject.fromObject(apiRequest.getRequestParam());
        if (!paramOBJ.containsKey("encryptedData") || !paramOBJ.containsKey("iv"))
            return APIUtil.paramErrorResponse("Parameter missing");
        String encryptedData = paramOBJ.getString("encryptedData");

        String iv = paramOBJ.getString("iv");
        String session_key = "tiihtNczf5v6AKRyjwEUhQ==";

        String result = "";

        byte[] resultByte = decrypt(decodeBase64(encryptedData), decodeBase64(session_key), decodeBase64(iv));
        if (null != resultByte && resultByte.length > 0) {
            result = new String(resultByte);
            System.out.println(result);
        }

        return APIUtil.getResponse(APIStatus.SUCCESS, result);
    }

    private byte[] decrypt(byte[] content, byte[] keyByte, byte[] ivByte) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        Key sKeySpec = new SecretKeySpec(keyByte, "AES");
        cipher.init(Cipher.DECRYPT_MODE, sKeySpec, generateIV(ivByte));// 初始化
        byte[] result = cipher.doFinal(content);
        return result;
    }

    //生成iv
    private static AlgorithmParameters generateIV(byte[] iv) throws Exception {
        AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
        params.init(new IvParameterSpec(iv));
        return params;
    }

    private byte[] decodeBase64(String s) {
        return Base64.base64ToByteArray(s);

    }

}
