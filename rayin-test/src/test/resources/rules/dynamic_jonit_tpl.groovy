package rules

import com.alibaba.fastjson2.JSONPath
import com.alibaba.fastjson2.JSONObject
import ink.rayin.htmladapter.base.model.tplconfig.*

JSONObject inputData =  input
JSONObject otherData = other

TemplateConfig template = new TemplateConfig()
List<Element> elements = new ArrayList<Element>()


/** 获取变量 **/
String orgId = JSONPath.eval(inputData,"public.orgId");
/** 判断变量 设置变量 **/
if (orgId == "110") {
    Element element1 = new Element()
    element1.setElementPath("examples/example1/element1.html")
    elements.add(element1)
    Element element2 = new Element()
    element2.setElementPath("examples/example2/element2.html")
    elements.add(element2)
}else{
    Element element1 = new Element()
    element1.setElementPath("examples/example2/element1.html")
    elements.add(element1)
    Element element2 = new Element()
    element2.setElementPath("examples/example1/element1.html")
    elements.add(element2)
}
template.setElements(elements)
return template