package com.aoshuotec.iron.cornerstone.encryptKit;

import org.java_websocket.util.Base64;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Created by molaith on 2017/8/31.
 */

public class KeyUtil {
    public static String createSerialKey(){
        String preKey=""+(System.currentTimeMillis()/1000);

        String publickey="";
        try {
            byte[] encodedkey=RSAUtil.encryptData(preKey.getBytes("UTF-8"),RSAUtil.loadPublicKey(publickey));
            preKey= Base64.encodeBytes(encodedkey);
            preKey=preKey.replace("+","-");
            preKey=preKey.replace("/","*");
            return preKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
