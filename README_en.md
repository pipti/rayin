# RAYIN
![Hex.pm](https://img.shields.io/hexpm/l/plug)
![Hex.pm](https://img.shields.io/badge/Release-V1.1.2-green.svg)
![Hex.pm](https://img.shields.io/badge/Author-Jonah%20Wang-ff69b4.svg) \
![Hex.pm](https://img.shields.io/badge/JDK-1.8+-green.svg)
![Hex.pm](https://img.shields.io/badge/PDFBOX-2.0.27-green.svg)
![Hex.pm](https://img.shields.io/badge/openhtmltopdf-1.0.10-green.svg)
![Hex.pm](https://img.shields.io/badge/thymeleaf-3.0.11.RELEASE-green.svg)
![Hex.pm](https://img.shields.io/badge/Lombok-1.8.24-green.svg)
![Hex.pm](https://img.shields.io/badge/fastjson2-2.0.10-green.svg)
![Hex.pm](https://img.shields.io/badge/easyexcel-3.1.1-green.svg)
![Hex.pm](https://img.shields.io/badge/groovy-3.0.11-green.svg)
![Hex.pm](https://img.shields.io/badge/jsonpath-2.4.0-green.svg)

[中文](./README.md)  |[English](./README_en.md)
## What's the Rayin!
Can deal with complicated display, template combination business scenario mass production tool of electronic documents.  

## Pain points of the project
Inspired by a large company's internal project.
Found problems in the process of project:
1. Format design is not easy: the original PDF template to make way for complex, relying on A tool, and from the system level through the template for the generation of the logic is complex, and binding data item is also more complicated;
2. Bad extensibility: system realize the template configuration, but due to rely on a large number of fixed coordinates, poor scalability, elements of template above adjustment is a very painful thing, the template production time costs as well as the system would need to be familiar with the cost is very high;
3. Poor maintainability: due to poor extensibility, so many rely on hard code to achieve, maintenance is very poor;
4. Personalized cope with difficulties: the business scenario more personalized, and derived a lot of templates, in the event of changes in the business, the enormous changes in the template workload;
5. Rarely used word problem: often character names don't support, lead to generate documentation errors;
   These problems to make common business personnel through the service means to achieve the template is not reality;
   Based on the above problems, therefore, to solve how to be more convenient for PDF template configuration, if you can better support extensibility and changeful business scenarios;

## Project Process
Starting from the 2019 project for conceptual design, on and off.   
Also made some attempts to test repeatedly, modify repeatedly. 
Encountered in the project needs a lot of new requirements, especially in different provinces personalized style may have different requirements, as well as different personality with content, if the product type many, again there is a personalized, the response to the template will be the index level of growth.   
Needs of changes in the project, if the template is overmuch, demand for a change is a very painful process, and it's easy to miss.   
So the constantly test to try to cover the demand points, through the way of configuration simplified way to meet the needs of variety as much as possible.


## Target
Scalability, flexibility, low code;
Based on the above goal hierarchical design, digestion and changeable step by step a personalized requirements, thereby reducing the number of the template;
Through "data rules" "element" "template" three layer structure through configuration as far as possible to solve the business scenario, rather than hard code to deal with;
   * data rules： Processing business logic, data through data related rules engine to complete, including the extension of the original data, data transformation, and through the rules specified template or dynamic splicing template;
   * element：To solve the combination of data and the style, the style of display, the expansion of the style;
   * template：Solve component reuse in the process of design, page number, blank pages, for large printer paper box selection problem;

In conclusion: trinity, dynamic configuration of multiple link address business scenarios for variety:
<img title="view" src="https://gitee.com/liuercode/images/raw/master/rayin/demonstrate1.png" width="500px">

## The sample show
### Font demo
<img title="Font demo" src="https://gitee.com/liuercode/images/raw/master/rayin/font_dems.png" width="500px">

### Format demo
<img title="Format demo" src="https://gitee.com/liuercode/images/raw/master/rayin/piaoju_sample.png" width="500px">

## Based on the toolkit to implement the design management
### element design
<img title="element design" src="https://rayin-common-resources.oss-cn-beijing.aliyuncs.com/%E6%9E%84%E4%BB%B6%E8%AE%BE%E8%AE%A1_.gif"/>

### The template editor and preview
<img title="The template editor and preview" src="https://rayin-common-resources.oss-cn-beijing.aliyuncs.com/%E6%A8%A1%E6%9D%BF%E7%BC%96%E8%BE%91%E9%A2%84%E8%A7%88_.gif"/>

### Template files generation
<img title="Template files generation" src="https://rayin-common-resources.oss-cn-beijing.aliyuncs.com/%E6%A8%A1%E6%9D%BF%E6%96%87%E4%BB%B6%E7%94%9F%E6%88%90_.gif"/>

## The HTML to PDF technology maturity
There are some articles for web transfer PDF tested some of the open source components transformation, in fact, in most scenarios document class does not require a similar web page so the style of the complex requirements, and web site there are a lot of dynamic js (and some web pages are not necessarily the HTML specification), even if it does, it can through the support of other tags to be realized, and the document class does not need similar js processing logic and dynamic effect.  

## Rayin concept
### data rules
Data rules for auxiliary toolkit for data conversion, usually if they do not conform to the requirements of the generated source system data or for some special business needs custom data, can be transformed using data rule script, implementation scenario can be configured, and reduce the hard code, enhance flexibility.   
### element definition
The child templates in the design of element is a PDF template ,that is a PDF split into multiple blocks to carry on the design, the final template configuration is composed of multiple components;
element function
1. Enhance reusability, reducing repetitive template making, when in a business scenario of a certain piece of content to be not at the same time, do not need to take to make the template, you just need to different module replacement back together. Especially for some traditional industries, regional demand, if not good reuse template there will be a disaster;
2. According to subsequent actual demand for processing, such as for a child need to increase the processing module page. Child module mapping and is convenient to different printers, paper box, cope with the large printer to print requirements;
3. Flexible control of the blank page and page setup requirements;

### template definition
Said above, the template is a combination of one or more components, the template configuration is a set of custom JSON Schema, mainly is the combination of components list as well as relevant Settings page, blank pages, component types;
Template can be a configuration file, can also be a dynamic splicing the JSON data. Can be dynamically according to the actual business logic of the JSON data, to realize the template in the flexible component composition;


## In the main open source framework
### PDFBOX
https://pdfbox.apache.org/  

### openhtmltopdf
https://github.com/danfickle/openhtmltopdf

### thymeleaf 
https://www.thymeleaf.org/index.html  
Similar freemaker dynamic script, open direct benefit is that HTML, the browser can be normal, does not destroy the HTML structure, the design preview is more important.  
### Adobe Fonts
https://github.com/adobe-fonts
### google Fonts noto-cjk
https://github.com/googlefonts/noto-cjk

### W3C CSS
https://www.w3.org/TR/css-page-3/  

## project structure
1. rayin-htmladapter-base                base package
2. rayin-tools                           tools package
3. rayin-htmladapter-openhtmltopdf       openhtmltopdf pdf core package
4. rayin-htmladapter-spring-boot-starter springboot start adapter
5. rayin-test                            test examples
6. rayin-springboot-sample               springboot samples
7. rayin-datarule                        data rules package


## List some support functions and features
1. The following list for openhtmltopdf support CSS to generate electronic documents commonly used features:  
    1. Support automatic extension, including form, long text;  
    2. The head and tail paging support form head to tail;  
    3. Support CSS2.0 and 3.0 portion of the style;  
    4. Support @ Page media features, that is, paper size definition, Page box model (Page shows around);  
    5. Support local loading images, HTTP/HTTPS protocol, according to the loading pictures of base64 display;  
    6. Support the directory label;  
    7. Support the qr code and bar code display;  
2. Dynamic data binding to generate electronic documents listed below part thymeleaf commonly used features： 
    1. Dynamic data binding;  
    2. Data layers of nested loop;  
    3. According to solid block cycle，table，div;  
    4. Data formatting;   
    5. Expression of logic, such as display logic judgment;  
    6. internationalization.  
3. Through the element-> template + data rules -> data, flexible configuration to generate PDF;
4. In the template can undertake combination element of different sizes, such as A4 B5 A3;
5. Custom configuration implementation artifacts types (mainly used for subsequent carton identification of large printers, can pass template configuration, bound to the PDF corresponding metadata, by reading the reprocessing into PS file specifies to the corresponding carton);   
6. Implementation artifacts blank page fill (odd element fill blank pages);  
7. Realize the flexible page count, page number, according to the flexible support component count, single page display page.  
8. Automatically loaded fonts, support for multiple font linkage, according to the rarely used word display;  
9. Implement PDF metadata preservation and access, through templates generated PDF, metadata include the page number information, hidden tag page coordinates, can be used for subsequent processing;  
10. Realize the PDF embedded;   
11. Realize the hidden tags, and can be used to obtain corresponding page through metadata and coordinates (can be used for subsequent reprocessing positioning, such as signature);  
12. Realize the thread pool;  
13. Integration is simple, can be a separate quote jars, and service integration.  



### project url  
Gitee : [https://gitee.com/Rayin/rayin](https://gitee.com/Rayin/rayin)    
GitHub: [https://github.com/pipti/rayin](https://github.com/pipti/rayin)  

### Get Started
* Step one: new project, the introduction of depend on the package.  

```xml
<dependency>
    <groupId>ink.rayin</groupId>
    <artifactId>rayin-htmladapter-openhtmltopdf</artifactId>
    <version>1.1.0</version>
</dependency>

```

* Step 2: create artifacts (i.e., a single HTML)  
* Step 3: create templates (that is, the json config file)  
  Specific see [Help](https://www.yuque.com/liuer_doc/rayin)

* Step 4: generate API calls:
```java
    PDFGeneratorInterface pdfGenerator = new PDFGenerator();
    pdfGenerator.init();
    
    // A single element generated, the data parameter can be null;
    pdfGenerator.generatePdfFileByHtmlAndData(htmlLocation, jsonData, outputFilePath);
    
    // Through the template definition generated, the data parameter can be null;
    pdfGenerator.generatePdfFileByTplConfigFile(templateLocation, jsonData, outputFilePath);
```

## v1.0.6 above, new rules for data processing;
Can the data transformation rules, the template configuration dynamically generated;
Specific see：[data rules help](https://www.yuque.com/liuer_doc/rayin/yu0v5l)  

## See documentation for detailed instructions;
[help](https://rayin.ink)

# ⭐⭐⭐⭐⭐
If thought that it was a good project, please give a ⭐star, oh, your ⭐is my motivation!, 
Program problem or using additive group or submit the issue!, Procedures to avoid problems, 
welcome to ask questions and valuable advice!, If you have a good demand and the suggestion, also welcome!

# Test the sample and sharing industry sample template
All the test cases included in the project
[https://gitee.com/Rayin/rayin-template-sample](https://gitee.com/Rayin/rayin-template-sample)

# Disclaimer：
Any direct or indirect with farce printing generated any content for all the consequences has nothing to do with the farce of developers, content is not limited to, font, graphics, text and other related content;

## Communication group
Enterprise WeChat convenient communication, can use WeChat scan code to add group
<img title="communication" src="https://gitee.com/liuercode/images/raw/master/rayin/weixinqun_qr.JPG" width="300px">