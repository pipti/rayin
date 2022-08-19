package ink.rayin.datarule;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONPath;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import groovy.transform.ThreadInterrupt;
import ink.rayin.htmladapter.base.utils.RayinException;
import ink.rayin.tools.utils.DigestUtil;
import ink.rayin.tools.utils.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ASTTransformationCustomizer;

import org.kohsuke.groovy.sandbox.SandboxTransformer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

/**
 * 数据规则转换引擎
 *
 * 这里使用Groovy作为规则引擎来处理数据
 * 需要做数据规则转换的场景，根据实际业务可能会出现以下的情况:  <br>
 * 1. 源系统数据不方便修改，对源系统数据进行二次加工，修改为pdf需要的数据 <br>
 * 2. pdf才需要的数据，将原有数据进行二次加工，得到新数据字段并应用至pdf中 <br>
 * 3. 根据数据规则指定不同的模板编号，例如相同的数据，不同分公司使用不同的模板 <br>
 * 4. 根据数据规则动态拼接模板，可以应对更复杂的动态场景，例如根据不同的产品选择不同的构件，
 *    假设有20种产品构件，其他构件固定不变的情况，就需要配置20套模板，如果其他构件还有不同的组合，
 *    那模板的数量就会几何式翻倍。<br>
 *    这种情况可以根据数据来动态组合"模板"配置，进而生成PDF。 <br>
 *
 * The data rule conversion engine
 * uses Groovy as the rule engine to handle the scenarios where data needs to be converted by data rules.
 * According to the actual business, the following situations may occur:
 * 1. The data of the source system is inconvenient to modify, and the data of the source system is reprocessed and modified to the data required by pdf
 * 2. For the data that is only required by pdf, the original data is processed twice to obtain new data fields and apply them to the pdf
 * 3. Specify different template numbers according to data rules, such as the same data, different branches use different templates
 * 4. Dynamically splicing templates according to data rules can deal with more complex dynamic scenarios,
 *    such as selecting different components according to different products.
 *    Assuming that there are 20 product components and other components are fixed, you need to configure 20 sets of templates.
 *    If There are different combinations of other components, and the number of templates will double geometrically.
 *    In this case, a "template" configuration can be dynamically combined based on the data to generate a PDF.
 *
 * @auther Jonah Wang / 王柱
 * @date 2022-08-07
 */
@Slf4j
public class RayinDataRule {
    /**
     * groovy脚本执行线程池
     */
    private ThreadPoolExecutor threadPool;
    /**
     * groovy Scriopt对象缓存缓存
     */
    private Cache<String, Script> innerScriptLruCache;
    /**
     * groovy脚本文件缓存
     */
    private Cache<String, String> innerFileLruCache;

