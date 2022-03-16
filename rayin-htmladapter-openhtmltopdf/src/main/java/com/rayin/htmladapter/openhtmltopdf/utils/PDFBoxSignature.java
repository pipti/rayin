package com.rayin.htmladapter.openhtmltopdf.utils;

import com.rayin.htmladapter.base.model.tplconfig.SignatureProperty;
import com.rayin.htmladapter.openhtmltopdf.signature.CreateVisibleSignature;
import com.rayin.tools.utils.ResourceUtil;
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
 * @author Jonah wz
 */
public class PDFBoxSignature {

    /**
     * 签章
     * @param password
     *            秘钥密码
     * @param keyStorePath
     *            秘钥文件路径
     * @param inputFile
     *            签名的PDF文件
     * @param signatureProperties
     *            签名坐标、页码、章信息
     */
    public static void sign(String password, String keyStorePath, String inputFile, String signedFile,
                            List<SignatureProperty> signatureProperties) throws IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
//        boolean externallySign = false;
//        Security.addProvider(SecurityProvider.getProvider());
//        //CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
//
//
//        // load the keystore
//        KeyStore keyStore = KeyStore.getInstance("PKCS12");
//        keyStore.load(new FileInputStream(keyStorePath), password.toCharArray());
//
//        //Certificate certificate = keyStore.getCertificateChain(keyStore.aliases().nextElement())[0];
//
//        // sign PDF
//        String inPath = inputFile;
//        File destFile;
//        SignatureInfo signatureInfo =signatureInfos.get(0);
//        try (FileInputStream fis = new FileInputStream(signatureInfo.getSignaturePath()))
//        {
//            CreateVisibleSignature signing = new CreateVisibleSignature(keyStore, password.toCharArray());
//            signing.setVisibleSignDesigner(inPath, signatureInfo.getX(), signatureInfo.getY(), -50, fis, signatureInfo.getPageNum());
//            signing.setVisibleSignatureProperties("name", "location", "Security", 0, 1, true);
//            signing.setExternalSigning(externallySign);
//            destFile = new File(signedFile);
//            //signing.signPDF(new File(inPath), destFile, null);
//            signing.signPDF(new FileInputStream(inPath), new FileOutputStream(destFile), null);
//        } catch (UnrecoverableKeyException e) {
//            e.printStackTrace();
//        }

//        sign(password, ResourceUtil.getResource(keyStorePath).getInputStream(),new FileInputStream(inputFile),
//                new FileOutputStream(signedFile),signatureInfos);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        sign(password, ResourceUtil.getResource(keyStorePath).getInputStream(),ResourceUtil.getResourceAsStream(inputFile),
                bos, signatureProperties);
        FileUtils.writeByteArrayToFile(new File(signedFile),bos.toByteArray());
    }

    /**
     * 签章
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
     * @return
     */
    public static void sign(String password, InputStream keyStoreIn, InputStream inputFileIs, ByteArrayOutputStream signedFileOs,
                            List<SignatureProperty> signatureProperties) throws IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
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

            signing.setVisibleSignDesigner(inputFileIs1, signatureProperty.getX(), signatureProperty.getY(), signatureProperty.getZoomPercent(), signatureInfoBis, signatureProperty.getPageNum());
            signing.setVisibleSignatureProperties("name", "location", "Security", 0, signatureProperty.getPageNum(), true);
            signing.setExternalSigning(externallySign);

            signing.signPDF(inputFileIs2, signedFileOs, null);


            tos = signedFileOs;

        }


    }

    /**
     *
     * @param password 密码
     * @param keyStoreIn 秘钥
     * @param inputFileIs 输入pdf文件流
     * @param signedFileOs 签章后文件流
     * @param signatureProperty 签章属性
     * @throws IOException
     * @throws CertificateException
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws UnrecoverableKeyException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static void signSameLocation(String password, InputStream keyStoreIn, InputStream inputFileIs,ByteArrayOutputStream signedFileOs,
                                        SignatureProperty signatureProperty) throws IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
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
        sign(password, keyStoreIn, inputFileIs2, signedFileOs, signatureProperties);
    }

    /**
     *
     * @param password
     * @param keyStorePath
     * @param inputFilePath
     * @param signedFilePath
     * @param signatureProperty
     * @throws IOException
     * @throws CertificateException
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws UnrecoverableKeyException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static void signSameLocation(String password, String keyStorePath, String inputFilePath, String signedFilePath,
                            SignatureProperty signatureProperty) throws IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        signSameLocation(password, new FileInputStream(keyStorePath),new FileInputStream(inputFilePath),
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
