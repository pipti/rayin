/**
 *
 * 自定义插件
 * 用来展示thymeleaf标签
 * Version: 5.1.5 (2020-02-06)
 */
(function () {
  'use strict'

  var Cell = function (initial) {
    var value = initial
    var get = function () {
      return value
    }
    var set = function (v) {
      value = v
    }
    var clone = function () {
      return Cell(get())
    }
    return {
      get: get,
      set: set,
      clone: clone
    }
  }

  var global = tinymce.util.Tools.resolve('tinymce.PluginManager')

  var fireVisualBlocks = function (editor, state) {
    editor.fire('VisualThymeleaf', { state: state })
  }
  var Events = { fireVisualBlocks: fireVisualBlocks }

  var toggleVisualBlocks = function (editor, pluginUrl, enabledState) {
    var dom = editor.dom
    dom.toggleClass(editor.getBody(), 'mce-visualthymeleaf')
    enabledState.set(!enabledState.get())
    Events.fireVisualBlocks(editor, enabledState.get())
  }
  var VisualBlocks = { toggleVisualBlocks: toggleVisualBlocks }

  var register = function (editor, pluginUrl, enabledState) {
    editor.addCommand('mceVisualThymeleaf', function () {
      VisualBlocks.toggleVisualBlocks(editor, pluginUrl, enabledState)
    })
  }
  var Commands = { register: register }

  var isEnabledByDefault = function (editor) {
    return editor.getParam('visualthymeleaf_default_state', true, 'boolean')
  }
  var Settings = { isEnabledByDefault: isEnabledByDefault }

  var setup = function (editor, pluginUrl, enabledState) {
    editor.on('PreviewFormats AfterPreviewFormats', function (e) {
      if (enabledState.get()) {
        editor.dom.toggleClass(editor.getBody(), 'mce-visualthymeleaf', e.type === 'afterpreviewformats')
      }
    })
    editor.on('init', function () {
      if (Settings.isEnabledByDefault(editor)) {
        VisualBlocks.toggleVisualBlocks(editor, pluginUrl, enabledState)
      }
    })
    editor.on('remove', function () {
      editor.dom.removeClass(editor.getBody(), 'mce-visualthymeleaf')
    })
  }
  var Bindings = { setup: setup }

  var toggleActiveState = function (editor, enabledState) {
    return function (api) {
      api.setActive(enabledState.get())
      var editorEventCallback = function (e) {
        return api.setActive(e.state)
      }
      editor.on('VisualThymeleaf', editorEventCallback)
      return function () {
        return editor.off('VisualThymeleaf', editorEventCallback)
      }
    }
  }


  var register$1 = function (editor, enabledState) {
    editor.ui.registry.getAll().icons.visualthymeleaf || editor.ui.registry.addIcon('visualthymeleaf','<svg t="1593183219244" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="6169" xmlns:xlink="http://www.w3.org/1999/xlink" width="24" height="24"><defs><style type="text/css"></style></defs><path d="M590.976 670.016v211.008q0 22.016 15.488 37.504t37.504 15.488h211.008q22.016 0 37.504-15.488t15.488-37.504v-211.008q0-22.016-15.488-37.504t-37.504-15.488h-211.008q-22.016 0-37.504 15.488t-15.488 37.504z m211.008 52.992v105.024H696.96v-105.024h105.024zM590.976 142.976v211.008q0 22.016 15.488 37.504t37.504 15.488h211.008q22.016 0 37.504-15.488t15.488-37.504V142.976q0-22.016-15.488-37.504t-37.504-15.488h-211.008q-22.016 0-37.504 15.488t-15.488 37.504z m211.008 53.056v105.024H696.96V196.032h105.024z m-315.968 104.96h105.024V195.968H486.016q-65.984 0-112.512 46.016t-46.528 112v52.992q0 20.992-14.976 36.48t-36.992 15.488H169.024q-22.016 0-37.504 15.488t-15.488 37.504 15.488 37.504 37.504 15.488h105.984q22.016 0 36.992 15.488t14.976 36.48v52.992q0 65.984 46.528 112t112.512 46.016h105.024v-105.024H486.016q-22.016 0-37.504-15.488t-15.488-37.504v-52.992q0-59.008-41.024-105.024 41.024-46.016 41.024-105.024v-52.992q0-22.016 15.488-37.504t37.504-15.488z" p-id="6170"></path></svg>');


    editor.ui.registry.addToggleButton('visualthymeleaf', {
      icon: 'visualthymeleaf',
      tooltip: '显示数据绑定标签',
      onAction: function () {
        return editor.execCommand('mceVisualThymeleaf')
      },
      onSetup: toggleActiveState(editor, enabledState)
    })
    editor.ui.registry.addToggleMenuItem('visualthymeleaf', {
      text: '显示数据绑定标签',
      onAction: function () {
        return editor.execCommand('mceVisualThymeleaf')
      },
      onSetup: toggleActiveState(editor, enabledState)
    })
  }
  var Buttons = { register: register$1 }

  function Plugin () {
    global.add('visualthymeleaf', function (editor, pluginUrl) {
      var enabledState = Cell(false)
      Commands.register(editor, pluginUrl, enabledState)
      Buttons.register(editor, enabledState)
      Bindings.setup(editor, pluginUrl, enabledState)
    })
  }


  Plugin()
}())
