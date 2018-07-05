
var popuplogin = document.getElementById('SIB');

window.onclick = function(event) {
    if (event.target == popuplogin) {
        popuplogin.style.display = "none";
    }
}


var popupregister = document.getElementById('RIB');

window.onclick = function(event) {

    if (event.target == popupregister) {
        popupregister.style.display = "none";
    }
}

var check = function() {
    if (document.getElementById("pwd1").value == document.getElementById("pwd2").value) {
        document.getElementById('message').style.color = 'green';
        document.getElementById('message').innerHTML = 'matching';
        document.getElementById('RC').disabled = false;
    } else {
        document.getElementById('message').style.color = 'red';
        document.getElementById('message').innerHTML = 'Must match the previous entry';
        document.getElementById('RC').disabled = true;
    }
}
