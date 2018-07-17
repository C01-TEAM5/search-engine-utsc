<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!--  learned pagination from:
https://stackoverflow.com/questions/31410007/how-to-do-pagination-in-jsp -->

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <!--link rel="stylesheet" href="./css/main.css" type="text/css"-->
    <link href="https://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="../lib/semantic/semantic.min.css">
    <link rel="stylesheet" href="./css/index.css" type="text/css">
    <link rel= "stylesheet" href= "./css/login.css" type="text/css">
    <link rel="stylesheet" href="./css/results.css" type="text/css">
    <link rel="stylesheet" href="./css/profile.css" type="text/css">

    <title>UTSC Document Search - Profile</title>
</head>
<body>
    <div id="search-background"></div>

    <div class="header">
        <a href="/"><img src="./media/utscblue-300x132.png" /></a>
        <div class= "header-choices">

            <!-- sign in/out & register button -->
            <div class = "header-choices">
                <button class = "btn notSignedIn loginButton">
                Login</button>

                <button class = "btn notSignedIn registerButton">
                Register</button>

                <a class="btn signedIn" id="uploadButton" href="/upload"><i class="upload icon"></i>Upload</a>
                <a class="btn signedIn" id="profileButton" href="/profile">Profile</a>
                <button class="btn signedIn" id="logoutButton">Logout</button>
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
                                <input id = "r-pwd1" onkeyup="check()" type="password" placeholder="Enter Password" name="registerpsw" required>

                                <label for="confirmpsw">Confirm Password</label>
                                <input id = "r-pwd2" onkeyup="check()" type="password" placeholder="Confirm Password" name="confirmpsw" required>
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

    <div class="profile-container">
        <div class="profile-info profile-container-item">
            <c:if test="${permission == 3}">
                <div class="ui blue horizontal label">Instructor</div>
            </c:if> 
            <c:if test="${permission == 2}">
                <div class="ui red horizontal label">Student</div>
            </c:if> 
            <c:if test="${permission == 1}">
                <div class="ui green horizontal label">Teaching Assistant</div>
            </c:if> 
            
            <div class="card ui raised segment">
                
                <div class="profile-img">
                    <div class="edit-image-container">
                        <i class="edit icon"></i>
                    </div>
                    <img class="ui medium circular image profile-image" src="http://www.zimphysio.org.zw/wp-content/uploads/2018/01/default-avatar-2.jpg">
                </div>
                <div class="user-info">
                    <div class="ui transparent input" id="input-username">
                        <input placeholder="Name..." type="text" value="${name}" id="username" disabled>
                        <button class="ui icon button signedIn" id="edit-username">
                            <i class="edit icon"></i>
                        </button>
                    </div>
                    <div class="courses">
                        <c:forEach var="result" items="${courses}">
                            <a class="ui teal horizontal label" href="/course?id=${result}"><c:out value="${fn:toUpperCase(result)}"/></a>
                        </c:forEach>
                    </div>
                    <div class="ui transparent input" id="input-desc">
                        <textarea id="userDesc" disabled>${desc}</textarea>
                        <button class="ui icon button signedIn" id="edit-desc">
                            <i class="edit icon"></i>
                        </button>
                    </div>
                    <button class="circular ui icon button teal signedIn" id="save-profile-button"><i class="save icon"></i></button>
                </div>
            </div>
            <h3 class="ui header segment bestfit">
                <i class="file icon"></i>
                Number of Files: <span class="numOfFiles"> ${numOfFiles}</span>
            </h3>
            <c:if test="${permission == 3}">
                <div class="ui raised segment signedIn">
                    <h3>Create a course</h3>
                    <hr>
                    <div class="ui action input">
                        <input placeholder="Course Id..." type="text" id="course-add-input">
                        <button class="ui button" id="course-add">Create Course</button>
                    </div>
                </div>
            </c:if> 
            
            <div class="stats">

            </div>
        </div>
        <div class="user-files profile-container-item">
            <div class="card ui raised segment">
                <h3>
                    <i class="file icon"></i>
                    Files
                </h3>
                <hr>
                <c:forEach var="result" items="${files}">
                    <div class="ui raised segment search-results-item">
                        <i class="file alternate outline icon"></i>
                        <div class="search-results-item-info">
                            <div class="file-name">
                                <a href="/file?id=${result.id}"><c:out value="${result.title}"/></a>
                                <div class="ui teal horizontal label"><c:out value="${result.fileType}"/></div>
                            </div>
                            <div class="file-course">
                                Course Code: 
                                <c:if test="${result.courseCode.length() > 0}">
                                    <a class="ui blue horizontal label" href="/course?id=${result.courseCode}"><c:out value="${fn:toUpperCase(result.courseCode)}"/></a>
                                </c:if> 
                                <c:if test="${result.courseCode.length() == 0}">
                                    <div class="ui red horizontal label">NONE</div>
                                </c:if> 
                                
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
    <script src="../lib/semantic/semantic.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
    <script src="js/api.js"></script>
    <script src="./js/login.js"></script>
    <script src="./js/profile.js"></script>
</body>
</html>
