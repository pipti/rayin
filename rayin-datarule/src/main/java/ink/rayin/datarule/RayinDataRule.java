package ink.rayin.datarule;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONPath;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.transform.ConditionalInterrupt;
import groovy.transform.ThreadInterrupt;
import ink.rayin.htmladapter.base.utils.RayinException;
import ink.rayin.tools.utils.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.ClosureExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ASTTransformationCustomizer;

import org.kohsuke.groovy.sandbox.SandboxTransformer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

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
 * @auther Jonah Wang / 王柱
 * @date 2022-08-07
 */
@Slf4j
public class RayinDataRule {


    /**
     * 执行groovy脚本
     * @param data 生成pdf数据
     * @param otherData 辅助数据
     * @param dataName 数据在脚本中注入的变量名
     * @param otherDataName 辅助数据在脚本中注入的变量名
     * @param scriptString 脚本字符串
     * @return
     */
    public static Object executeGroovyScript(JSONObject data, JSONObject otherData, String dataName,
                                           String otherDataName, String scriptString){
        Binding binding = new Binding();
        binding.setProperty(dataName, data);
        binding.setProperty(otherDataName, otherData);
        GroovyShell groovyShell = createGroovyShell(binding);

        Object result = groovyShell.evaluate(scriptString);
        return result;
    }


    public static Object executeGroovyFile(JSONObject data, JSONObject otherData, String dataName,
                                           String otherDataName, String scriptFilePath) throws IOException {
        return executeGroovyScript(data, otherData, dataName, otherDataName,
                ResourceUtil.getResourceAsString(scriptFilePath, StandardCharsets.UTF_8));
    }

    /**
     * 执行groovy 脚本文件
     * @param data 生成pdf数据
     * @param otherData 辅助数据
     * @param dataName 数据在脚本中注入的变量名
     * @param otherDataName 辅助数据在脚本中注入的变量名
     * @param scriptFileNameSeparator 文件数据名称分隔符，可空，例如
     * @param rulesRootPath 规则脚本所在根目录
     * @see ink.rayin.tools.utils.ResourceUtil#getResourceAsString
     * @param scriptFileNameDataPaths 数据规则转换脚本文件名称对应的数据路径，可以指定多个，多个可使用scriptFileNameSeparator参数做拼接
     *                            例如：public.org = 110 public.prdCode = PDA01 ,则参数(data,otherData,"input","otherInput", "_", "/Users/xiaobai/rules","public.org","public.prdCode"),
     *                            查找对应的规则脚本文件名称为 "_110_PDA01.groovy"，因为groovy脚本文件名不能以数字开头，因此会增加分隔符前缀
     *                            如果不使用分隔符，注意拼接的规则脚本文件名称最好不要以数字开头。
     *
     * @return
     * @throws IOException
     */
    public static Object executeGroovyFile(JSONObject data, JSONObject otherData, String dataName,
                                       String otherDataName, String scriptFileNameSeparator,
                                       String rulesRootPath, String... scriptFileNameDataPaths) throws IOException {
//        if(data == null){
//            return null;
//        }
        //String script = "";
        String groovyScriptName = scriptFileNameSeparator;
        for(String sndp : scriptFileNameDataPaths){
            if(JSONPath.eval(data, sndp) == null){
                throw new RayinException("parameter error,'" + sndp + "' data path not found!");
            }
            groovyScriptName += JSONPath.eval(data, sndp).toString();
            groovyScriptName = StringUtils.isNotBlank(scriptFileNameSeparator)? groovyScriptName + scriptFileNameSeparator : groovyScriptName;
        }
        groovyScriptName = StringUtils.isNotBlank(scriptFileNameSeparator) ? groovyScriptName.substring(0, groovyScriptName.length() - 1) : groovyScriptName;
        log.info("查找规则脚本文件：" + rulesRootPath + "/" + groovyScriptName + ".groovy");

//        if(StringUtils.isNotBlank(groovyScriptName)){
//            script += ResourceUtil.getResourceAsString(rulesDocPath + "/" + groovyScriptName + ".groovy", StandardCharsets.UTF_8);
//        }else{
//            log.error("data error,grovvy script name is null");
//            throw new RayinException("data error,grovvy script name is null");
//        }

//        Binding binding = new Binding();
//        binding.setProperty(dataName, data);
//        binding.setProperty(otherDataName, otherData);
//        GroovyShell groovyShell = createGroovyShell(binding);
//
//        Object result = groovyShell.evaluate(ResourceUtil.getResourceAsString("rules/" + groovyScriptName + ".groovy", StandardCharsets.UTF_8));
//        return result;
        return executeGroovyScript(data, otherData, dataName,
                otherDataName, ResourceUtil.getResourceAsString(rulesRootPath + "/"  + groovyScriptName + ".groovy", StandardCharsets.UTF_8));
    }

    private static GroovyShell createGroovyShell(Binding binding){
        CompilerConfiguration config = new CompilerConfiguration();
        config.addCompilationCustomizers(new ASTTransformationCustomizer(ThreadInterrupt.class));
        Map<String, Object> timeoutArgs = new HashMap<>();
        timeoutArgs.put("value", new ClosureExpression(
                Parameter.EMPTY_ARRAY,
                new ExpressionStatement(
                        new MethodCallExpression(
                                new ClassExpression(ClassHelper.make(ConditionInterceptor.class)),
                                "checkTimeout",
                                new ConstantExpression(10))
                )
        ));
        config.addCompilationCustomizers(new ASTTransformationCustomizer(timeoutArgs, ConditionalInterrupt.class));

        // 沙盒环境
        config.addCompilationCustomizers(new SandboxTransformer());


//        GroovyClassLoader groovyLoader = new GroovyClassLoader();
//        Class<Script> groovyClass = (Class<Script>) groovyLoader.parseClass(parentScriptString);

        GroovyShell shell = new GroovyShell(null, binding, config);
        // 注册方法拦截
        new GroovyNotSupportInterceptor().register();

        return shell;
    }
}
