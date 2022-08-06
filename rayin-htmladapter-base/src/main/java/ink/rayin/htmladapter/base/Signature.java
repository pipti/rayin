package ink.rayin.htmladapter.base;

import ink.rayin.htmladapter.base.model.tplconfig.SignatureProperty;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.List;

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
public interface Signature {
    /**
     * 多个签章类型
     *
     * @param password            秘钥密码
     * @param keyStorePath        秘钥文件路径
     * @param inputFilePath           签名的PDF文件路径
     * @param signedFilePath          签名文件路径
     * @param signatureProperties 签名坐标、页码、章信息
     */
    void multipleSign(String password, String keyStorePath, String inputFilePath, String signedFilePath,
                             List<SignatureProperty> signatureProperties);

    /**
     * 多个签章类型
     *
     * @param password            秘钥密码
     * @param keyStoreIn          秘钥文件
     * @param inputFileIs         输入PDF文件流
     * @param signedFileOs        签章后文件流
     * @param signatureProperties 签名坐标、页码、章信息
     */
    void multipleSign(String password, InputStream keyStoreIn, InputStream inputFileIs, ByteArrayOutputStream signedFileOs,
                             List<SignatureProperty> signatureProperties);

    /**
     * 单个签章类型
     *
     * @param password          密码
     * @param keyStoreIn        秘钥
     * @param inputFileIs       输入pdf文件流
     * @param signedFileOs      签章后文件流
     * @param signatureProperty 签章属性
     */
    void singleSign(String password, InputStream keyStoreIn, InputStream inputFileIs, ByteArrayOutputStream signedFileOs,
                           SignatureProperty signatureProperty);

    /**
     * 单个签章类型
     *
     * @param password          密码
     * @param keyStorePath      秘钥
     * @param inputFilePath     输入PDF文件路径
     * @param signedFilePath    签名输出文件路径
     * @param signatureProperty 签名设置属性
     */
    void singleSign(String password, String keyStorePath, String inputFilePath, String signedFilePath,
                           SignatureProperty signatureProperty);

}