<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang = "en">
<head>
    <meta charset= "UTF-8">
    <title>FAQ page</title>
    <link href="https://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet"> 
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="../lib/semantic/semantic.min.css">
    <link rel="stylesheet" href="./css/reset.css" type="text/css">
    <link rel="stylesheet" href="./css/index.css" type="text/css">
    <link rel= "stylesheet" href= "./css/login.css" type="text/css">
    <link rel= "stylesheet" href= "./css/faq.css">
</head>

<body>
    <div id="search-background"></div>
    <div class="header">
        <a href="/"><img src="./media/utscblue-300x132.png" /></a>
            <!-- sign in/out & register button -->
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


	<div class = "faqSection ui raised segment">
		<form>
			<h1>Frequently Asked Questions</h1>
			
			<h3>About</h3>
			<p class ="q"> What is this website all about?</p>
			<p>This site is a dedicated platform for uploading, managing and sharing UofT documents. The site is completely free and accessible to the public. The system is designed to consider and fill the needs of a niche market for UofT students looking to share and view school documents in an isolated server. Documents can be of any type, such as research papers, course syllabi, course content, past exams, etc. All uploaded documents are tied directly to a user and a course to allow for better access to documents and maintenance of files.</p>
			<p>We hope you enjoy using our search platform and find it a great tool for your success in school.</p>
			<p>Sincerely,</p>
			<p>The Development Team of CSCC01 Group 5</p>
			
			<h3>Searching</h3>
			<p class ="q"> What can I search?</p>
			<p>You can search for files based on their title, contents or course code. Matches in the results will be highlighted for you to easily identify if the search result is relevant to your query.</p>
			<p class ="q"> What is this difference between all, instructor and student searches?</p>
			<p>The search options allow you to filter based on who originally uploaded the file. The all search will find files uploaded by any user. The instructor search will find files uploaded only by an instructor. The student search will find files uploaded only by a student. The search system is designed to help you identify the validity of an uploaded file based on user type</p>
			<p class ="q"> What do the .txt, .pdf, .html and .docx filters do?</p>
			<p>The search filters will allow you to display only results of each file type. Filters are stackable, where you can select multiple filters at once. If no filter is selected then the search will simply search for all file types. </p>
			<p class ="q"> What is in the search results?</p>
			<p>Each search will have a list of all files found with the specified query. The results are automatically paginated for users. Each found file will display: the file name, file type, course code, owner and contents. Across each file, the related keywords will be highlighted for the user. </p>
			
			<h3>Uploading Files</h3>
			<p class ="q"> How do I upload files?</p>
			<p>You can upload files once logged in to your account. Uploaded files will be tagged to your account and the specified course code you entered (course is optional field).</p>
			<p class ="q"> Can I upload multiple files at once?</p>
			<p>Yes, file uploading supports multiple files at once</p>
			<p class ="q"> How do I crawl a website?</p>
			<p>The upload crawler requires you to provide a website link, from where it will search for any html, docx, pdf or txt files accessible from the link. All links from crawled in the upload will be stored to the database. For a timely execution, crawls are limited to a maximum of 30 pages and a depth of 2.</p>
			<p class ="q"> Why does my upload crawler take so long to run?</p>
			<p>The crawler is executed through an external java library. To ensure accuracy the crawler runs multiple wait threads for each page before closing to ensure no data is missed. Crawling can take several minutes to complete so please be patient with its upload process.</p>
			
			
			<h3>Downloading Files</h3>
			<p class ="q"> How do I download files?</p>
			<p>By clicking on a file in the search results, you can preview its contents and download the file to your local drive.</p>
			<p class ="q"> Why can't I preview this file?</p>
			<p>You may preview all files before you download them, except for certain docx files. The docx files which cannot be previewed are restricted to the file and cannot be viewed unless downloaded.</p>
			
			
			<h3>Accounts</h3>
			<p class ="q"> Do I need an account to search and access the website?</p>
			<p>No, you can search without a user account. However, having an account allows for additional functions such as file uploading, account management and course access.</p>
			<p class ="q"> How do I create an account?</p>
			<p>You can create an account through the registration portal located at the top right corner of every page. Users can register as either a student or an instructor.</p>
			<p class ="q"> How does my user profile work?</p>
			<p>User profiles give the user the option to manage their details and any files they have uploaded. Each profile includes a series of file statistics to help better understand how the user is contributing to the search system. You can access your own profile by clicking the profile button on the top right corner of each page. You can view another user's profile by clicking on their name.</p>
			
			
			<h3>Courses</h3>
			<p class ="q"> What is a course?</p>
			<p>Courses are a unique feature used to manage both users and files who have interest in a specific course taught at the university. By viewing a course, users can further see related course users and files. As instructors in the course, the instructor may edit course details and contents.</p>
			<p class ="q"> Course management?</p>
			<p>Course management is only available as an instructor. Instructors have the ability to create new courses from their profile. From within a course, Instructors can change course specific information, files, instructors and students.</p>
					
		</form>
	</div>


    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
    <script src="../lib/semantic/semantic.min.js"></script>
    <script src="js/api.js"></script>
    <script src = "./js/uploader.js" type = "text/javascript"></script>
    <script src="./js/login.js"></script>
</body>
