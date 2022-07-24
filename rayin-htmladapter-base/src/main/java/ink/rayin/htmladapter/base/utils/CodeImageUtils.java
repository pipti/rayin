/**
 * Copyright (c) 2022-2030, Janah Wang / 王柱 (wangzhu@cityape.tech).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *  http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ink.rayin.htmladapter.base.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 条形码，二维码生成图片工具类
 *
 * @author Janah Wang / 王柱 2019-09-07
 */
public class CodeImageUtils {

    /**
     * 生成二维码
     * @param text
     * @return
     * @throws IOException
     */
    public static ByteArrayOutputStream qrCodeImage(String text) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        //编码
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        //边框距
        hints.put(EncodeHintType.MARGIN, 0);
        BitMatrix bitMatrix;
        try {
            //参数分别为：编码内容、编码类型、图片宽度、图片高度，设置参数
            bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, 300, 300,hints);
        }catch(WriterException e) {
            e.printStackTrace();
            return null;
        }
        // 将二维码输出到页面中
        MatrixToImageWriter.writeToStream(bitMatrix, "png", bos);
        return bos;
    }

    /**
     * 生成二维码base64
     * @param text
     * @return
     * @throws IOException
     */
    public static String qrCodeImageBase64(String text) throws IOException {
        final Base64.Encoder encoder = Base64.getEncoder();
        ByteArrayOutputStream stream = qrCodeImage(text);
        return encoder.encodeToString(stream.toByteArray());
    }


    /**
     * 生成条形码
     * @param text 待生成的内容
     * @param width 宽度 height 高度
     * @param angel 角度
     * @param barcodeFormat 编码格式
     * @return
     * @throws IOException
     * @throws WriterException
     */
    public static ByteArrayOutputStream barCodeImage(String text,int width,int height,int angel,BarcodeFormat barcodeFormat) throws IOException, WriterException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //配置参数
        Map<EncodeHintType,Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        // 容错级别 这里选择最高H级别
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        MultiFormatWriter writer = new MultiFormatWriter();
        // 图像数据转换，使用了矩阵转换 参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
        BitMatrix bitMatrix = writer.encode(text, barcodeFormat, width, height, hints);
        // 将二维码输出到页面中
        MatrixToImageWriter.writeToStream(bitMatrix, "png", bos);

        //if(angel == 0){
            return bos;
        //}

//        BufferedImage res = null;
//        Rectangle rect_des = CalcRotatedSize(new Rectangle(new Dimension(
//                width, height)), angel);
//        res = new BufferedImage(rect_des.width, rect_des.height,
//                BufferedImage.TYPE_INT_RGB);
//        Graphics2D g2 = res.createGraphics();
//        g2.translate((rect_des.width - width) / 2, (rect_des.height - height) / 2);
//        g2.rotate(Math.toRadians(angel), width / 2, height / 2);
//
//        ByteArrayOutputStream bosImg = new ByteArrayOutputStream();
//        Image barCodeImg = ImageIO.read(new ByteArrayInputStream(bos.toByteArray()));
//        g2.drawImage(barCodeImg, null, null);
//        ImageIO.write( res, "png", bosImg );
//
//        if(bos != null){
//            bos.close();
//        }

       // return bosImg;
    }

    /**
     * 计算转换后目标矩形的宽高
     * @param src 源矩形
     * @param angel 角度
     * @return 目标矩形
     */
    private static Rectangle CalcRotatedSize(Rectangle src, int angel) {
        // 如果旋转的角度大于90度做相应的转换
        if (angel >= 90) {
            if (angel / 90 % 2 == 1) {
                int temp = src.height;
                src.height = src.width;
                src.width = temp;
            }
            angel = angel % 90;
        }

        double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
        double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
        double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
        double angel_dalta_width = Math.atan((double) src.height / src.width);
        double angel_dalta_height = Math.atan((double) src.width / src.height);

        int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha
                - angel_dalta_width));
        int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha
                - angel_dalta_height));
        int des_width = src.width + len_dalta_width * 2;
        int des_height = src.height + len_dalta_height * 2;
        return new Rectangle(new Dimension(des_width, des_height));
    }

    /**
     * 生成条形码base64
     * @param text
     * @return
     * @throws IOException
     * @throws WriterException
     */
    public static String barCodeImageBase64(String text,int width,int height,int angel,BarcodeFormat barcodeFormat) throws IOException, WriterException {
        final Base64.Encoder encoder = Base64.getEncoder();
        ByteArrayOutputStream stream = barCodeImage(text,width,height,angel,barcodeFormat);
        return encoder.encodeToString(stream.toByteArray());
    }

}
