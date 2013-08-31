$(document).ready(function() {
  var cur = 0;
  var max = 2;

  function change() {
    $('#background').fadeOut(1000, function() {
      cur = (cur + 1) % max;
      $(this).css("background-image", "url('/assets/images/background-" + cur + ".jpg')");
      $(this).fadeIn(1000);
    });
    setTimeout(change, 15000);
  }

  setTimeout(change, 10000);

  $("#content").accordion({
    collapsible : true,
    heightStyle : "fill",
    icons : null,
    active : false
  });
  
  $(window).resize(function() {
    $("#content").accordion('refresh');
  });
});