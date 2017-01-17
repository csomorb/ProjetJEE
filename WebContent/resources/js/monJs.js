function resizeTextArea(textarea){
    var hiddenDiv = $('.hiddendiv').first();
    if (hiddenDiv.length) {
          hiddenDiv.css('width', textarea.width());
          textarea.css('height', hiddenDiv.height());
    }
};