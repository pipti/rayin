/**
 * Version: 5.1.5 (2020-06-26)
 */
(function () {
    'use strict';

    var global = tinymce.util.Tools.resolve('tinymce.PluginManager');

    var register_newblock = function (editor) {

      editor.addCommand('newblock', function () {
        let element =  tinymce.activeEditor.selection.getNode();

        if(element.nodeName === 'div' || element.nodeName === 'DIV' || element.nodeName === 'span'
          || element.nodeName === 'SPAN'){
          var elDiv = document.createElement("div");
          var elSpan = document.createElement("span");
          elSpan.style.color = "lightgrey";
          elSpan.style.fontStyle = "italic";

          var elSpanText = document.createTextNode('\<新编辑区块\>');
          elSpan.appendChild(elSpanText);
          var elDivText = document.createTextNode('  ');
          elDiv.appendChild(elDivText);
          elDiv.appendChild(elSpan);
          element.parentNode.appendChild(elDiv);
        }else{
          editor.execCommand('mceInsertContent', false, '<div>&nbsp;<span style="color:lightgrey;font-style: italic">\<新编辑区块\></span></div>');
        }

      });
    };
    var Commands = { register: register_newblock };





  var register$1_newblock = function (editor) {

      editor.ui.registry.getAll().icons.newblock || editor.ui.registry.addIcon('newblock','<svg t="1593178993637" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4244" xmlns:xlink="http://www.w3.org/1999/xlink" width="24" height="24"><defs><style type="text/css"></style></defs><path d="M569 358.8H215.8c-20.4 0-36.9-16.5-36.9-36.9s16.5-36.9 36.9-36.9h316.4v-60.4H216.8c-20.4 0-36.9-16.5-36.9-36.9s16.5-36.9 36.9-36.9H569c20.4 0 36.9 16.5 36.9 36.9v134.1c0 20.5-16.5 37-36.9 37zM569 871.7H215.8c-20.4 0-36.9-16.5-36.9-36.9s16.5-36.9 36.9-36.9h316.4v-60.4H216.8c-20.4 0-36.9-16.5-36.9-36.9s16.5-36.9 36.9-36.9H569c20.4 0 36.9 16.5 36.9 36.9v134.1c0 20.6-16.5 37-36.9 37zM820.9 617.8H430.8c-20.4 0-36.9-16.5-36.9-36.9V447.5c0-20.3 16.4-36.6 36.7-36.7l390.1-1.8h0.2c9.7 0 19 3.7 26 10.5 7 7 10.9 16.2 10.9 26v135.4c0 20.4-16.5 36.9-36.9 36.9z m-353.2-73.7h316.4v-61.5l-316.4 1.6v59.9zM307.9 631.1c-9.4 0-18.8-3.6-26.1-10.8l-84-84c-14.4-14.4-14.4-37.8 0-52.1l81.9-81.9c14.4-14.4 37.8-14.4 52.1 0 14.4 14.4 14.4 37.8 0 52.1L276 510.2l57.9 57.9c14.4 14.4 14.4 37.8 0 52.1-7.1 7.3-16.5 10.9-26 10.9z" p-id="4245"></path></svg>');


      editor.ui.registry.addButton('newblock', {
        icon: 'newblock',
        tooltip: 'newblock',
        onAction: function () {
          return editor.execCommand('newblock');
        }
      });
      editor.ui.registry.addMenuItem('newblock', {
        icon: 'newblock',
        text: 'newblock',
        onAction: function () {
          return editor.execCommand('newblock');
        }
      });
    };
    var Buttons = { register: register$1_newblock };

    function Plugin () {
      global.add('newblock', function (editor) {
        Commands.register(editor);
        Buttons.register(editor);
      });
    }

    Plugin();

}());
