package ink.rayin.app.web.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @program: rayin-app-parent
 * @description:
 * @author: tangyongmao
 * @create: 2020-07-02 14:10
 **/
public class DecryptUtil {

    private static String slatKey= "Bs@2020!Linux@a$";
    private static String key= "!@eprint#$beisun";
    public static String decrypt(String base64Content) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKey secretKey = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec iv = new IvParameterSpec(slatKey.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        byte[] content = Base64.decodeBase64(base64Content);
        byte[] encrypted = cipher.doFinal(content);
        return new String(encrypted);
    }


    public static String encrypt(String content) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKey secretKey = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec iv = new IvParameterSpec(slatKey.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        byte[] encrypted = cipher.doFinal(content.getBytes());
        return Base64.encodeBase64String(encrypted);
    }
}
