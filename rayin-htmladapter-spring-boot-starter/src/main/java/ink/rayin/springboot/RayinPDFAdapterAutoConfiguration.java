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
package ink.rayin.springboot;

import ink.rayin.datarule.DataRule;
import ink.rayin.htmladapter.base.PdfGenerator;
import ink.rayin.htmladapter.base.Signature;
import ink.rayin.htmladapter.openhtmltopdf.service.OpenHtmlGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;

@Slf4j
@SpringBootConfiguration
@ConditionalOnClass({PdfGenerator.class, DataRule.class})
@EnableConfigurationProperties({RayinPdfProperties.class,RayinDataRuleProperties.class})
public class RayinPDFAdapterAutoConfiguration {
    @Resource
    private RayinDataRuleProperties dataRuleProperties;
    @Resource
    private RayinPdfProperties pdfProperties;
    @Bean
    @ConditionalOnClass(name= "ink.rayin.htmladapter.openhtmltopdf.service.PdfBoxGenerator")
    public PdfGenerator createHtmlToPdfRunnable() throws Exception {
        OpenHtmlGenerator openHtmlGenerator = new ink.rayin.htmladapter.openhtmltopdf.service.OpenHtmlGenerator();
        openHtmlGenerator.init(pdfProperties.getMinIdle(),pdfProperties.getMaxIdle(), pdfProperties.getMaxTotal(), pdfProperties.getFontPath());

        return openHtmlGenerator;
    }

    @Bean
    @ConditionalOnClass(name= "ink.rayin.htmladapter.openhtmltopdf.service.PdfBoxSignature")
    public Signature createSignRunnable() {
        return new ink.rayin.htmladapter.openhtmltopdf.service.PdfBoxSignature();
    }

    @Bean
    @ConditionalOnClass(name= "ink.rayin.datarule.DataRule")
    public DataRule createDataRuleRunnable() {
        log.info("ScriptObjectMaximumCacheSize:" + dataRuleProperties.getScriptObjectMaximumCacheSize());
        log.info("ScriptObjectCacheExpireAfterAccessSeconds:" + dataRuleProperties.getScriptObjectCacheExpireAfterAccessSeconds());
        log.info("ScriptFileMaximumCacheSize:" + dataRuleProperties.getScriptFileMaximumCacheSize());
        log.info("ScriptFileCacheExpireAfterAccessSeconds:" + dataRuleProperties.getScriptFileCacheExpireAfterAccessSeconds());
        log.info("GroovyExecuteKeepAliveTime:" + dataRuleProperties.getGroovyExecuteKeepAliveTime());
        log.info("GroovyExecuteThreadPoolNum:" + dataRuleProperties.getGroovyExecuteThreadPoolNum());

        DataRule dataRule = new DataRule(dataRuleProperties.getScriptObjectMaximumCacheSize(),
                dataRuleProperties.getScriptObjectCacheExpireAfterAccessSeconds(),
                dataRuleProperties.getScriptFileMaximumCacheSize(),
                dataRuleProperties.getScriptFileCacheExpireAfterAccessSeconds(),
                dataRuleProperties.getGroovyExecuteKeepAliveTime(),
                dataRuleProperties.getGroovyExecuteThreadPoolNum());
        return dataRule;
    }
}
