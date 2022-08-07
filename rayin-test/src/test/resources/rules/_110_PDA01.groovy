package rules
// 导入包
import com.alibaba.fastjson2.JSONPath
import com.alibaba.fastjson2.JSONObject

JSONObject inputData =  input
JSONObject otherData = other

/** 获取变量 **/
String orgId = JSONPath.eval(inputData,"public.orgId");
/** 判断变量 设置变量 **/
if (orgId == "110") {
    inputData.put("templateId","110000001");
}else{
    inputData.put("templateId","120000001");
}

println 'Groovy shell: public.orgId ' + JSONPath.eval(inputData,"public.orgId")


JSONObject orgs = otherData.get("orgs");
orgName = orgs.get(orgId);
JSONPath.set(inputData, "public.orgName" , orgName);
return inputData.toString();
