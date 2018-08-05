<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset= "UTF-8">
        <title>Course page: ${courseID}</title>
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
        <!-- <div class="content">
            <div class="course-info">
                <div class="text-item">Course ID: <span id="courseID">${courseID}</span></div>
                <div class="text-item">${courseName}</div>
                <div class="text-desc">${courseDesc}</div>
                <div class="separator"></div>
                <div class="text-item">Instructors: <span id="instructors"></div>
                <div class="text-item">Enrollment number: ${numStudentsEnrolled}</div>
                <div class="separator"></div>
                <button class="btn" id="showStudents">Show Students</button>
                <button class="btn" id="showFiles">Show Course Files</button>
                <div class="separator"></div>
                <div class="course-info-item instructorOnly">
                    <input id="studentUsername" type="text" placeholder="Username">
                    <button class="btn" id="addStudent">Add Student</button>
                </div>
            </div>
            <div class="course-content">
                <div class="course-content-item item-active" id="student-list">
                    <div class="text-item">Course Students</div>
                    <div class="student">
                        <div class="studnet-username"><a href="/profile?id=kalindu">kalindu</a></div>
                        <button class="btn">Remove Student</button>
                    </div>
                </div>
                <div class="course-content-item" id="file-list">
                    <div class="text-item">Course Files</div>
                    <div class="file">
                        <div class="filename">filename.pdf</div>
                        <button class="btn">Download File</button>
                    </div>
                </div>
            </div>
        </div> -->
        <div class="course-content">
            <div class="icon-bar">
                <div class="bar-item filler"></div>
                <div class="bar-item bar-active" id="course-home"><i class="home icon"></i>
                    <div class="ui left pointing label"> Course Home </div>
                </div>
                <div class="bar-item" id="course-students"><i class="users icon"></i>
                    <div class="ui left pointing label"> Course Students </div></div>
                <div class="bar-item" id="course-files"><i class="file alternate icon"></i>
                    <div class="ui left pointing label"> Course files </div></div>
                <div class="bar-item" id="course-config"><i class="cog icon"></i>
                    <div class="ui left pointing label"> Configure course </div></div>
            </div>
            <div class="content-info">
                <h1 class="ui header segment">
                    <i class="book icon"></i><span class="courseID">${courseID}</span> : <span class="courseName">${courseName}</span>
                </h1>
                
                
                <div class="content-info-item content-info-item-active" id="course-home-info">
                    <div class="content-info-segment">
                        <div class="ui raised segment course-info-segment" >
                            <h3 class="ui header course-info-title">Description</h3>
                            <p class="courseDesc">${courseDesc}</p>
                        </div>
                        <h3 class="ui header segment">
                            <i class="user circle icon"></i>
                            Enrollment Size: <span class="courseSize"> ${numStudentsEnrolled}</span>
                        </h3>
                    </div>
                    <div class="ui content-info-segment">
                        <div class="ui raised segment" id="instructors-list">
                            <h3 class="">Instructors</h3>
                            <hr>
                            <div class="custom-list">
                                
                                <!-- <div class="ui raised segment">
                                    <div class="ui list">
                                        <div class="item">
                                            <i class="user icon"></i>
                                            <div class="content">
                                                <a href="/profile?id=bob">Crazy Bob</a>
                                                <button class="ui button">Remove</button>
                                            </div>
                                        </div>
                                    </div>
                                </div> -->
                                
                            </div>
                        </div>
                        
                        <div class="ui raised segment" id="tas-list">
                            <h3 class="">Teaching Assistants</h3>
                            <hr>
                            <div class="custom-list">

                                <!-- <div class="ui raised segment">
                                    <div class="ui list">
                                        <div class="item">
                                            <i class="user icon"></i>
                                            <div class="content">
                                                <a href="/profile?id=bob">Crazy Bob</a>
                                                <button class="ui button">Remove</button>
                                            </div>
                                        </div>
                                    </div>
                                </div> -->
                                
                            </div>
                        </div>
                    </div>
                </div>
                <div class="content-info-item" id="course-students-info">
                    <div class="ui content-info-segment">
                        <div class="ui raised segment" id="students-list">
                            <h3 class="">Students</h3>
                            <hr>
                            <div class="custom-list">
                                                                
                            </div>
                        </div>
                    </div>
                    <div class="content-info-segment">
                        <!-- <div class="ui raised segment segment-info">
                            <h3 class="student-name">Crazy Bob</h3>
                            <hr>
                            <div class="custom-list">
                                
                            </div>
                        </div> -->
                    </div>
                </div>
                <div class="content-info-item" id="course-files-info">
                    <div class="content-info-segment">
                        <h3 class="ui header segment">
                            <i class="file icon"></i>
                            Number of Files: <span class="courseNumSize"> </span>
                        </h3>
                    </div>
                    <div class="ui content-info-segment">
                        <div class="ui raised segment" id="files-list">
                            <h3 class="">Files</h3>
                            <hr>
                            <div class="custom-list">
                                
                                <!-- <div class="ui raised segment">
                                    <div class="ui list">
                                        <div class="item">
                                            <i class="file icon"></i>
                                            <div class="content">
                                                <a href="/file?id=bob">Crazy Bob</a>
                                                <button class="ui button remove-file-button">Remove</button>
                                            </div>
                                        </div>
                                    </div>
                                </div> -->
                                
                            </div>
                        </div>
                    </div>
                </div>
                <div class="content-info-item" id="course-config-info">
                    <div class="content-info-segment">
                        <div class="ui raised segment">
                            <h3>Add Instructor</h3>
                            <hr>
                            <div class="ui action input">
                                <input placeholder="Username..." type="text" id="course-add-instructor-input">
                                <button class="ui button" id="course-add-instructor">Add Instructor</button>
                            </div>
                        </div>
                        <div class="ui raised segment">
                            <h3>Add Student</h3>
                            <hr>
                            <div class="ui action input">
                                <input placeholder="Username..." type="text" id="course-add-student-input">
                                <button class="ui button" id="course-add-student">Add Student</button>
                            </div>
                        </div>
                        <div class="ui raised segment">
                            <h3>Upload file</h3>
                            <hr>
                            <div class="ui action input">

                                <label for="courses-upload" class="ui icon button">
                                    <i class="file icon"></i>
                                    Open File
                                </label>
                                <form class="" enctype = "multipart/form-data" id="couse-file-upload-form">
                                    <input type="file" id="courses-upload" style="display:none">
                                </form>
                                <input placeholder="File..." type="text" id="courses-upload-text" disabled multiple>
                                <button class="ui button" id="courses-upload-btn">Upload</button>
                            </div>
                        </div>
                    </div>
                    <div class="content-info-segment">
                        <div class="ui stacked segment">
                            <h3>Course Information</h3>
                            <hr>
                            <div class="ui raised segment course-config-segment">
                                <div class="ui action input">
                                    <div class="ui blue right pointing label">
                                        Course code
                                    </div>
                                    <input id="newCode" placeholder="${courseID}" type="text" value="${courseID}" required>
                                </div>
                                <div class="ui action input">
                                    <div class="ui blue right pointing label">
                                        Course name
                                    </div>
                                    <input id="newName" placeholder="${courseName}" type="text" value="${courseName}" required>
                                </div>
                                <div class="ui form segment">
                                    <div class="field">
                                        <div class="ui blue ribbon label">
                                            Course Description
                                        </div>
                                        <textarea id="newDesc" placeholder="${courseDesc}" type="text" required>${courseDesc}</textarea>
                                    </div>
                                </div>
                                <div class="ui action input">
                                    <div class="ui blue right pointing label">
                                        Enrollment size
                                    </div>
                                    <input id="newSize" placeholder="${numStudentsEnrolled}" type="number" value="${numStudentsEnrolled}" required>
                                </div>
                                <h4 class="ui horizontal divider header">
                                    <i class="save icon"></i>
                                </h4>
                                <button class="fluid blue ui button" id="course-save-info">Save</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
        <script src="../lib/semantic/semantic.min.js"></script>
        <script src="../js/api.js"></script>
        <script src="../js/course.js"></script>
        <script src="../js/login.js"></script>
    </body>
</html>