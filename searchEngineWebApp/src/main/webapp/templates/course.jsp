<!DOCTYPE html>
<html>
    <head>
        <meta charset= "UTF-8">
        <title>Course page: ${courseID}</title>
        <!-- <link rel= "stylesheet" href= "./css/main.css"> -->
        <link href="https://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet"> 
        <link rel="stylesheet" href="../css/index.css" type="text/css">
        <link rel= "stylesheet" href= "../css/login.css" type="text/css">
        <link rel= "stylesheet" href= "../css/course.css" type="text/css">
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
                    <div class="signedIn header-info">Welcome! <span class="user"></span></div>
                    <a class="btn signedIn" id="uploadButton" href="/upload">Upload</a>
                    <a class="btn signedIn" id="profileButton" href="/profile">Profile</a>
                    <button class="btn signedIn" id="logoutButton">Logout</button>
                </div>

                <!-- the sign in pop up window -->
                <div id="SIB" class="popup">
                <form class="login-content" method ="POST" enctype = "multipart/form-data">
                    <div class="container">
                    <label for="username">Username</label>
                    <input id="s-username" type="text" placeholder="Enter Username" name="loginuname" required>

                    <label for="psw">Password</label>
                    <input id="s-pwd" type="password" placeholder="Enter Password" name="loginpsw" required>

                    <button id = "SC" class = "loginButton" type="submit">Login</button>

                    <button type="button"
                    onclick="document.getElementById('SIB').style.display='none'"
                    class="cancelbtn">Cancel</button>
                    </div>
                </form>
                </div>


                <!-- the register pop up window -->
                <div id="RIB" class="popup">
                <form class="login-content" method ="POST" enctype = "multipart/form-data">
                    <div class="container">
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

                    <button id ="RC" class = "loginButton" type="submit" >Register</button>

                    <button type="button"
                    onclick="document.getElementById('RIB').style.display='none'"
                    class="cancelbtn">Cancel</button>
                    </div>
                </form>
                </div>
            </div>
        </div>
        <span id="instID" class="hide">${instID}</span>
        <div class="content">
            <div class="course-info">
                <div class="text-item">Course ID: <span id="courseID">${courseID}</span></div>
                <div class="text-item">${courseName}</div>
                <div class="text-desc">${courseDesc}</div>
                <div class="separator"></div>
                <div class="text-item">Instructor: <a href="/profile?id=${instID}">${courseInstructor}</a></div>
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
        </div>

        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
        <script src="../js/api.js"></script>
        <script src="../js/login.js"></script>
        <script src="../js/course.js"></script>
    </body>
</html>