<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <!--link rel="stylesheet" href="./css/main.css" type="text/css"-->
        <link href="https://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="../lib/semantic/semantic.min.css">
        <link rel="stylesheet" href="./css/index.css" type="text/css">
        <link rel= "stylesheet" href= "./css/login.css" type="text/css">
        <link rel= "stylesheet" href= "./css/file.css" type="text/css">
        <title>UTSC Document Search - File</title>
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
                <a class = "btn help helpButton" onClick="location.href='faq.html'">
                        <i class="question circle outline icon"></i><span class="tooltiptext">Help</span></a>
                <a class="btn signedIn" id="uploadButton" href="/upload"><i class="upload icon"></i><span class="tooltiptext">Upload</span></a>
                <a class="btn signedIn" id="profileButton" href="/profile"><i class="user outline icon"></i><span class="tooltiptext">Profile</span></a>
                <a class="btn signedIn" id="logoutButton"><i class="sign out alternate icon"></i><span class="tooltiptext">Logout</span></a>
            </div>
    
            <!-- the sign in pop up window -->
            <div id="SIB" class="popup-login">
                <form class="login-content" method ="POST" enctype = "multipart/form-data">
                <div class="container-logins">
                    <label for="username">Username</label>
                    <input id="s-username" type="text" placeholder="Enter Username" name="loginuname" required>

                    <label for="psw">Password</label>
                    <input id="s-pwd" type="password" placeholder="Enter Password" name="loginpsw" required>

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
                <form class="login-content" method ="POST" enctype = "multipart/form-data">
                <div class="container-logins">
                    <div class="row">
                        <div class = "register-type col-sm-5">
                            <section class="typeinfo"> toggle register type info</section>
                            <div class="registerchoices">
                                <button type = "button" class="registertypebutton" id="permission-instructor">Create Instructor Account</button>
                                <button type = "button" class="registertypebutton active-permission" id="permission-student">Create Student Account</button>
                            </div>
                        </div>

                        <div class = "register-info col-sm-7">
                            <label for="uemail">User Email</label>
                            <input id="r-email" type="email" placeholder="Enter Email" name="registeruemail" required>

                            <label for="uname">Name</label>
                            <input id="r-name" type="text" placeholder="Enter name" name="registername" required>

                            <label for="username">Username</label>
                            <input id="r-username" type="text" placeholder="Enter username" name="registerusername" required>

                            <label for="userpsw">Password</label>
                            <input id = "r-pwd1" type="password" placeholder="Enter Password" name="registerpsw" required>

                            <label for="confirmpsw">Confirm Password</label>
                            <input id = "r-pwd2" type="password" placeholder="Confirm Password" name="confirmpsw" required>
                            <span id = "message"></span><br>

                            <div class="btn-container">
                                <button id ="RC" class = "loginButton" type="submit" >Register</button>

                                <button type="button"
                                onclick="document.getElementById('RIB').style.display='none'"
                                class="cancelbtn">Cancel</button>
                            </div>
                        </div>
                    </div>
                </div>
                </form>
            </div>
    
            </div>
        </div>
        <div class="file-container">
            <div class="file-container-item">
                <div class="file-info ui raised segment">
                    <div class="buttons-container">
                        <a class="ui blue button" href="${path}" download><i class="download icon"></i>Download</a>
                        <button class="ui blue button isOwner" id="remove-file-button"><i class="trash icon"></i>Delete</a>
                    </div>
                    <a class="ui teal label" href="/profile?id=${owner}">
                        <i class="user icon"></i>Owned by: ${owner}
                    </a>
                    <div class="file-info-edit-item ui transparent input" id="file-name">
                        <input placeholder="File Name..." type="text" value="${fileName}" id="file-name-input" disabled>
                        <button class="ui icon button isOwner" id="edit-file-name">
                            <i class="edit icon"></i>
                        </button>
                    </div>
                    <div class="file-info-edit-item file-info-edit-item-smaller ui transparent input" id="courseId">
                        <input placeholder="Course code..." type="text" value="${courseId}" id="courseId-input" disabled>
                        <button class="ui icon button isOwner" id="edit-courseId">
                            <i class="edit icon"></i>
                        </button>
                        <a class="ui icon teal button" href="/course?id=${courseId}">
                            <i class="linkify icon"></i>
                        </a>
                    </div>
                    <select name="gender" class="ui dropdown disabled" id="file-permissions">
                        <option value="">Permission</option>
                        <c:if test="${permission == 3}">
                            <option value="0">All</option>
                            <option value="2">Student</option>
                            <option value="3" selected>Instructor</option>
                        </c:if> 
                        <c:if test="${permission == 2}">
                            <option value="0">All</option>
                            <option value="2" selected>Student</option>
                            <option value="3">Instructor</option>
                        </c:if> 
                        <c:if test="${permission == 0}">
                            <option value="0" selected>All</option>
                            <option value="2">Student</option>
                            <option value="3">Instructor</option>
                        </c:if>
                    </select>
                    <button class="ui blue button isOwner" id="save-file-info"><i class="save icon"></i>Save</a>
                </div>
            </div>
            <div class="file-container-item ui raised segment">
                <div class="ui red ribbon label">Preview</div>
                <c:if test='${fileType.equals("docx")}'>
                    <iframe src="https://docs.google.com/gview?url=${path}&embedded=true"
                    class="file-preview">Preview Unavailable, please download file to view.</iframe>
                </c:if>
                <c:if test='${!fileType.equals("docx")}'>
                    <object data="${path}" type="text/html" class="file-preview">Preview Unavailable, please download file to view.
                    </object>
                </c:if>
                
            </div>
        </div>

        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
        <script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
        <script src="../lib/semantic/semantic.min.js"></script>
        <script src="../js/api.js"></script>
        <script src="../js/file.js"></script>
        <script src="../js/login.js"></script>
    </body>
</html>
