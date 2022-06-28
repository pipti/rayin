/**
 * Version: 5.1.5 (2020-06-26)
 */
(function (domGlobals) {
  'use strict';

  var global = tinymce.util.Tools.resolve('tinymce.PluginManager');
  var openDialog = function (editor) {
    let selection = domGlobals.getSelection()
    return editor.windowManager.open({
      title: '插入pdf控件',
      body: {
        type: 'panel',
        items: [{
            type: 'grid',
            columns: 2,
            items: [{
                type: 'selectbox',
                name: 'inputType',
                disabled: false,
                label: '值类型',
                items: [{
                    text: '值',
                    value: 'value'
                  },
                  {
                    text: '变量',
                    value: 'data-th-value'
                  },
                ],
              },
              {
                type: 'selectbox',
                name: 'keyType',
                disabled: true,
                label: '键类型',
                items: [{
                    text: '变量',
                    value: 'value'
                  },
                  {
                    text: '集合',
                    value: 'data-th-value'
                  },
                ],
              },
            ]
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
        if (data.inputType === 'data-th-value') dialogApi.enable("keyType")
        if (data.inputType === 'value') dialogApi.disable("keyType")
      },
      onSubmit: function (api) {
        var data = api.getData();
        var dataValue = "";
        if(data.value){
          if (data.inputType === 'data-th-value') {
            dataValue= "${"+data.value+"}"
          }else{
            dataValue= data.value
          }
          editor.execCommand('mceInsertContent', false,`&nbsp;<div r-type="pdf" ${data.inputType}="${dataValue}"
            style="display:inline-block; position: absolute; right: 15px; bottom: 15px;">${dataValue}</div>`);
          api.close();
        }else{
          domGlobals.alert("值/变量不为空");
        }
      }
    });
  };


  var register = function (editor) {
    editor.addCommand('pdfcon', function () {
      openDialog(editor)
    });
  };
  var Commands = {
    register: register
  };





  var register$1 = function (editor) {

    editor.ui.registry.getAll().icons.mark || editor.ui.registry.addIcon('pdfcon', '<svg t="1607065803157" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="3172" width="24" height="24"><path d="M192 400h39.408c98.4 0 89.872-160 4.592-160H144v208h48v-48z m0-112h31.248c33.984 0 37.2 64-3.312 64H192v-64zM144 80v80h640v487.632L568.128 864H224V528H144v416h457.376L864 680.656V80H144z m336 720h80V640h160v-80H480v240z m-52.864-560H336v208h80.16c63.536 0 93.136-42.752 93.136-103.264 0-49.76-21.44-104.736-82.16-104.736zM384 399.968V288h26.624c40.56 0 44.352 44.272 44.352 56.72 0 63.856-38.976 55.248-70.976 55.248zM688 288v-48h-144v208h48v-80h80v-48h-80v-32h96z" fill="#565D64" p-id="3173"></path></svg>');
    editor.ui.registry.addButton('pdfcon', {
      icon: 'pdfcon',
      tooltip: 'pdfcon',
      onAction: function () {
        return openDialog(editor)
      }
    });
    editor.ui.registry.addMenuItem('pdfcon', {
      icon: 'pdfcon',
      text: 'pdfcon',
      onAction: function () {
        return openDialog(editor)
      }
    });
  };
  var Buttons = {
    register: register$1
  };

  function Plugin() {
    global.add('pdfcon', function (editor) {
      Commands.register(editor);
      Buttons.register(editor);
    });
  }

  Plugin();

}(window));