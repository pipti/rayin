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

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "rayin")
public class RayinProperties {
    public String getGeneratorClass() {
        return generatorClass;
    }

    public void setGeneratorClass(String generatorClass) {
        this.generatorClass = generatorClass;
    }

    private String generatorClass;
}

