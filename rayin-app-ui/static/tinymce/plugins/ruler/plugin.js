
(function () {
  'use strict'

  var global = tinymce.util.Tools.resolve('tinymce.PluginManager')

  function Plugin () {
    global.add('ruler', function (editor) {
      var domHtml
      var lastPageBreaks

      function refreshRuler () {
        console.log('ddd')
        try {
          domHtml = $(editor.getDoc().getElementsByTagName('HTML')[0])

          // HACK - erase this, I have to put my CSS here
          console.log($('tinystyle').html())
          domHtml.find('head').append($('<style>' + $('tinystyle').html() + '</style>'))
        } catch (e) {
          console.log(e)
          //return setTimeout(refreshRuler, 50)
        }

        var dpi = 96
        var cm = dpi / 2.54
        var a4px = cm * (29.7 - 5.7) // A4 height in px, -5.5 are my additional margins in my PDF print

        // ruler begins (in px)
        var startMargin = 4

        // max size (in px) = document size + extra to be sure, idk, the height is too small for some reason
        var imgH = domHtml.height() + a4px * 5

        var pageBreakHeight = 14 // height of the pagebreak line in tinyMce

        var pageBreaks = []
        domHtml.find('.mce-pagebreak').each(function () {
          pageBreaks[pageBreaks.length] = $(this).offset().top
        })
        pageBreaks.sort()

        // if pageBreak is too close next page, then ignore it

        if (lastPageBreaks === pageBreaks) {
          return // no change
        }
        lastPageBreaks = pageBreaks

        console.log('Redraw ruler')

        var s = ''
        s += '<svg width="100%" height="' + imgH + '" xmlns="http://www.w3.org/2000/svg">'

        s += '<style>'
        s += '.pageNumber{font-weight:bold;font-size:19px;font-family:verdana;text-shadow:1px 1px 1px rgba(0,0,0,.6);}'
        s += '</style>'

        var pages = Math.ceil(imgH / a4px)

        var i, j, curY = startMargin
        for (i = 0; i < pages; i++) {
          var blockH = a4px

          var isPageBreak = 0
          for (var j = 0; j < pageBreaks.length; j++) {
            if (pageBreaks[j] < curY + blockH) {
              // musime zmensit velikost stranky
              blockH = pageBreaks[j] - curY
              // pagebreak prijde na konec stranky
              isPageBreak = 1
              pageBreaks.splice(j, 1)
            }
          }

          s += '<line x1="0" y1="' + curY + '" x2="100%" y2="' + curY + '" stroke-width="1" stroke="red"/>'

          // zacneme pravitko
          s += '<pattern id="ruler' + i + '" x="0" y="' + curY + '" width="37.79527559055118" height="37.79527559055118" patternUnits="userSpaceOnUse">'
          s += '<line x1="0" y1="0" x2="100%" y2="0" stroke-width="1" stroke="black"/>'
          s += '</pattern>'
          s += '<rect x="0" y="' + curY + '" width="100%" height="' + blockH + '" fill="url(#ruler' + i + ')" />'

          // napiseme cislo strany
          s += '<text x="10" y="' + (curY + 19 + 5) + '" class="pageNumber" fill="#ffffff">' + (i + 1) + '.</text>'

          curY += blockH
          if (isPageBreak) {
            // s+= '<rect x="0" y="'+curY+'" width="100%" height="'+pageBreakHeight+'" fill="#FFFFFF" />';
            curY += pageBreakHeight
          }
        }

        s += '</svg>'

        domHtml.css('background-image', 'url("data:image/svg+xml;utf8,' + encodeURIComponent(s) + '")')
      }
      editor.on('NodeChange', refreshRuler)
      editor.on('init', refreshRuler)
    })
  }

  Plugin()
}())
