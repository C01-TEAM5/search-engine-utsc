
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

document.getElementById("RIB").addEventListener("submit", function(e){
    e.preventDefault();
});
document.getElementById("SIB").addEventListener("submit", function(e){
    e.preventDefault();
});

document.getElementById("SC").addEventListener("click", function(){

    var email = document.getElementById("username").value;
    var password = document.getElementById("pwd").value;

    document.getElementById("username").value = '';
    document.getElementById("pwd").value = '';
    
    api.signin(email, password, function(err, result) {
        if (err != null) {
            console.log(err);
        }
        else {
            alert(result);
        }
    });


    document.getElementById('RIB').style.display='none';
});



document.getElementById("RC").addEventListener("click", function(){

    var username = document.getElementById("username").value;
    var name = document.getElementById("name").value;
    var email = document.getElementById("email").value;
    var password = document.getElementById("pwd1").value;

    document.getElementById("username").value = '';
    document.getElementById("name").value = '';
    document.getElementById("email").value = '';
    document.getElementById("pwd1").value = '';
    document.getElementById("pwd2").value = '';

    api.register(username, name, email, password, function(err, result) {
        if (err != null) {
            console.log(err);
        }
        else {
            alert(result);
        }
    });


    document.getElementById('RIB').style.display='none';
});

var check = function() {
    if (document.getElementById("pwd1").value == document.getElementById("pwd2").value) {
        document.getElementById('message').style.color = 'green';
        document.getElementById('message').innerHTML = 'passwords match';
        document.getElementById('RC').disabled = false;
    } else {
        document.getElementById('message').style.color = 'red';
        document.getElementById('message').innerHTML = 'The provided passwords do not match';
        document.getElementById('RC').disabled = true;
    }
}
