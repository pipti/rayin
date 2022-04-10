# ecs-eprint-web

> E印设计

## Build Setup

``` bash
# install dependencies
npm install

# serve with hot reload at localhost:8080
npm run dev

# build for production with minification
npm run build

# build for production and view the bundle analyzer report
npm run build --report

# run unit tests
npm run unit

# run e2e tests
npm run e2e

# run all tests
npm test
```

### 如果提示缺少eslint
``` bash
npm init -y
npm install eslint --save-dev
```
For a detailed explanation on how things work, check out the [guide](http://vuejs-templates.github.io/webpack/) and [docs for vue-loader](http://vuejs.github.io/vue-loader).



### 2019-01-08 创建
#### 本地获取仓库命令
``` bash
git init
git remote add origin https://gitee.com/liuercode_eprint/ecs-eprint-web.git
git pull origin master
```
#### 提交文件
``` bash
git add 文件
git commit -m "注释"
git push -u origin master
```

##首次安装需要手动安装的插件
将/static/tinymce/plugins/lineheight
/static/tinymce/plugins/indent2em
/static/tinymce/plugins/visualthymeleaf
粘贴至
/node_modules/tinymce/plugins 下

### 编码规范手册
https://cn.vuejs.org/v2/style-guide/
### html缩略图生成插件
http://html2canvas.hertzen.com/
### 富文本编辑器 TinyMCE
https://www.tiny.cloud/
### JSON 插件``
https://leezng.github.io/vue-json-pretty/
### html代码编辑器
https://codemirror.net/index.html
### w3c css
https://www.w3.org/TR/css-page-3/
### 图标库素材
https://www.iconfont.cn/
### vue-awesome
https://fontawesome.dashgame.com/
