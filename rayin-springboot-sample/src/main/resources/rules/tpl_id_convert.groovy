package rules
// 导入包
import com.alibaba.fastjson2.JSONPath
import com.alibaba.fastjson2.JSONObject

JSONObject inputData =  input

/** 获取变量 **/
String orgId = JSONPath.eval(inputData,"public.orgId")
/** 判断变量 设置变量 **/
if (orgId == "110") {
    inputData.put("templateId","example1")
}else{
    inputData.put("templateId","example2")
}

println 'Groovy shell: public.orgId ' + JSONPath.eval(inputData,"public.orgId")


return inputData
