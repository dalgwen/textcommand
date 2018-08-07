<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

  <head>
    <title>Text Command</title>
    <meta content="text/html; charset=UTF-8" http-equiv="Content-Type"/>
  </head>

<script>

function sendCommand() {
    xhttp = new XMLHttpRequest();
    xhttp.open("POST", "/", true);
    xhttp.onload = function() {
        document.getElementById("response").textContent = xhttp.response;
    };
    xhttp.send(document.getElementById("command").value);
}

</script>

  <body>
  
    <p>Enter command : <input id="command"></input>
    <button onClick="sendCommand()">Send</button>
    </p>
    <p id="response"></p>

  </body>

</html>