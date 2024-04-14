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

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "rayin.datarule")
public class RayinDataRuleProperties {

    /**
     * 规则脚本路径
     */
    private String ruleScriptPath;

    /**
     * groovy script对象缓存最大容量，缓存个数
     */
    private int scriptObjectMaximumCacheSize = 100;
    /**
     * groovy script对象缓存过期时间，单位秒（当缓存项在指定的时间段内没有被读或写就会被回收）
     */
    private int scriptObjectCacheExpireAfterAccessSeconds = 1800;
    /**
     * groovy文件缓存最大容量，缓存个数
     */
    private int scriptFileMaximumCacheSize = 100;
    /**
     * groovy文件缓存过期时间，单位秒（当缓存项在指定的时间段内没有被读或写就会被回收）
     */
    private int scriptFileCacheExpireAfterAccessSeconds = 1800;
    /**
     * 线程活动时间
     */
    private int groovyExecuteKeepAliveTime = 30;
    /**
     * 执行任务队列数
     */
    private int groovyExecuteThreadPoolNum = 500;

    public String getRuleScriptPath() {
        return ruleScriptPath;
    }

    public void setRuleScriptPath(String ruleScriptPath) {
        this.ruleScriptPath = ruleScriptPath;
    }

    public int getScriptObjectMaximumCacheSize() {
        return scriptObjectMaximumCacheSize;
    }

    public void setScriptObjectMaximumCacheSize(int scriptObjectMaximumCacheSize) {
        this.scriptObjectMaximumCacheSize = scriptObjectMaximumCacheSize;
    }

    public int getScriptObjectCacheExpireAfterAccessSeconds() {
        return scriptObjectCacheExpireAfterAccessSeconds;
    }

    public void setScriptObjectCacheExpireAfterAccessSeconds(int scriptObjectCacheExpireAfterAccessSeconds) {
        this.scriptObjectCacheExpireAfterAccessSeconds = scriptObjectCacheExpireAfterAccessSeconds;
    }

    public int getScriptFileMaximumCacheSize() {
        return scriptFileMaximumCacheSize;
    }

    public void setScriptFileMaximumCacheSize(int scriptFileMaximumCacheSize) {
        this.scriptFileMaximumCacheSize = scriptFileMaximumCacheSize;
    }

    public int getScriptFileCacheExpireAfterAccessSeconds() {
        return scriptFileCacheExpireAfterAccessSeconds;
    }

    public void setScriptFileCacheExpireAfterAccessSeconds(int scriptFileCacheExpireAfterAccessSeconds) {
        this.scriptFileCacheExpireAfterAccessSeconds = scriptFileCacheExpireAfterAccessSeconds;
    }

    public int getGroovyExecuteKeepAliveTime() {
        return groovyExecuteKeepAliveTime;
    }

    public void setGroovyExecuteKeepAliveTime(int groovyExecuteKeepAliveTime) {
        this.groovyExecuteKeepAliveTime = groovyExecuteKeepAliveTime;
    }

    public int getGroovyExecuteThreadPoolNum() {
        return groovyExecuteThreadPoolNum;
    }

    public void setGroovyExecuteThreadPoolNum(int groovyExecuteThreadPoolNum) {
        this.groovyExecuteThreadPoolNum = groovyExecuteThreadPoolNum;
    }
}

