
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


document.getElementById("RC").addEventListener("click", function(){
    var username = document.getElementById("username").value;
    var name = document.getElementById("name").value;
    var email = document.getElementById("email").value;
    var password = document.getElementById("pwd1").value;
    console.log("success", username, name, email, password);
    api.register(username, name, email, password, function(err, result) {
        if (err != null) {
            console.log(err);
        }
        else {
            console.log(result);
        }
    });
});