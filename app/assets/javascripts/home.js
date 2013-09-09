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

  //setTimeout(change, 10000);

  function guestpost() {
    var name = $("#guestname").val();
    var message = $("#guestmessage").val();
    if (name.length > 0 && message.length > 0) {
      var id = (new Date()).getTime();
      var data = {
          id : id,
          name : name,
          message : message
      }
      $.ajax({
        type    :   'POST',
        datatype:   'html',
        url     :   '/guestbook/add',
        data    :   data,    
        success :   function(d) {
          $("#guestname").val("");
          $("#guestmessage").val("");
          $("#guestbook").html(d);
        }
      });
    }
  }
  
  $("#guestpost").on("click", function(e) {
    guestpost();
  });
  
  function guestlist() {
    $.ajax({
      type    :   'GET',
      datatype:   'html',
      url     :   '/guestbook/list',
      success :   function(d) {
        $("#guestbook").html(d);
      }
    });
  }
  
  guestlist();
});