    /**
     * 构造方法
     */
    public RayinDataRule(){
        threadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                Runtime.getRuntime().availableProcessors(), 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>(100));
        innerScriptLruCache = CacheBuilder.newBuilder()
                //最大容量
                .maximumSize(1000)
                //当缓存项在指定的时间段内没有被读或写就会被回收
                .expireAfterAccess(60, TimeUnit.SECONDS)
                //当缓存项上一次更新操作之后的多久会被刷新
                //.refreshAfterWrite(5, TimeUnit.SECONDS)
                // 设置并发级别为cpu核心数
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .build();
        innerFileLruCache = CacheBuilder.newBuilder()
                //最大容量
                .maximumSize(1000)
                //当缓存项在指定的时间段内没有被读或写就会被回收
                .expireAfterAccess(60, TimeUnit.SECONDS)
                //当缓存项上一次更新操作之后的多久会被刷新
                //.refreshAfterWrite(5, TimeUnit.SECONDS)
                // 设置并发级别为cpu核心数
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .build();
    }

    /**
     * 构造方法
     * @param scriptObjectMaximumCacheSize groovy script对象缓存最大容量，缓存个数
     * @param scriptObjectCacheExpireAfterAccessSeconds groovy script对象缓存过期时间，单位秒（当缓存项在指定的时间段内没有被读或写就会被回收）
     * @param scriptFileMaximumCacheSize groovy文件缓存最大容量，缓存个数
     * @param scriptFileCacheExpireAfterAccessSeconds groovy文件缓存过期时间，单位秒（当缓存项在指定的时间段内没有被读或写就会被回收）
     */
    public RayinDataRule(int scriptObjectMaximumCacheSize, int scriptObjectCacheExpireAfterAccessSeconds,
                         int scriptFileMaximumCacheSize, int scriptFileCacheExpireAfterAccessSeconds,
                         int groovyExecuteKeepAliveTime, int threadPoolNum){
        threadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                Runtime.getRuntime().availableProcessors(), groovyExecuteKeepAliveTime, TimeUnit.SECONDS, new LinkedBlockingQueue<>(threadPoolNum));
        innerScriptLruCache = CacheBuilder.newBuilder()
                //最大容量
                .maximumSize(scriptObjectMaximumCacheSize)
                //当缓存项在指定的时间段内没有被读或写就会被回收
                .expireAfterAccess(scriptObjectCacheExpireAfterAccessSeconds, TimeUnit.SECONDS)
                //当缓存项上一次更新操作之后的多久会被刷新
                //.refreshAfterWrite(5, TimeUnit.SECONDS)
                // 设置并发级别为cpu核心数
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .build();

        innerFileLruCache = CacheBuilder.newBuilder()
                //最大容量
                .maximumSize(scriptFileMaximumCacheSize)
                //当缓存项在指定的时间段内没有被读或写就会被回收
                .expireAfterAccess(scriptFileCacheExpireAfterAccessSeconds, TimeUnit.SECONDS)
                //当缓存项上一次更新操作之后的多久会被刷新
                //.refreshAfterWrite(5, TimeUnit.SECONDS)
                // 设置并发级别为cpu核心数
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .build();
    }

    /**
     * 执行groovy脚本，缓存key为脚本的md5
     * Execute the groovy script and cache the md5 with the key as the script
     * @param data 生成pdf所需要的数据
     * @param otherData 辅助数据，主要是辅助判断，例如机构参数
     * @param dataName 生成pdf所需要的数据在脚本中注入的变量名，即在脚本中引用的名称
     * @param otherDataName 辅助数据在脚本中注入的变量名，即辅助数据引用的名称
     * @param scriptString 脚本字符串
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public Object executeGroovyScript(JSONObject data, JSONObject otherData, String dataName,
                                           String otherDataName, String scriptString) throws InstantiationException, IllegalAccessException {
        Binding binding = new Binding();
        binding.setProperty(dataName, data);
        binding.setProperty(otherDataName, otherData);
        GroovyShell groovyShell = createGroovyShell();


        String scriptKey = DigestUtil.md5Hex(scriptString);
        Script groovyScript = innerScriptLruCache.getIfPresent(scriptKey);
        if (groovyScript == null) {
            log.debug("reload script cache：\n" + scriptString);
            groovyScript = groovyShell.parse(scriptString);
            innerScriptLruCache.put(scriptKey, groovyScript);
        }

        groovyScript.setBinding(binding);

        Future<Object> future = threadPool.submit((Callable<Object>) groovyScript::run);
        try{
            return future.get(30, TimeUnit.SECONDS);
        }catch (TimeoutException exception) {
            future.cancel(true);
            log.error("TimeoutException,try cancel future task, is cancelled", future.isCancelled());
            //do something else
        }catch (InterruptedException  exception){
            future.cancel(true);
            log.error("InterruptedException,try cancel future task, is cancelled" , future.isCancelled());
        }catch (ExecutionException exception){
            future.cancel(true);
            log.error("ExecutionException,try cancel future task, is cancelled" , future.isCancelled());
        }
        return null;
    }

    /**
     * 执行groovy 脚本文件-根据路径获取脚本文件-通过路径md5对脚本内容进行缓存
     * @param data 生成pdf所需要的数据
     * @param otherData 辅助数据，主要是辅助判断，例如机构参数
     * @param dataName 生成pdf所需要的数据在脚本中注入的变量名，即在脚本中引用的名称
     * @param otherDataName 辅助数据在脚本中注入的变量名，即辅助数据引用的名称
     * @param scriptFileURI 脚本URI see {@link ink.rayin.tools.utils.ResourceUtil#getResourceAsString (final String resourceLocation,final Charset encoding)}
     * @return
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public Object executeGroovyFile(JSONObject data, JSONObject otherData, String dataName,
                                           String otherDataName, String scriptFileURI) throws IOException, InstantiationException, IllegalAccessException {
        String fileKey = DigestUtil.md5Hex(scriptFileURI);
        String scriptString = innerFileLruCache.getIfPresent(fileKey);
        if (scriptString == null) {
            log.debug("reload file cache:" + scriptFileURI);
            scriptString = ResourceUtil.getResourceAsString(scriptFileURI, StandardCharsets.UTF_8);
            innerFileLruCache.put(fileKey, scriptString);
        }

        return executeGroovyScript(data, otherData, dataName, otherDataName,
                scriptString);
    }

    /**
     * 执行groovy 脚本文件-依据数据动态拼接文件名获取脚本
     * Execute the groovy script file - get the script by dynamically concatenating the file name according to the data
     * @param data 生成pdf所需要的数据
     * @param otherData 辅助数据，主要是辅助判断，例如机构参数
     * @param dataName 生成pdf所需要的数据在脚本中注入的变量名，即在脚本中引用的名称
     * @param otherDataName 辅助数据在脚本中注入的变量名，即辅助数据引用的名称
     * @param scriptFileNameSeparator 脚本文件数据名称分隔符，可空
     * @param rulesRootURI 规则脚本所在根路径 see {@link ink.rayin.tools.utils.ResourceUtil#getResourceAsString (final String resourceLocation,final Charset encoding)}
     * @param scriptFileNameDataPaths 数据规则转换脚本文件名称对应的数据路径，可以指定多个，多个可使用scriptFileNameSeparator参数做拼接
     *                            例如：public.org = 110 public.prdCode = PDA01 ,则参数(data,otherData,"input","otherInput", "_", "/Users/xiaobai/rules","public.org","public.prdCode"),
     *                            查找对应的规则脚本文件名称为 "_110_PDA01.groovy"，因为groovy脚本文件名不能以数字开头，因此会增加分隔符前缀
     *                            如果不使用分隔符，注意拼接的规则脚本文件名称最好不要以数字开头。
     *
     * @return
     * @throws IOException
     */
    public Object executeGroovyFile(JSONObject data, JSONObject otherData, String dataName,
                                       String otherDataName, String scriptFileNameSeparator,
                                       String rulesRootURI, String... scriptFileNameDataPaths) throws IOException, InstantiationException, IllegalAccessException {

        String groovyScriptName = scriptFileNameSeparator;
        for(String sndp : scriptFileNameDataPaths){
            if(JSONPath.eval(data, sndp) == null){
                throw new RayinException("parameter error,'" + sndp + "' data path not found!");
            }
            groovyScriptName += JSONPath.eval(data, sndp).toString();
            groovyScriptName = StringUtils.isNotBlank(scriptFileNameSeparator)? groovyScriptName + scriptFileNameSeparator : groovyScriptName;
        }
        groovyScriptName = StringUtils.isNotBlank(scriptFileNameSeparator) ? groovyScriptName.substring(0, groovyScriptName.length() - 1) : groovyScriptName;
        log.info("查找规则脚本文件：" + rulesRootURI + File.separator + groovyScriptName + ".groovy");

        return executeGroovyFile(data, otherData, dataName,
                otherDataName, rulesRootURI + File.separator  + groovyScriptName + ".groovy");
    }

    /**
     * 根据数据获取groovy文件名
     * 可根据数据动态匹配groovy规则脚本
     * @param data 数据
     * @param scriptFileNameSeparator  脚本文件数据名称分隔符，可空
     * @param scriptFileNameDataPaths 数据规则转换脚本文件名称对应的数据路径，可以指定多个，多个可使用scriptFileNameSeparator参数做拼接
     *                                  例如：public.org = 110 public.prdCode = PDA01 ,则参数(data,otherData,"input","otherInput", "_", "/Users/xiaobai/rules","public.org","public.prdCode"),
     *                                  查找对应的规则脚本文件名称为 "_110_PDA01.groovy"，因为groovy脚本文件名不能以数字开头，因此会增加分隔符前缀
     *                                  如果不使用分隔符，注意拼接的规则脚本文件名称最好不要以数字开头。
     * @return
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public String getGroovyFileNameByData(JSONObject data, String scriptFileNameSeparator,
                                    String... scriptFileNameDataPaths) throws IOException, InstantiationException, IllegalAccessException {

        String groovyScriptName = scriptFileNameSeparator;
        for(String sndp : scriptFileNameDataPaths){
            if(JSONPath.eval(data, sndp) == null){
                throw new RayinException("parameter error,'" + sndp + "' data path not found!");
            }
            groovyScriptName += JSONPath.eval(data, sndp).toString();
            groovyScriptName = StringUtils.isNotBlank(scriptFileNameSeparator)? groovyScriptName + scriptFileNameSeparator : groovyScriptName;
        }
        groovyScriptName = StringUtils.isNotBlank(scriptFileNameSeparator) ? groovyScriptName.substring(0, groovyScriptName.length() - 1) : groovyScriptName;
        //log.info("查找规则脚本文件：" + rulesRootURI + File.separator + groovyScriptName + ".groovy");
        return groovyScriptName + ".groovy";
    }

    /**
     * 创建Groovy脚本实例
     * Create Groovy script instance
     * @return
     */
    private static GroovyShell createGroovyShell(){
        CompilerConfiguration config = new CompilerConfiguration();
        config.addCompilationCustomizers(new ASTTransformationCustomizer(ThreadInterrupt.class));

        // 沙盒环境
        config.addCompilationCustomizers(new SandboxTransformer());

        // 注册方法拦截
        new GroovyNotSupportInterceptor().register();
        GroovyShell shell = new GroovyShell(config);

        return shell;
    }

    /**
     * 清理缓存
     * clear cache
     */
    public void clearCache(){
        //缓存内部会加锁处理
        innerScriptLruCache.cleanUp();
        innerFileLruCache.cleanUp();
    }
}
