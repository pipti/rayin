/**
 * Version: 5.1.5 (2020-06-26)
 */
(function (domGlobals) {
  'use strict';

  var global = tinymce.util.Tools.resolve('tinymce.PluginManager');
  var submitMark = false;
  var checkFunction = function (data) {
    var FnStatus = true;
    var FnMssage = "通过";
    if(!data.Xmark) data.Xmark = 0;
    if(!data.Ymark) data.Ymark = 0;
    if(!data.objHeight) data.objHeight = 0;
    if(!data.objWidth) data.objWidth = 0;
    if(!data.objDeg) data.objDeg = 0;
    if(!data.value) FnStatus = false;
    FnMssage = "值/变量不为空";
    if(FnStatus){
      if(data.posStatus === "absolute" && isNaN(data.Xmark*1)) FnStatus = false;
      if(data.posStatus === "absolute" && isNaN(data.Ymark*1)) FnStatus = false;
      if(isNaN(data.objHeight*1)) FnStatus = false;
      if(isNaN(data.objWidth*1)) FnStatus = false;
      if(data.markType === "barcode" && isNaN(data.objDeg*1)) FnStatus = false;
      FnMssage = "请输入正确的类型";
    }
    return{
      status:FnStatus,
      mssage:FnMssage
    } ;
  }
  var openDialog = function (editor) {
    return editor.windowManager.open({
      title: '插入控件',
      body: {
        type: 'panel',
        items: [{
            type: 'grid',
            columns: 3,
            items: [{
                type: 'selectbox',
                name: 'markType',
                label: '标识类型',
                items: [{
                    text: '关键字',
                    value: 'mark'
                  },
                  {
                    text: '二维码',
                    value: 'qrcode'
                  },
                  {
                    text: '条形码',
                    value: 'barcode'
                  }
                ],
              },
              {
                type: 'selectbox',
                name: 'barcodeType',
                disabled: true,
                label: '编码方式',
                items: [{
                    text: 'CODE_93',
                    value: 'CODE_93'
                  },
                  {
                    text: 'CODE_128',
                    value: 'CODE_128'
                  },
                  {
                    text: 'CODE_39',
                    value: 'CODE_39'
                  },
                ],
              },
              {
                type: 'selectbox',
                name: 'inputType',
                disabled: false,
                label: '值类型',
                items: [{
                    text: '值',
                    value: 'r-value'
                  },
                  {
                    text: '变量',
                    value: 'data-th-value'
                  },
                ],
              },
            ]
          },
          {
            type: 'grid',
            columns: 3,
            items: [{
                type: 'selectbox',
                name: 'posStatus',
                disabled: false,
                label: '定位类型',
                items: [{
                    text: '绝对定位',
                    value: 'absolute'
                  },
                  {
                    text: '相对定位',
                    value: 'relative'
                  },
                ],
              }, {
                type: 'input',
                name: 'Xmark',
                label: 'X：',
                disabled: false
              },
              {
                type: 'input',
                name: 'Ymark',
                label: 'Y:',
                disabled: false
              },
            ] // array of panel components
          },
          {
            type: 'grid',
            columns: 3,
            items: [{
                type: 'input',
                name: 'objWidth',
                label: '宽：',
                disabled: false
              },
              {
                type: 'input',
                name: 'objHeight',
                label: '高:',
                disabled: false
              },
              {
                type: 'input',
                name: 'objDeg',
                label: '角度:',
                disabled: true
              },
            ] // array of panel components
          },
          {
            type: 'input',
            name: 'value',
            label: '值/变量',
          }

        ]
      },
      buttons: [{
          type: 'cancel',
          text: 'Close'
        },
        {
          type: 'submit',
          text: 'Save',
          primary: true
        }
      ],
      onChange: function (dialogApi, details) {
        var data = dialogApi.getData();
        if (details.name === 'markType') {
          dialogApi.disable("barcodeType")
          dialogApi.disable("objDeg")
          if (data.posStatus === 'relative') {
            dialogApi.disable("Xmark")
            dialogApi.disable("Ymark")
          }
          if (data.markType === 'barcode') dialogApi.enable("barcodeType")
          if (data.markType === 'barcode') dialogApi.enable("objDeg")
        }
        if (details.name === 'posStatus') {
          if (data.posStatus === 'absolute') {
            dialogApi.enable("Xmark")
            dialogApi.enable("Ymark")
          }
          if (data.posStatus === 'relative') {
            dialogApi.disable("Xmark")
            dialogApi.disable("Ymark")
          }
        }
      },
      onSubmit: function (api) {
        var data = api.getData();
        let htmlText = "";
        submitMark = checkFunction(data);
        if (submitMark.status) {
          var styleStr = "";
          var dataValue = "";
          var domobj = {
            width: data.objWidth < 50 ? "50px" : data.objWidth + "px",
            height: data.objHeight < 30 ? "30px" : data.objHeight + "px",
            position: data.posStatus || "relative",
          }

          if (data.Xmark) {
            domobj["left"] = data.Xmark < 15 ? "15px" : data.Xmark + "px";
          } else {
            domobj["right"] = data.Xmark < 15 ? "15px" : data.Xmark + "px";
          }
          if (data.Ymark) {
            domobj["top"] = data.Ymark < 15 ? "15px" : data.Ymark + "px";
          } else {
            domobj["bottom"] = data.Ymark < 15 ? "15px" : data.Ymark + "px";
          }

          if (data.inputType === 'data-th-value') {
            dataValue = "${" + data.value + "}"
          } else {
            dataValue = data.value
          }

          if (domobj.position === 'relative') {
            if (domobj.left) domobj.left = null;
            if (domobj.right) domobj.right = null;
            if (domobj.top) domobj.top = null;
            if (domobj.bottom) domobj.bottom = null;
          }
          if(data.markType === "barcode"){
            styleStr = "transform: rotate("+data.objDeg+"deg);"
          }else{
            styleStr = ""
          }
          for (const key in domobj) {
            if (domobj[key]) styleStr += key + ":" + domobj[key] + ";";
          }
          htmlText = `<div r-type="${data.markType}" ${data.inputType}="${dataValue}"
          style="display:inline-block;${styleStr}" `;
          if (data.markType === 'barcode')  htmlText += `r-format = ${data["barcodeType"]}`;
          htmlText += `>${data.value}</div>`;
          editor.execCommand('mceInsertContent', false, htmlText);
          api.close();
        } else {
          domGlobals.alert(submitMark.mssage);
        }
      }
    });
  };


  var register = function (editor) {
    editor.addCommand('mark', function () {
      openDialog(editor)
    });
  };
  var Commands = {
    register: register
  };





  var register$1 = function (editor) {

    editor.ui.registry.getAll().icons.mark || editor.ui.registry.addIcon('mark', '<svg t="1605235761758" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="1226" xmlns:xlink="http://www.w3.org/1999/xlink" width="24" height="24"><defs><style type="text/css"></style></defs><path d="M921.6 192c-38.4-38.4-89.6-59.733333-145.066667-59.733333-55.466667 0-106.666667 21.333333-145.066666 59.733333l-379.733334 379.733333c-34.133333 34.133333-46.933333 89.6-25.6 136.533334 17.066667 46.933333 64 76.8 115.2 76.8 34.133333 0 64-12.8 85.333334-34.133334l379.733333-379.733333c17.066667-17.066667 17.066667-42.666667 0-59.733333-4.266667-4.266667-12.8-8.533333-17.066667-8.533334-12.8-4.266667-29.866667 0-38.4 12.8l-379.733333 379.733334c-17.066667 17.066667-42.666667 17.066667-59.733333 0-17.066667-17.066667-17.066667-42.666667 0-59.733334L691.2 256c21.333333-21.333333 55.466667-38.4 85.333333-34.133333 34.133333 0 64 12.8 85.333334 38.4 46.933333 46.933333 46.933333 128 0 174.933333l-102.4 102.4-281.6 285.866667c-81.066667 81.066667-213.333333 81.066667-290.133334 0-81.066667-81.066667-81.066667-213.333333 0-290.133334l388.266667-388.266666c17.066667-17.066667 17.066667-42.666667 0-59.733334-4.266667-4.266667-12.8-8.533333-17.066667-8.533333-12.8-4.266667-29.866667 0-38.4 12.8L128 465.066667c-110.933333 115.2-110.933333 298.666667 0 409.6 110.933333 110.933333 294.4 115.2 405.333333 0l401.066667-401.066667c4.266667-4.266667 8.533333-8.533333 8.533333-12.8 59.733333-85.333333 46.933333-196.266667-21.333333-268.8z" p-id="1227"></path></svg>');


    editor.ui.registry.addButton('mark', {
      icon: 'mark',
      tooltip: 'mark',
      onAction: function () {
        return openDialog(editor)
      }
    });
    editor.ui.registry.addMenuItem('mark', {
      icon: 'mark',
      text: 'mark',
      onAction: function () {
        return openDialog(editor)
      }
    });
  };
  var Buttons = {
    register: register$1
  };

  function Plugin() {
    global.add('mark', function (editor) {
      Commands.register(editor);
      Buttons.register(editor);
    });
  }

  Plugin();

}(window));
