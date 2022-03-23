/**
 * Copyright (c) 2022-2030, Janah wz 王柱 (carefreefly@163.com).
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
package ink.rayin.springboot;

import ink.rayin.htmladapter.base.PdfGenerator;
import ink.rayin.htmladapter.openhtmltopdf.service.PdfBoxGenerator;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
@ConditionalOnClass(PdfGenerator.class)
@EnableConfigurationProperties(RayinProperties.class)
public class RayinPDFAdapterAutoConfiguration {
//    @Resource
//    private RayinProperties properties;
    @Bean
    @ConditionalOnClass(name= "ink.rayin.htmladapter.openhtmltopdf.service.PdfBoxGenerator")
    public PdfGenerator createHtmlToPdfRunnable() throws Exception {
        PdfBoxGenerator pdfBoxGenerator = new ink.rayin.htmladapter.openhtmltopdf.service.PdfBoxGenerator();
        pdfBoxGenerator.init();
        return pdfBoxGenerator;
    }

    @Bean
    @ConditionalOnClass(name= "ink.rayin.htmladapter.openhtmltopdf.service.PdfBoxSignature")
    public Runnable createPdfBoxSignRunnable(){
        return () -> {};
    }
//
//    @Bean
//    @ConditionalOnClass(name= "ink.rayin.htmladapter.itext5.service.PDFGenerator")
//    public Runnable createItext5Runnable(){
//        return () -> {};
//    }
//
//    @Bean
//    @ConditionalOnClass(name= "ink.rayin.htmladapter.itext2.service.PDFGenerator")
//    public Runnable createItext2Runnable(){
//        return () -> {};
//    }

//    @Bean
//    @ConditionalOnMissingBean
//    public PDFGeneratorInterface pdfGeneratorCreate() throws Exception {
//        if(properties.getGeneratorClass().equals("") || properties.getGeneratorClass() == null){
//            throw new RayinException("请配置需要实例化的pdf生成器类型-rayin.generatorClass！");
//        }
//        Class generatorClass = Class.forName(properties.getGeneratorClass()).newInstance().getClass();
//        PDFGeneratorInterface c = (PDFGeneratorInterface) generatorClass.newInstance();
//        c.init();
//        return c;
//    }
}
