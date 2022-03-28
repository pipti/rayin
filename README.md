# 睿印
![Hex.pm](https://img.shields.io/hexpm/l/plug)

## 项目起源
某大型公司的内部项目的启发而来。  
项目过程中发现的问题：  
1. 原有针对PDF的模板制作方式复杂，依赖某A的工具，而从系统层面通过该模板实现的生成逻辑复杂。
2. 系统虽然实现模板配置，但是由于依赖大量固定坐标，对模板上面的元素调整是一件十分痛苦的事情，而绑定数据项也是比较繁琐。模板制作的时间成本以及需要对系统的熟悉的成本很高。
3. 原有模板制作以及后续生成方式使用了大量固定坐标，扩展性很差，进而在模板组合的过程中需要大量的硬代码去处理，维护性很差。
4. 业务场景的个性化多，进而带来衍生大量的模板，一旦发生业务变动，所牵涉的模板变动工作量巨大。
以上问题要想让普通的业务人员通过自服务方式去实现模板的制作就不太现实。
因此基于以上问题，去解决如何更方便快捷的进行PDF模板的配置，如果能够更好支持扩展性以及多变的业务场景。

## 达到目标
扩展性、灵活性、自服务。
基于上面的目标选择合适的技术和合适的设计。
1. 通过html-css完成复杂样式的设计；
2. 模板模块化，复用；
3. 通过富文本可以实现在线设计；
4. 通过"数据规则"-"模板组合"-"视图合成"三层结构尽量通过配置来解决业务场景，而非硬代码来处理。
    数据规则：处理数据业务逻辑，规则的实现目前不在项目当中，具体应用过程应该尽量在数据处理过程中将业务逻辑消化完成，例如数据的附加转换，模板的指定等。
    模板组合：解决设计过程中的子模板复用，灵活组合，子模板的显示与否，子模板类型（用于与打印机纸盒映射）。
    视图合成：解决样式的展现，样式的扩展，动态数据与模板的结合；

## 关于html转PDF技术成熟度
网上有一些文章针对网页转PDF进行了一些开源组件的转换测试，其实在大部分场景下文档类并不需要类似网页那么复杂的样式需求（而且有些网页的html并不一定规范），即使有也可以通过其他的支持标签去实现。

## 模板模块化
模板模块化通过拆分子模板来实现模板的复用和复杂业务场景的组合。即"构件"->"模板"
### 构件定义
构件是PDF模板设计中的子模板，即将一个PDF拆分成多个块进行设计，最终的模板配置是由多个构件组成。  
构件作用
    1. 增强复用性,降低重复的模板制作.  
    2. 应对大型生成打印机的纸盒映射.  
    3. 可灵活控制其空白页填充以及页码的设置需求.  

### 模板定义
上面说到，模板就是一个或多个构件的组合拼接，模板配置是自定义的一套JSON Schema，主要是构件的组合列表以及相关的页码、空白页、构件类型相关设置。  
模板可以是配置文件，也可以是动态拼接的JSON数据。可以依据实际的业务逻辑进行动态的JSON数据拼接，来实现模板中灵活的构件组合。


## 项目过程
从2019年项目至于开始构思设计，断断续续。也是做了一些尝试，反复测试，反复修改。
例如：
从freemaker 换成 thymeleaf；
从itext2版本到itext5版本，再到后来的pdfbox；
从flyingsaucer切换到openhtmltopdf；
也是通过项目需求过程中，不断测试尝试能够覆盖的需求点，通过配置的方式尽可能简化的方式满足需求。

## 使用的开源框架一览
### thymeleaf 官网
https://www.thymeleaf.org/index.html  
类似freemaker的动态脚本，好处是直接打开html，浏览器可以正常解析，不破坏html结构，在设计预览比较重要。

### openhtmltopdf
https://github.com/danfickle/openhtmltopdf

### jsoup

### w3c css
https://www.w3.org/TR/css-page-3/  
主要针对@page 样式的设置，通用样式flyingsaucer基本支持，css2基本支持，css3部分支持。
### Adobe Fonts 思源字体
https://github.com/adobe-fonts
### google Fonts noto-cjk
https://github.com/googlefonts/noto-cjk

## 项目结构
1. rayin-htmladapter-base                基础类包
2. rayin-tools                           工具类
3. rayin-htmladapter-openhtmltopdf       openhtmltopdf生成pdf核心包
4. rayin-htmladapter-spring-boot-starter springboot启动适配
5. rayin-test                            测试样例
6. rayin-springboot-serer                springboot-server 样例


## 列举一些实现功能与特性

1. 以下罗列为openhtmltopdf支持的CSS生成电子文件常用的特性：
    1. 支持自动扩展，包括表格，长文本.
    2. 支持表格头尾分页保留头尾.
    3. 支持CSS2.0 以及 3.0部分样式.
    4. 支持 @Page 媒体特性，即纸张大小定义，Page盒模型（页四周显示）.
    5. 支持图片的url显示，图片的base64显示.
    6. 支持目录标签.
    7. 实现二维码和条形码的显示.
2. 以下罗列thymeleaf动态数据绑定生成电子文件常用的特性：
    1. 动态数据.
    2. 数据嵌套循环.
    3. 显示块整体循环，table，div.
    4. 数据格式化.
    5. 显示逻辑判断，包括数据字段与html块，包括div，body等.
    6. 国际化.
3. 通过构件->模板 + 数据，灵活配置生成PDF.
4. 实现构件对应不同页尺寸组合，例如A4+B5+A3.
5. 实现构件类型自定义配置（主要用于后续大型打印机的纸盒识别，可通过模板配置后，绑定至PDF对应的元数据中，通过读取再加工转换为PS文件指定至对应的纸盒）.
6. 实现构件空白页填充（奇数页构件补空白页）.
7. 实现页码灵活计数，页码灵活显示，支持构件重计数,单页显示多页码.
8. 支持生僻字显示.
9. 实现pdf元数据设置.
10. 实现pdf嵌入.
11. 实现字体自动加载.
12. 实现pdf文件信息的源数据设置和读取.
13. 实现隐藏标记，并可通过元数据进行获取起页码，坐标（可用于后续再加工的定位，例如签章位置）。
14. 实现线程池。

### 开始使用
新建项目，引入依赖包
依赖包还在上传中，如果不能自动下载可先下载源码包打包引入。
```xml
<dependency>
    <groupId>com.rayin</groupId>
    <artifactId>rayin-htmladapter-openhtmltopdf</artifactId>
    <version>1.0.1</version>
</dependency>

```

### 创建构件或模板
构件就是单个html
模板是json配置文件
具体参见帮助手册 [https://www.yuque.com/wangzhu-yapmh/rayin/](https://www.yuque.com/wangzhu-yapmh/rayin/)

### 生成代码
```java
    PDFGeneratorInterface pdfGenerator = new PDFGenerator();
    pdfGenerator.init();
    
    //单个构件生成，数据参数可以为空
    pdfGenerator.generatePdfFileByHtmlAndData(elementPath, jsonData, outputFilePath);

    //通过模板定义生成，数据参数可以为空
    pdfGenerator.generatePdfFileByTplConfigFile(tplconfigPath, jsonData, outputFile);

```

## 帮助文档
[https://www.yuque.com/wangzhu-yapmh/rayin/](https://www.yuque.com/wangzhu-yapmh/rayin/)

## 共享模板项目
为了方便大家快速制作模板，大家可分享上传自己制作的模板。
项目地址

## 交流群
<img title="交流群二维码" src="https://gitee.com/liuercode/images/raw/master/rayin/weixinqun_qr.JPG" width="300px">

