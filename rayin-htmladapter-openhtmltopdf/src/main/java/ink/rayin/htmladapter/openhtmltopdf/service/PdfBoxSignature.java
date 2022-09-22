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
package ink.rayin.htmladapter.openhtmltopdf.service;

import ink.rayin.htmladapter.base.Signature;
import ink.rayin.htmladapter.base.model.tplconfig.SignatureProperty;
import ink.rayin.htmladapter.openhtmltopdf.signature.CreateVisibleSignature;
import ink.rayin.tools.utils.ResourceUtil;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.SecurityProvider;
import org.springframework.util.Base64Utils;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;


/**
 * 签章
 * 签章暂只是测试使用，不要用于生产环境
 * @author Janah Wang / 王柱 2022-03-16
 */
public class PdfBoxSignature implements Signature {

    /**
     * 多个签章类型
     * @param password
     *            秘钥密码
     * @param keyStorePath
     *            秘钥文件路径
     * @param inputFile
     *            签名的PDF文件
     * @param signatureProperties
     *            签名坐标、页码、章信息
     */
    @SneakyThrows
    @Override
    public void multipleSign(String password, String keyStorePath, String inputFile, String signedFile,
                             List<SignatureProperty> signatureProperties) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        multipleSign(password, ResourceUtil.getResourceAsStream(keyStorePath),ResourceUtil.getResourceAsStream(inputFile),
                bos, signatureProperties);
        FileUtils.writeByteArrayToFile(new File(signedFile),bos.toByteArray());
    }

    /**
     * 多个签章类型
     * @param password
     *            秘钥密码
     * @param keyStoreIn
     *            秘钥文件
     * @param inputFileIs
     *            输入PDF文件流
     * @param signedFileOs
     *            签章后文件流
     * @param signatureProperties
     *            签名坐标、页码、章信息
     */
    @SneakyThrows
    @Override
    public void multipleSign(String password, InputStream keyStoreIn, InputStream inputFileIs, ByteArrayOutputStream signedFileOs,
                             List<SignatureProperty> signatureProperties) {
        boolean externallySign = false;
        Security.addProvider(SecurityProvider.getProvider());

        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(keyStoreIn, password.toCharArray());

        CreateVisibleSignature signing = new CreateVisibleSignature(keyStore, password.toCharArray());
        InputStream signatureInfoBis = null;


        ByteArrayOutputStream tos = new ByteArrayOutputStream();
        IOUtils.copy(inputFileIs, tos);
        for(SignatureProperty signatureProperty : signatureProperties){

            InputStream inputFileIs1 = new ByteArrayInputStream(tos.toByteArray());
            InputStream inputFileIs2 = new ByteArrayInputStream(tos.toByteArray());

            if(signatureProperty.getSignatureImage().indexOf(";base64,") > 0){
                byte[] b = Base64Utils.decodeFromString(signatureProperty.getSignatureImage());
                signatureInfoBis = new ByteArrayInputStream(b);
            }else{
                signatureInfoBis = ResourceUtil.getResourceAsStream(signatureProperty.getSignatureImage());
            }

            signing.setVisibleSignDesigner(inputFileIs1, signatureProperty.getX(), signatureProperty.getY(), signatureProperty.getWidth(),signatureProperty.getHeight(), signatureInfoBis, signatureProperty.getPageNum());
            signing.setVisibleSignatureProperties(signatureProperty.getName(), signatureProperty.getLocation(),
                    signatureProperty.getReason(), 0, signatureProperty.getPageNum(), signatureProperty.isVisualSignEnabled());
            signing.setExternalSigning(externallySign);

            signing.signPDF(inputFileIs2, signedFileOs, null);


            tos = signedFileOs;

        }


    }

    /**
     * 单个签章类型
     * @param password 密码
     * @param keyStoreIn 秘钥
     * @param inputFileIs 输入pdf文件流
     * @param signedFileOs 签章后文件流
     * @param signatureProperty 签章属性
     */
    @SneakyThrows
    @Override
    public void singleSign(String password, InputStream keyStoreIn, InputStream inputFileIs, ByteArrayOutputStream signedFileOs,
                           SignatureProperty signatureProperty) {
        ByteArrayOutputStream tos = new ByteArrayOutputStream();
        IOUtils.copy(inputFileIs,tos);
        InputStream inputFileIs1 = new ByteArrayInputStream(tos.toByteArray());
        InputStream inputFileIs2 = new ByteArrayInputStream(tos.toByteArray());

        PDDocument pdDocument = PDDocument.load(inputFileIs1);
        int pageNum = pdDocument.getNumberOfPages();
        pdDocument.close();
        List<SignatureProperty> signatureProperties = new ArrayList<>();



        for(int i = 1; i <= pageNum; i++){
            SignatureProperty cloneSignatureProperty = (SignatureProperty) BeanUtils.cloneBean(signatureProperty);
            cloneSignatureProperty.setPageNum(i);
            signatureProperties.add(cloneSignatureProperty);
        }
        multipleSign(password, keyStoreIn, inputFileIs2, signedFileOs, signatureProperties);
    }

    /**
     * 单个签章类型
     * @param password 密码
     * @param keyStorePath 秘钥路径
     * @param inputFilePath 待签文件路径
     * @param signedFilePath 签名后保存路径
     * @param signatureProperty 签章属性
     */
    @SneakyThrows
    @Override
    public void singleSign(String password, String keyStorePath, String inputFilePath, String signedFilePath,
                           SignatureProperty signatureProperty) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        singleSign(password, ResourceUtil.getResourceAsStream(keyStorePath), ResourceUtil.getResourceAsStream(inputFilePath),
                bos, signatureProperty);
        FileUtils.writeByteArrayToFile(new File(signedFilePath),bos.toByteArray());
    }

    public static void main(String[] args) throws Exception {
//       sign("123456", ResourceLoader.getPath("config") + "/cert/keystore.p12", //
//               ResourceLoader.getPath("config") + "/cert/help.pdf",
//               ResourceLoader.getPath("config") + "/cert/signed.pdf",
//                ResourceLoader.getPath("config") + "/cert/jfsign.gif", 100, 290,1);
        //FileOutputStream f = new FileOutputStream(new File("/Users/Eric/CloudStation/code/beisun-git/rayin-parent/rayin-test/tmp/signed.pdf"));
        //f.write(fileData);
        //f.close();

    }
}
