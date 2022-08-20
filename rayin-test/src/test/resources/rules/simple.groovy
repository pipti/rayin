package rules
// 导入包
import com.alibaba.fastjson2.JSONPath
import com.alibaba.fastjson2.JSONObject

JSONObject inputData =  input
JSONObject otherData = other

/** 获取变量 **/
String orgId = JSONPath.eval(inputData,"public.orgId");

println 'Groovy shell: public.orgId :' + JSONPath.eval(inputData,"public.orgId")
println 'Groovy shell: orgs.110 :' + JSONPath.eval(otherData,"orgs.110")

return inputData.toString();
