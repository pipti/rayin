# 一、功能区域
## 编辑器
<img src="/static/help/images/element_help1.png" alt="编辑器" width="100%"/>

## 数据绑定
<img src="/static/help/images/element_help2.png" alt="数据绑定" width="100%"/>

## 源码编辑
<img src="/static/help/images/element_help3.png" alt="源码编辑" width="100%"/>



# 二、特殊控件
1. 隐藏标记控件，用于标记PDF的位置关键字，设置后会在生成PDF的元数据中获取相关关键字对应页码以及坐标；
``` html
<!-- 隐藏标记控件，用于标记PDF的位置关键字，设置后会在生成PDF的元数据中获取相关关键字对应页码以及坐标 -->
<!-- 也可指定宽度样式 例如：style="width:50pt;height:20pt;"-->
<!-- 也可指定位置样式 例如：style="position:absolute;top:10pt,right:10pt"-->
<object type="mark" value="关键字"/>
```
2. PDF控件，用于PDF中嵌入PDF；
``` html
<!-- PDF控件，用于PDF中嵌入PDF 不设置page则默认全部-->
<object type="file/pdf" value="绝对路径/http链接" page="1,2,3"></object>
```
3. 二维码控件，用于PDF中嵌入PDF；
``` html
<!-- 二维码控件，用于PDF中嵌入PDF -->
<object type="image/barcode" value="二维码编码值"></object>
```
4. 条形码控件，format编码类型包括：CODE_39、CODE_128、CODE_93；
``` html
<!-- 条形码控件，format编码类型包括：CODE_39、CODE_128、CODE_93 -->
<object type="image/barcode" value="条形码编码固定值" format="编码方式"></object>


<object type="image/barcode" data-th-value="${json路径动态值}" format="编码方式"></object>
```

如果使用数据标签对应value中，可使用data-th-value，例如
```
<object type="file/pdf"  data-th-value="${json路径}"></object>
```
# 三、常用数据绑定标签
1. 文本类数据绑定标签 data-th-text,通常在<span>、<div>、<td>标签中，可通过左侧数据标签中选择数据->选择右侧编辑器控件->点击左侧绑定标签 添加。
``` html
<span data-th-text="${json路径}"></span>
```

2. 数据循环标签 data-th-each，针对数组类数据，支持多层嵌套。
<img src="/static/help/images/element_help_3_2.png" alt="数据循环标签" width="500px"/>

``` html
<tr data-th-each="slbps1_cpt_lst:${SLBPS1.CPT_LST}">
  <td data-th-text="${slbps1_cpt_lst.CPT_TYPE}"><td>
</tr>
```

3. 脱离html标签的数据绑定标签，utext会将数据中的html标签解析后显示
```
<data-th:block th:utext="${policy}"/>
```

