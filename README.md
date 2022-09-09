# 睿印
![Hex.pm](https://img.shields.io/hexpm/l/plug)
![Hex.pm](https://img.shields.io/badge/Release-V1.0.6-green.svg)
![Hex.pm](https://img.shields.io/badge/Author-Jonah%20Wang-ff69b4.svg) \
![Hex.pm](https://img.shields.io/badge/JDK-1.8+-green.svg)
![Hex.pm](https://img.shields.io/badge/PDFBOX-2.0.25-green.svg)
![Hex.pm](https://img.shields.io/badge/openhtmltopdf-1.0.10-green.svg)
![Hex.pm](https://img.shields.io/badge/thymeleaf-3.0.11.RELEASE-green.svg)
![Hex.pm](https://img.shields.io/badge/Lombok-1.8.24-green.svg)
![Hex.pm](https://img.shields.io/badge/fastjson2-2.0.10-green.svg)
![Hex.pm](https://img.shields.io/badge/easyexcel-3.1.1-green.svg)

## 睿印是什么
通用业务场景的电子凭证批量生成工具，即 数据+构件+模板=批量动态生成PDF工具

## 项目起源和痛点
某大型公司的内部项目的启发而来。  
项目过程中发现的问题：
1. 版式设计不方便：原有针对PDF的模板制作方式复杂，依赖某A的工具，而从系统层面通过该模板实现的生成逻辑复杂，而绑定数据项也是比较繁琐。
2. 扩展性不好：系统虽然实现模板配置，但是由于依赖大量固定坐标，扩展性很差，对模板上面的元素调整是一件十分痛苦的事情，模板制作的时间成本以及需要对系统的熟悉的成本很高。
3. 可维护性差：由于扩展性差，因此很多依赖硬代码去实现，维护性很差。
4. 个性化应对困难：业务场景的个性化多，进而带来衍生大量的模板，一旦发生业务变动，所牵涉的模板变动工作量巨大。
5. 生僻字问题：经常会遇到人名字库不支持，导致生成的文档错误。
   以上问题要想让普通的业务人员通过自服务方式去实现模板的制作就不太现实。
   因此基于以上问题，去解决如何更方便快捷的进行PDF模板的配置，如果能够更好支持扩展性以及多变的业务场景。

## 项目过程
从2019年项目至于开始构思设计，断断续续。也是做了一些尝试，反复测试，反复修改。 \
从freemaker 换成 thymeleaf； \
从itext2版本到itext5版本，再到后来的pdfbox； \
从flyingsaucer切换到openhtmltopdf，也感谢openhtmltopdf为html生成pdf提供了一个很好的技术方案。\
项目需求中遇到很多新的需求，尤其是不同的省份可能会有不同的个性化样式需求，以及不同的个性穿插内容，如果产品类型多，又存在个性化，那应对的模板将是指数级别的增长。 \
项目中变更的需求，如果模板过多，对于一个变更的需求是一件很痛苦的过程，并且很容易遗漏。 \
因此不断测试尝试能够覆盖的需求点，通过配置的方式尽可能简化的方式满足多样的需求。


## 达到目标
扩展性、灵活性、低代码。
基于上面的目标实现分层设计，逐层消化多变的个性化需求，从而减少模板的数量。
通过"数据规则"-"构件"-"模板"三层结构尽量通过配置来解决业务场景，而非硬代码来处理。
   * 数据规则：处理数据业务逻辑，可通过数据相关的规则引擎去完成，包括数据的延展，数据的转换，以及通过规则进行指定模板或者动态拼接模板。
   * 构件：解决数据与样式的结合，样式的展现，样式的扩展；
   * 模板：解决设计过程中的构件的复用，页码，空白页，针对大型打印机的纸盒选择问题。

综上：三位一体多个环节的动态配置，解决业务多变性场景。
<img title="示意" src="https://gitee.com/liuercode/images/raw/master/rayin/demonstrate1.png" width="500px">

## 样例展示
### 字体演示
<img title="字体演示" src="https://gitee.com/liuercode/images/raw/master/rayin/font_dems.png" width="500px">

### 版式样图
<img title="版式样图" src="https://gitee.com/liuercode/images/raw/master/rayin/piaoju_sample.png" width="500px">

## 基于工具包实现的设计管理端展示
### 构件设计
<img title="构件设计" src="https://rayin-common-resources.oss-cn-beijing.aliyuncs.com/%E6%9E%84%E4%BB%B6%E8%AE%BE%E8%AE%A1_.gif"/>

### 模板编辑与预览
<img title="模板编辑预览" src="https://rayin-common-resources.oss-cn-beijing.aliyuncs.com/%E6%A8%A1%E6%9D%BF%E7%BC%96%E8%BE%91%E9%A2%84%E8%A7%88_.gif"/>

### 模板文件生成
<img title="模板文件生成" src="https://rayin-common-resources.oss-cn-beijing.aliyuncs.com/%E6%A8%A1%E6%9D%BF%E6%96%87%E4%BB%B6%E7%94%9F%E6%88%90_.gif"/>


## 关于html转PDF技术成熟度
网上有一些文章针对网页转PDF进行了一些开源组件的转换测试，其实在大部分场景下文档类并不需要类似网页那么复杂的样式需求，并且网页会存在很多动态的js（而且有些网页的html并不一定规范），即使有也可以通过其他的支持标签去实现，并且文档类并不需要类似js的处理逻辑和动态效果。

## 睿印概念
### 数据规则
数据规则为辅助工具包对数据的转换，通常源系统的数据如果不符合生成的要求或者为了某些特殊业务需要定制数据时，可通过数据规则脚本进行转换，实现场景可配置化，降低硬代码，增强灵活性。

### 构件定义
构件是PDF模板设计中的子模板，即将一个PDF拆分成多个块进行设计，最终的模板配置是由多个构件组成。  
构件作用
1. 增强复用性,降低重复的模板制作，当以个业务场景中的某一块内容出现不同时，不需要重新取制作模板，只需要将不同的模块替换重新组合即可。对于一些传统行业地域需求尤其突出，如果无法很好复用将出现模板灾难。
2. 可根据后续实际需求对其进行处理，例如针对某一个子模块页面需要增加处理。还有就是方便将子模块映射至不同的打印机纸盒，应对大型打印机的打印需求.
3. 可灵活控制其空白页填充以及页码的设置需求。

### 模板定义
上面说到，模板就是一个或多个构件的组合拼接，模板配置是自定义的一套JSON Schema，主要是构件的组合列表以及相关的页码、空白页、构件类型相关设置。  \
模板可以是配置文件，也可以是动态拼接的JSON数据。可以依据实际的业务逻辑进行动态的JSON数据拼接，来实现模板中灵活的构件组合。


## 主要的开源框架一览
### PDFBOX
官网：https://pdfbox.apache.org/  \

### thymeleaf 官网
https://www.thymeleaf.org/index.html  \
类似freemaker的动态脚本，好处是直接打开html，浏览器可以正常解析，不破坏html结构，在设计预览比较重要。

### openhtmltopdf
https://github.com/danfickle/openhtmltopdf

### Adobe Fonts 思源字体
https://github.com/adobe-fonts
### google Fonts noto-cjk
https://github.com/googlefonts/noto-cjk

### W3C CSS
https://www.w3.org/TR/css-page-3/  
主要针对@page 样式的设置，通用样式基本支持，css2基本支持，css3部分支持。

## 项目结构
1. rayin-htmladapter-base                基础类包
2. rayin-tools                           工具类
3. rayin-htmladapter-openhtmltopdf       openhtmltopdf生成pdf核心包
4. rayin-htmladapter-spring-boot-starter springboot启动适配
5. rayin-test                            测试样例
6. rayin-springboot-sample               springboot 样例
7. rayin-datarule                        数据规则工具包


## 列举一些实现功能与特性
1. 以下罗列为openhtmltopdf支持的CSS生成电子文件常用的特性：
    1. 支持自动扩展，包括表格，长文本;
    2. 支持表格头尾分页保留头尾;
    3. 支持CSS2.0 以及 3.0部分样式;
    4. 支持 @Page 媒体特性，即纸张大小定义，Page盒模型（页四周显示）;
    5. 支持图片本地加载，http/https等协议的加载显示，图片的base64显示;
    6. 支持目录标签;
    7. 支持二维码和条形码的显示。
2. 以下罗列部分thymeleaf动态数据绑定生成电子文件常用的特性：
    1. 动态数据绑定；
    2. 数据多层嵌套循环；
    3. 显示块整体循环，table，div；
    4. 数据格式化；
    5. 表达式，逻辑判断，例如显示逻辑判断；
    6. 国际化。
3. 通过构件->模板 + 数据规则->数据，灵活配置生成PDF.
4. 模板中可以对不同尺寸的构件进行组合，例如A4+B5+A3.
5. 实现构件类型自定义配置（主要用于后续大型打印机的纸盒识别，可通过模板配置后，绑定至PDF对应的元数据中，通过读取再加工转换为PS文件指定至对应的纸盒）.
6. 实现构件空白页填充（奇数页构件补空白页）.
7. 实现页码灵活计数，页码灵活显示，支持构件重计数,单页显示多页码.
8. 自动加载字体，支持多字体联动显示，生僻字显示.
9. 实现pdf元数据的保存与获取，通过模板生成的PDF，元数据中包括页码信息，隐藏标签页码坐标，可用于后续加工使用。
10. 实现pdf嵌入. 
11. 实现隐藏标记，并可通过元数据进行获取对应页码，坐标（可用于后续再加工的定位，例如签章位置）。
12. 实现线程池。
13. 集成简便，可单独引用jar包，也可与服务集成。



### 项目地址
Gitee : [https://gitee.com/Rayin/rayin](https://gitee.com/Rayin/rayin)  
GitHub: [https://github.com/pipti/rayin](https://github.com/pipti/rayin)

### 开始使用
* 第一步：新建项目，引入依赖包 \
如果使用阿里云可能会存在同步滞后，如果不能自动下载可使用原始的maven源。

```xml
<dependency>
    <groupId>ink.rayin</groupId>
    <artifactId>rayin-htmladapter-openhtmltopdf</artifactId>
    <version>1.0.7</version>
</dependency>

```

* 第二步：创建构件（即单个html） 
* 第三步：创建模板（即json配置文件） 
具体参见 [帮助手册](https://www.yuque.com/liuer_doc/rayin)

* 第四步：调用生成API
```java
    PDFGeneratorInterface pdfGenerator = new PDFGenerator();
    pdfGenerator.init();
    
    //单个构件生成，数据参数可以为空
    pdfGenerator.generatePdfFileByHtmlAndData(htmlLocation, jsonData, outputFilePath);

    //通过模板定义生成，数据参数可以为空
    pdfGenerator.generatePdfFileByTplConfigFile(templateLocation, jsonData, outputFilePath);
```

## v1.0.6 以上版本 新增数据规则处理
可以对数据进行规则转换，模板配置动态生成。  
参见：[数据规则帮助](https://www.yuque.com/liuer_doc/rayin/yu0v5l)  

## 详细使用说明请参见帮助文档
[帮助文档](https://www.yuque.com/liuer_doc/rayin)

# ⭐⭐⭐⭐⭐
如果觉得项目不错，麻烦给个⭐️star噢，你的⭐️就是我前进的动力! \
程序问题或者使用问题可加群或提交issue！ \
程序难免存在问题，欢迎提出问题以及宝贵的建议！  

# 欢迎共享行业样例模板
[https://gitee.com/Rayin/rayin-template-sample](https://gitee.com/Rayin/rayin-template-sample)

## 交流群
<img title="交流群二维码" src="https://gitee.com/liuercode/images/raw/master/rayin/wxqun_qrcode.JPG" width="300px">

<img title="交流群二维码" src="https://gitee.com/liuercode/images/raw/master/rayin/weixinqun_qr.JPG" width="300px">