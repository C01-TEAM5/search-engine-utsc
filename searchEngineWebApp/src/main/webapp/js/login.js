(function(){
    "use strict";

    var permission = 2;

    var load = function() {
        
        setNotSignedIn();
        if (api.getCurrentUser() == null || api.getCurrentUser() === "") {
            setNotSignedIn();
        }
        else {
            console.log("setting signed in");
            setSignedIn();
        }

        var popuplogin = document.getElementById('SIB');
        var popupregister = document.getElementById('RIB');

        window.onclick = function(event) {
            if (event.target == popuplogin || event.target == popupregister) {
                popupregister.style.display = "none";
                popuplogin.style.display = "none";
            }
        }

        $(".registertypebutton").click(function() {
            $(".registertypebutton").removeClass("active-permission");
            $(this).addClass("active-permission");
        });

        $("#permission-student").click(function() {
            permission = 2;
        });

        $("#permission-instructor").click(function() {
            permission = 3;
        });

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
            swal({
                title: "Processing!",
                text: "Please wait.",
                icon: "../media/icons/loading.gif",
                buttons: false
            });
            api.signin(email, password, function(err, result) {
                if (err != null) {   //get error when login
                    //display for register and login
                    // document.getElementById("loginButton").style.display = "inline-block";
                    // document.getElementById("registerButton").style.display = "inline-block";
                    // //display==none logout
                    // document.getElementById("logoutButton").style.display = "none";
                    setNotSignedIn();
                    console.log(err);
                    swal.close();
                    swal("Error!", "Failed to login. Make sure you have verified your email.", "error");
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
                    swal.close();
                    location.reload(); 
                }
            });
            
            // clears entries after
        });

        document.getElementById("RC").addEventListener("click", function(){

            var username = document.getElementById("r-username").value;
            var name = document.getElementById("r-name").value;
            var email = document.getElementById("r-email").value;
            var password = document.getElementById("r-pwd1").value;

            swal({
                title: "Processing!",
                text: "Please wait.",
                icon: "../media/icons/loading.gif",
                buttons: false
            });
            api.register(username, name, email, password, permission, function(err, result) {
                if (err != null) {  //get error when register
                    //display for register and login
                    // document.getElementById("loginButton").style.display = "inline-block";
                    // document.getElementById("RegisterButton").style.display = "inline-block";
                    // //display==none logout
                    // document.getElementById("logoutButton").style.display = "none";
                    setNotSignedIn();
                    console.log(err);
                    swal.close();
                    swal("Error!", "Unable to register. Please try again." + err, "error");
                }
                else { //success register 
                    //display==none for register and login
                    // document.getElementById("loginButton").style.display = "none";
                    // document.getElementById("RegisterButton").style.display = "none";
                    // //display logout
                    // document.getElementById("logoutButton").style.display = "inline-block";
                    //setSignedIn();
                    setNotSignedIn();
                    swal.close();
                    swal("Success!", "Please check your email for the verification email.", "success");
                    popupregister.style.display = "none";
                    //location.reload(); 
                }
            });
            
            // clears entries after 
            document.getElementById("r-username").value = '';
            document.getElementById("r-name").value = '';
            document.getElementById("r-email").value = '';
            document.getElementById("r-pwd1").value = '';
            document.getElementById("r-pwd2").value = '';


            document.getElementById('RIB').style.display='none';
        });

        document.getElementById("logoutButton").addEventListener("click", function(){
            
            console.log(1);
            api.signout(function(err, result){	
                if (err) {
                    console.log(err);
                }
                else {
                    setNotSignedIn();
                    location.reload(); 
                }
            });
            console.log(2);
        });

        document.getElementById("r-pwd1").addEventListener("keyup", check);
        document.getElementById("r-pwd2").addEventListener("keyup", check);


        parseURL();
        
    }

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

    var setSignedIn = function() {
        $(".userName").html(api.getCurrentUser().replace(/"/g, ''));
        $(".signedIn").css("display", "inline-block");
        $(".notSignedIn").css("display", "none");
    }

    var setNotSignedIn = function() {
        $(".userName").html("");
        $(".notSignedIn").css("display", "inline-block");
        $(".signedIn").css("display", "none");
    }

    var showLogin = function() {
        document.getElementById('SIB').style.display='flex';
    }

    var showRegister = function() {
        document.getElementById('RIB').style.display='flex';
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
