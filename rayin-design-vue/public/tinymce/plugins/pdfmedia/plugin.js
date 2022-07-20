/**
 * Version: 5.1.5 (2020-06-26)
 */
(function () {
  'use strict';

  var global = tinymce.util.Tools.resolve('tinymce.PluginManager');

  var register = function (editor) {

    editor.addCommand('pdfmedia', function () {

        editor.execCommand('mceInsertContent', false, '<object type="file/pdf" data-th-value="" page=""></>');

    });
  };
  var Commands = { register: register };





  var register$1 = function (editor) {

    editor.ui.registry.getAll().icons.pdfmedia || editor.ui.registry.addIcon('pdfmedia','<svg t="1593180187724" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="5314" xmlns:xlink="http://www.w3.org/1999/xlink" width="24" height="24"><defs><style type="text/css"></style></defs><path d="M731.52 251.15l117.15-0.03c8.49 0 12.87-11.15 7.06-17.96L738.59 95.89c-2-2.34-4.52-3.39-6.99-3.39-4.97 0-9.77 4.23-9.77 10.68l-0.01 137.3c0.01 5.89 4.35 10.67 9.7 10.67z m17.4-98.91l66.31 77.71-66.32 0.02 0.01-77.73zM297.37 327.15c-16.45 0-29.79 13.34-29.79 29.79s13.34 29.79 29.79 29.79h429.26c16.45 0 29.79-13.34 29.79-29.79s-13.34-29.79-29.79-29.79H297.37zM391.15 592.18c0-16.51-11.94-22.68-26.61-22.68h-27.13v45.18h26.41c16.01 0 27.33-6.16 27.33-22.5zM556.83 620.05c0-26.14-12.7-50.14-44.04-50.14h-26.15v104.13h25.95c30.68 0 44.24-22.82 44.24-53.99z" p-id="5315"></path><path d="M867.68 464.08V269.95h-42v194.14H198.32v-332.1c0-4.96 3.59-8.99 8-8.99h495.34V81H206.32c-27.57 0-50 22.88-50 50.99v332.09h-61v320.17h61V892c0 28.12 22.43 50.99 50 50.99h611.35c27.57 0 50-22.87 50-50.99V784.26h61V464.08h-60.99z m-244.8 77.87h111.71v28.36h-77.92v39.21h72.99v28.36h-72.99v64.1h-33.79V541.95z m-170.85 0h63.46c46.61 0 77.08 30.7 77.08 78.01 0 46.68-29.56 82.03-78.54 82.03h-62V541.95z m-149.25 0h66.21c34 0 57.09 19.14 57.09 49.56 0 34.77-26.33 50.72-57.9 50.72h-30.77V702h-34.63V541.95z m522.9 350.06c0 4.96-3.59 8.99-8 8.99H206.32c-4.41 0-8-4.03-8-8.99V784.26h627.36v107.75z" p-id="5316"></path></svg>');


    editor.ui.registry.addButton('pdfmedia', {
      icon: 'pdfmedia',
      tooltip: 'pdfmedia',
      onAction: function () {
        return editor.execCommand('pdfmedia');
      }
    });
    editor.ui.registry.addMenuItem('pdfmedia', {
      icon: 'pdfmedia',
      text: 'pdfmedia',
      onAction: function () {
        return editor.execCommand('pdfmedia');
      }
    });
  };
  var Buttons = { register: register$1 };

  function Plugin () {
    global.add('pdfmedia', function (editor) {
      Commands.register(editor);
      Buttons.register(editor);
    });
  }

  Plugin();

}());