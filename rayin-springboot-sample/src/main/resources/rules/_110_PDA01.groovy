package rules
// 导入包
import com.alibaba.fastjson2.JSONPath
import com.alibaba.fastjson2.JSONObject

JSONObject inputData =  input
JSONObject otherData = other

/** 获取变量 **/
//String orgId = JSONPath.eval(inputData,"public.orgId");

/** 判断变量 设置变量 **/

inputData.put("prdName","金宝库110");

println 'Groovy shell:' + inputData.toString()

return inputData