4. 函数标签
样例：${#函数(变量)}
```
<!-- 字符串截取样例 -->
<span data-th-text="${#strings.substring(grpInsuredHome.cntrEstablishDate,0,10)}"></span>
```

5. 表达式判断
gt(>)， lt(<)，ge(> =)，le(<=)，非(!)。 还有eq(==)，neq / ne(!=)
```
 <div data-th-if="${user.isAdmin()} == false">
```

6. 强制分页判断，最后一页不分页
```
//business_cash_value_lstStat 循环变量后系统追加的变量，即business_cash_value_lst+Stat
<div data-th-each="business_cash_value_lst,business_cash_value_lstStat:${business.cash_value_lst}">
//使用business_cash_value_lstStat获取list的索引信息和大小信息
<div style="page-break-after: always;" data-th-if="${business_cash_value_lstStat.index} &lt; ${#lists.size(business.cash_value_lst)} - 1">
</div>
```

7. 属性变量
例如图片 src
```
<img height="100%" width="100%" data-th-src="${images.url}" alt="">
```

# 三、常用标签函数
1. 字符串处理
```
${#strings.isEmpty(name)}
${#strings.arrayIsEmpty(nameArr)}
${#strings.listIsEmpty(nameList)}
${#strings.setIsEmpty(nameSet)}
${#strings.defaultString(text,default)}
${#strings.arrayDefaultString(textArr,default)}
${#strings.listDefaultString(textList,default)}
${#strings.setDefaultString(textSet,default)}
${#strings.contains(name,'ez')}
${#strings.containsIgnoreCase(name,'ez')}
${#strings.startsWith(name,'Don')}
${#strings.endsWith(name,endingFragment)}
${#strings.indexOf(name,frag)}
${#strings.substring(name,3,5)}
${#strings.substringAfter(name,prefix)}
${#strings.substringBefore(name,suffix)}
${#strings.replace(name,'las','ler')}
${#strings.prepend(str,prefix)}
${#strings.append(str,suffix)}
${#strings.toUpperCase(name)}
${#strings.toLowerCase(name)}
${#strings.arrayJoin(namesArray,',')}
${#strings.listJoin(namesList,',')}
${#strings.setJoin(namesSet,',')}
${#strings.arraySplit(namesStr,',')}
${#strings.listSplit(namesStr,',')}
${#strings.setSplit(namesStr,',')}
${#strings.trim(str)}
${#strings.length(str)}
${#strings.abbreviate(str,10)}
${#strings.capitalize(str)}
${#strings.unCapitalize(str)}
${#strings.capitalizeWords(str)}
${#strings.capitalizeWords(str,delimiters)}
${#strings.escapeXml(str)}
${#strings.escapeJava(str)}
${#strings.escapeJavaScript(str)}
${#strings.unescapeJava(str)}
${#strings.unescapeJavaScript(str)}
${#strings.equals(str)}
${#strings.equalsIgnoreCase(str)}
${#strings.concat(str)}
${#strings.concatReplaceNulls(str)}
```

2. 日期格式处理
```
${#dates.format(date)}
${#dates.arrayFormat(datesArray)}
${#dates.listFormat(datesList)}
${#dates.setFormat(datesSet)}
${#dates.format(date, 'yyyy-MM-dd HH:mm:ss')}
${#dates.arrayFormat(datesArray, 'dd/MMM/yyyy HH:mm')}
${#dates.listFormat(datesList, 'dd/MMM/yyyy HH:mm')}
${#dates.setFormat(datesSet, 'dd/MMM/yyyy HH:mm')}
${#dates.day(date)}
${#dates.month(date)}
${#dates.monthName(date)}
${#dates.monthNameShort(date)}
${#dates.year(date)}
${#dates.dayOfWeek(date)}
${#dates.dayOfWeekName(date)}
${#dates.dayOfWeekNameShort(date)}
${#dates.hour(date)}
${#dates.minute(date)}
${#dates.second(date)}
${#dates.millisecond(date)}
${#dates.create(year,month,day)}
${#dates.create(year,month,day,hour,minute)}
${#dates.create(year,month,day,hour,minute,second)}
${#dates.create(year,month,day,hour,minute,second,millisecond)}
${#dates.createNow()}
${#dates.createToday()}
```
3. 日历处理
```
${#calendars.format(cal)}
${#calendars.arrayFormat(calArray)}
${#calendars.listFormat(calList)}
${#calendars.setFormat(calSet)}
${#calendars.format(cal, 'dd/MMM/yyyy HH:mm')}
${#calendars.arrayFormat(calArray, 'dd/MMM/yyyy HH:mm')}
${#calendars.listFormat(calList, 'dd/MMM/yyyy HH:mm')}
${#calendars.setFormat(calSet, 'dd/MMM/yyyy HH:mm')}
${#calendars.day(date)}
${#calendars.month(date)}
${#calendars.monthName(date)}
${#calendars.monthNameShort(date)}
${#calendars.year(date)}
${#calendars.dayOfWeek(date)}
${#calendars.dayOfWeekName(date)}
${#calendars.dayOfWeekNameShort(date)}
${#calendars.hour(date)}
${#calendars.minute(date)}
${#calendars.second(date)}
${#calendars.millisecond(date)}
${#calendars.create(year,month,day)}
${#calendars.create(year,month,day,hour,minute)}
${#calendars.create(year,month,day,hour,minute,second)}
${#calendars.create(year,month,day,hour,minute,second,millisecond)}
${#calendars.createNow()}
${#calendars.createToday()}
```
4. 数字格式化
```
${#numbers.formatInteger(num,3)}
${#numbers.arrayFormatInteger(numArray,3)}
${#numbers.listFormatInteger(numList,3)}
${#numbers.setFormatInteger(numSet,3)}
${#numbers.formatInteger(num,3,'POINT')}
${#numbers.arrayFormatInteger(numArray,3,'POINT')}
${#numbers.listFormatInteger(numList,3,'POINT')}
${#numbers.setFormatInteger(numSet,3,'POINT')}
${#numbers.formatDecimal(num,3,2)}
${#numbers.arrayFormatDecimal(numArray,3,2)}
${#numbers.listFormatDecimal(numList,3,2)}
${#numbers.setFormatDecimal(numSet,3,2)}
${#numbers.formatDecimal(num,3,2,'COMMA')}
${#numbers.arrayFormatDecimal(numArray,3,2,'COMMA')}
${#numbers.listFormatDecimal(numList,3,2,'COMMA')}
${#numbers.setFormatDecimal(numSet,3,2,'COMMA')}
${#numbers.formatDecimal(num,3,'POINT',2,'COMMA')}
${#numbers.arrayFormatDecimal(numArray,3,'POINT',2,'COMMA')}
${#numbers.listFormatDecimal(numList,3,'POINT',2,'COMMA')}
${#numbers.setFormatDecimal(numSet,3,'POINT',2,'COMMA')}
${#numbers.sequence(from,to)}
${#numbers.sequence(from,to,step)}
```
5. list集合相关
```
${#lists.toList(object)}
${#lists.size(list)}
${#lists.isEmpty(list)}
${#lists.contains(list, element)}
${#lists.containsAll(list, elements)}
${#lists.sort(list)}
${#lists.sort(list, comparator)}
```
6. set集合相关
```
${#sets.toSet(object)}
${#sets.size(set)}
${#sets.isEmpty(set)}
${#sets.contains(set, element)}
${#sets.containsAll(set, elements)}
```
