(function(){
    "use strict";

    var load = function() {
        setNotSignedIn();
        if (api.getCurrentUser() == null || api.getCurrentUser() === "") {
            setNotSignedIn();
        }
        else {
            setSignedIn();
        }

        var popuplogin = document.getElementById('SIB');
        var popupregister = document.getElementById('RIB');

        window.onclick = function(event) {
            if (event.target == popuplogin || event.target == popupregister) {
                popupregister.style.display = "none";
                popuplogin.style.display = "none";
                history.pushState(null, '', '/');
            }
        }

        // handle login and register buttons
        $(".registerButton").click(function(){
            console.log(document.URL);
            showRegister();
        });

        $(".loginButton").click(function(){
            console.log(document.URL);
            showLogin();
        });

        document.getElementById("RIB").addEventListener("submit", function(e){
            e.preventDefault();
        });
        document.getElementById("SIB").addEventListener("submit", function(e){
            e.preventDefault();
        });

        document.getElementById("SC").addEventListener("click", function(){

            var email = document.getElementById("s-username").value;
            var password = document.getElementById("s-pwd").value;
            
            api.signin(email, password, function(err, result) {
                if (err != null) {   //get error when login
                    //display for register and login
                    // document.getElementById("loginButton").style.display = "inline-block";
                    // document.getElementById("registerButton").style.display = "inline-block";
                    // //display==none logout
                    // document.getElementById("logoutButton").style.display = "none";
                    setNotSignedIn();
                    console.log(err);
                }
                else {  //success when login 
                    //display==none for register and login
                    // document.getElementById("loginButton").style.display = "none";
                    // document.getElementById("registerButton").style.display = "none";
                    // //display logout
                    // document.getElementById("logoutButton").style.display = "inline-block";
                    setSignedIn();
                    document.getElementById("s-username").value = "";
                    document.getElementById("s-pwd").value = "";
                    document.getElementById("SIB").style.display = "none";
                    history.pushState(null, '', '/');
                }
            });
            
            // clears entries after
        });



        document.getElementById("RC").addEventListener("click", function(){

            var username = document.getElementById("r-username").value;
            var name = document.getElementById("r-name").value;
            var email = document.getElementById("r-email").value;
            var password = document.getElementById("r-pwd1").value;

            api.register(username, name, email, password, function(err, result) {
                if (err != null) {  //get error when register
                    //display for register and login
                    // document.getElementById("loginButton").style.display = "inline-block";
                    // document.getElementById("RegisterButton").style.display = "inline-block";
                    // //display==none logout
                    // document.getElementById("logoutButton").style.display = "none";
                    setNotSignedIn();
                    console.log(err);
                }
                else { //success register 
                    //display==none for register and login
                    // document.getElementById("loginButton").style.display = "none";
                    // document.getElementById("RegisterButton").style.display = "none";
                    // //display logout
                    // document.getElementById("logoutButton").style.display = "inline-block";
                    setSignedIn();
                }
            });
            
            // clears entries after 
            document.getElementById("r-username").value = '';
            document.getElementById("r-name").value = '';
            document.getElementById("r-email").value = '';
            document.getElementById("r-pwd1").value = '';
            document.getElementById("r-pwd2").value = '';


            document.getElementById('RIB').style.display='none';
            history.pushState(null, '', '/');
        });

        document.getElementById("logoutButton").addEventListener("click", function(){
            
            console.log(1);
            api.signout(function(err, result){	
                if (err) {
                    console.log(err);
                }
                else {
                    setNotSignedIn();
                }
            });
            console.log(2);
        });



        var check = function() {
            if (document.getElementById("r-pwd1").value == document.getElementById("r-pwd2").value) {
                document.getElementById('message').style.color = 'green';
                document.getElementById('message').innerHTML = 'Passwords match';
                document.getElementById('RC').disabled = false;
            } else {
                document.getElementById('message').style.color = 'red';
                document.getElementById('message').innerHTML = 'The provided passwords do not match';
                document.getElementById('RC').disabled = true;
            }
        }
        parseURL();
    }

    var setSignedIn = function() {
        $(".signedIn").css("display", "inline-block");
        $(".notSignedIn").css("display", "none");
    }

    var setNotSignedIn = function() {
        $(".notSignedIn").css("display", "inline-block");
        $(".signedIn").css("display", "none");
    }

    var showLogin = function() {
        history.pushState(null, '', '?login');
        document.getElementById('SIB').style.display='block';
    }

    var showRegister = function() {
        history.pushState(null, '', '?register');
        document.getElementById('RIB').style.display='block';
    }

    var parseURL =  function() {
        var urlParts = document.URL.split("?");
        if(urlParts.length > 1) {
            if (urlParts[1] === "login") showLogin();
            if (urlParts[1] === "register") showRegister();
        }
    }

    load();

}());
