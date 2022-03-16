package com.rayin.springboot;

import com.rayin.htmladapter.base.PDFGeneratorInterface;
import com.rayin.htmladapter.base.utils.RayinException;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;


@SpringBootConfiguration
@ConditionalOnClass(PDFGeneratorInterface.class)
@EnableConfigurationProperties(RayinProperties.class)
public class RayinPDFAdapterAutoConfiguration {
    @Resource
    private RayinProperties properties;

//    @Bean
//    @ConditionalOnClass(name= "com.rayin.htmladapter.openhtmltopdf.service.PDFGenerator")
//    public Runnable createHtmlToPdfRunnable(){
//        return () -> {};
//    }
//
//    @Bean
//    @ConditionalOnClass(name= "com.rayin.htmladapter.itext5.service.PDFGenerator")
//    public Runnable createItext5Runnable(){
//        return () -> {};
//    }
//
//    @Bean
//    @ConditionalOnClass(name= "com.rayin.htmladapter.itext2.service.PDFGenerator")
//    public Runnable createItext2Runnable(){
//        return () -> {};
//    }

    @Bean
    @ConditionalOnMissingBean
    public PDFGeneratorInterface pdfGeneratorCreate() throws Exception {
        if(properties.getGeneratorClass().equals("") || properties.getGeneratorClass() == null){
            throw new RayinException("请配置需要实例化的pdf生成器类型-rayin.generatorClass！");
        }
        Class generatorClass = Class.forName(properties.getGeneratorClass()).newInstance().getClass();
        PDFGeneratorInterface c = (PDFGeneratorInterface) generatorClass.newInstance();
        c.init();
        return c;
    }
}
