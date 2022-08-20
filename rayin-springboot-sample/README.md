# springboot样例
## 启动类
RayinSpringbootServerApplication
## pdf模板生成接口
GET http://localhost:8971/pdf/create/tpl/{tplName}/file  
样例：tplId = example1

## pdf模板生成流下载
GET http://localhost:8971/pdf/create/tpl/{tplName}/os  
样例：tplId = example1

## pdf模板生成通过数据获取模板Id
POST http://localhost:8971/pdf/create/file
样例数据：data_sample.json

参见：ink.rayin.sprintboot.controller.PDFCreatorController