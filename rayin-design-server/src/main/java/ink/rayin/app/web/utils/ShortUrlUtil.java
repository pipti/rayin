package ink.rayin.app.web.utils;

import java.security.MessageDigest;
import java.util.UUID;

/**
 * @program: rayin-app-parent
 * @description:
 * @author: 作者名字
 * @create: 2020-09-08 15:21
 **/
public class ShortUrlUtil {
    // 生成几位短连接地址的签名
    public final static int LENGTH = 6;

    /**
     * 获取短连接
     * @param url
     * @return
     * @return String[]
     * @author tyg
     * @date   2019年1月24日下午2:28:36
     */
    public static String shortUrl(String url){
        return shortUrl(url,LENGTH);
    }
    public static String shortUrl(String url,int length) {
        int size;
        if (length != 0) {
            size = length;
        } else {
            size = LENGTH;
        }
        // 可以自定义生成 MD5 加密字符传前的混合 KEY
        // 要使用生成 URL 的字符
        String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h",
                "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
                "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
                "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H",
                "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
                "U", "V", "W", "X", "Y", "Z"

        };
        // 对传入网址进行 MD5 加密
        String hex = md5ByHex(UUID.randomUUID().toString() + url);


            // 把加密字符按照 8 位一组 16 进制与 0x3FFFFFFF 进行位与运算
        String sTempSubString = hex.substring(8,16);

        // 这里需要使用 long 型来转换，因为 Inteper .parseInt() 只能处理 31 位 , 首位为符号位 , 如果不用long ，则会越界
        long lHexLong = 0x3FFFFFFF & Long.parseLong(sTempSubString, 16);
        String outChars = "";
        for (int j = 0; j < size; j++) {
            // 把得到的值与 0x0000003D 进行位与运算，取得字符数组 chars 索引
            long index = 0x0000003D & lHexLong;
            // 把取得的字符相加
            outChars += chars[(int) index];
            // 每次循环按位右移 5 位
            lHexLong = lHexLong >> 3;
        }
        // 把字符串存入对应索引的输出数组
        return outChars;
    }

    /**
     * MD5加密(32位大写)
     * @param src
     * @return
     * @return String
     * @author tyg
     * @date   2019年5月7日下午6:31:59
     */
    public static String md5ByHex(String src) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] b = src.getBytes();
            md.reset();
            md.update(b);
            byte[] hash = md.digest();
            String hs = "";
            String stmp = "";
            for (int i = 0; i < hash.length; i++) {
                stmp = Integer.toHexString(hash[i] & 0xFF);
                if (stmp.length() == 1)
                    hs = hs + "0" + stmp;
                else {
                    hs = hs + stmp;
                }
            }
            return hs.toUpperCase();
        } catch (Exception e) {
            return "";
        }
    }
}
