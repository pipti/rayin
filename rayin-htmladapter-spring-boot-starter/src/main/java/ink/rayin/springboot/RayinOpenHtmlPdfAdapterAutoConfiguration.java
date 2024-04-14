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

import ink.rayin.htmladapter.openhtmltopdf.service.OpenHtmlGenerator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


@SpringBootConfiguration
@ConditionalOnClass({OpenHtmlGenerator.class})
@EnableConfigurationProperties({RayinOpenHtmlProperties.class})
public class RayinOpenHtmlPdfAdapterAutoConfiguration {
    private static Log log = LogFactory.getLog(RayinOpenHtmlPdfAdapterAutoConfiguration.class);

    @Resource
    private RayinOpenHtmlProperties pdfProperties;
    @Bean
    @ConditionalOnClass(name= "ink.rayin.htmladapter.openhtmltopdf.service.OpenHtmlGenerator")
    public OpenHtmlGenerator createOpenHtmlToPdfGeneratorRunnable() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class classZ = Class.forName("ink.rayin.htmladapter.openhtmltopdf.service.OpenHtmlGenerator");
        Constructor cons = classZ.getConstructor();
        OpenHtmlGenerator generator  = (OpenHtmlGenerator)cons.newInstance();
        generator.init(pdfProperties.getMinIdle(),pdfProperties.getMaxIdle(), pdfProperties.getMaxTotal(), pdfProperties.getFontPath());
        return generator;
    }


}
