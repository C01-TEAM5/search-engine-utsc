<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset= "UTF-8">
        <title>Verify Email</title>
        <!-- <link rel= "stylesheet" href= "./css/main.css"> -->
        <link href="https://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet"> 
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="../lib/semantic/semantic.min.css">
        <link rel="stylesheet" href="./css/reset.css" type="text/css">
        <link rel="stylesheet" href="../css/index.css" type="text/css">
        <link rel= "stylesheet" href= "../css/login.css" type="text/css">
        <link rel= "stylesheet" href= "../css/course.css" type="text/css">

    </head>
    <body>
        <div id="search-background"></div>

        <div class="header">
            <a href="/"><img src="./media/utscblue-300x132.png" /></a>
            <div class = "header-choices">
                <a class = "btn notSignedIn loginButton"><i class="user outline icon"></i><span class="tooltiptext">Login</span></a>

                <a class = "btn notSignedIn registerButton"><i class="edit outline icon"></i><span class="tooltiptext">Register</span></a>
                
                <div class="signedIn header-info"><span class="userName"></span></div>
                <c:if test='${loggedIn == true}'>
                    <div class="btn ui dropdown">
                        <i class="world icon"><i class="dropdown icon"></i></i>
                        <c:if test='${hasNew == true}'>
                            <div class="floating ui red label notification-status"></div>
                        </c:if>
                        <div class="menu">

                            <c:forEach items="${notifications}" var="res">
                                <div class="notification-item item" id="${res.id}">
                                    <div class="notification-msg">
                                        <c:if test="${res.isOpened() == false}">
                                            <div class="floating ui red label notification-status"></div>
                                        </c:if>
                                        ${res.msg}
                                    </div> 
                                    <div class="notification-item-buttons">
                                        <a class="btn notification-link" id="${res.id}-link" href="${res.link}"><i class="linkify icon"></i></a>
                                        <button class="btn notification-delete" id="${res.id}-delete"><i class="trash alternate outline icon"></i></button>
                                    </div>
                                </div>
                            </c:forEach>
                            <div class="notification-item item">
                                <div class="notification-msg">
                                    - End of notifications -
                                </div> 
                            </div>
                        </div>
                    </div>
                </c:if>
                <a class = "btn help helpButton" href="/help">
                        <i class="question circle outline icon"></i><span class="tooltiptext">Help</span></a>
                <a class="btn signedIn" id="uploadButton" href="/upload"><i class="upload icon"></i><span class="tooltiptext">Upload</span></a>
                <a class="btn signedIn" id="profileButton" href="/profile"><i class="user outline icon"></i><span class="tooltiptext">Profile</span></a>
                <a class="btn signedIn" id="logoutButton"><i class="sign out alternate icon"></i><span class="tooltiptext">Logout</span></a>
            </div>
    
                <!-- the sign in pop up window -->
            <div id="SIB" class="popup-login">
              <form class="login-content" method ="POST" enctype = "multipart/form-data">
                <div class="container-logins">
                    <div class="login-content-item">
                        <div class="label"><i class="user alternative icon"></i>Username </div>
                        <input class="form-input" id="s-username" type="text" placeholder="Enter Username" name="loginuname" required>
                    </div>
                    <div class="login-content-item">
                        <div class="label"><i class="lock icon"></i>Password </div>
                        <input class="form-input" id="s-pwd" type="password" placeholder="Enter Password" name="loginpsw" required>
                    </div>

                    <div class="btn-container">
                        <button id = "SC" class = "loginButton" type="submit">Login</button>

                        <button type="button"
                        onclick="document.getElementById('SIB').style.display='none'"
                        class="cancelbtn">Cancel</button>
                    </div>
                </div>
              </form>
            </div>


            <!-- the register pop up window -->
            <div id="RIB" class="popup-login">
                <form class="login-content register-content" method ="POST" enctype = "multipart/form-data">
                    <div class="container-logins">
                        <div class = "register-info">
                            <div class="login-content-item">
                                <div class="label"><i class="envelope icon"></i>Email </div>
                                <input class="form-input" id="r-email" type="email" placeholder="Enter Email" name="registeruemail" required>
                            </div>
                            <div class="login-content-item">
                                <div class="label"><i class="user icon"></i>Name </div>
                                <input class="form-input"  id="r-name" type="text" placeholder="Enter name" name="registername" required>
                            </div>
                            <div class="login-content-item">
                                <div class="label"><i class="user icon"></i>Username </div>
                                <input class="form-input" id="r-username" type="text" placeholder="Enter username" name="registerusername" required>
                            </div>
                            <div class="login-content-item">
                                <div class="label"><i class="lock icon"></i>Password </div>
                                <input class="form-input" id = "r-pwd1" type="password" placeholder="Enter Password" name="registerpsw" required>      
                            </div>
                            <div class="login-content-item">
                                <div class="label"><i class="lock icon"></i>Confirm Password </div>
                                <input class="form-input" id = "r-pwd2" type="password" placeholder="Confirm Password" name="confirmpsw" required>
                            </div>
                            <span id = "message"></span><br>
                            <div class="registerchoices">
                                <button type = "button" class="registertypebutton" id="permission-instructor">Create Instructor Account</button>
                                <button type = "button" class="registertypebutton active-permission" id="permission-student">Create Student Account</button>
                            </div>

                            <div class="btn-container">
                                <button id ="RC" class = "loginButton" type="submit" >Register</button>

                                <button type="button"
                                onclick="document.getElementById('RIB').style.display='none'"
                                class="cancelbtn">Cancel</button>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        
        <div class="ui segments">
            <div class="ui green segment">
                <p>${msg}</p>
            </div>
        </div>

        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
        <script src="../lib/semantic/semantic.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
        <script src="../js/api.js"></script>
        <script src="../js/login.js"></script>
    </body>
</html>