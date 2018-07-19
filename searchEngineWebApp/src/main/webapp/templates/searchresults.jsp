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
  <link rel="stylesheet" href="./css/reset.css" type="text/css">
  <link rel="stylesheet" href="./css/index.css" type="text/css">
  <link rel="stylesheet" href="./css/results.css" type="text/css">
  <link rel= "stylesheet" href= "./css/login.css" type="text/css">
  <title>UTSC Document Search</title>
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

            <a class="btn signedIn" id="uploadButton" href="/upload">Upload</a>
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
    <!-- search results -->
    <div class=search-results>
        <div class="ui orange horrizontal label" style="z-index: 100;">${totalResults} results found</div>
        <div class="separator"></div>
        
        <div class="results-table">
            <c:forEach var="result" items="${searchResults}">
                <div class="ui raised segment search-results-item">
                    <i class="file alternate outline icon"></i>
                    <div class="search-results-item-info">
                        <div class="file-name">
                            <a href="/file?id=${result.id}"><c:out value="${result.title}"/></a>
                            <div class="ui teal tag label"><c:out value="${result.fileType}"/></div>
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
                        <div class="file-owner">
                            Owner: 
                            <a href="/profile?id=${result.owner}"><c:out value="${fn:toUpperCase(result.owner)}"/></a>
                        </div>
                    </div>
                </div>
                <!-- <tr>
                    <td>
                        Title: <c:out value="${result.title}"/><br>
                        Owner: <c:out value="${result.owner}"/><br>
                        Course Code: <c:out value="${result.courseCode}"/><br>
                        File Type: <c:out value="${result.fileType}"/><br>
                    </td>
                </tr> -->
            </c:forEach>
        </div>

        <!-- page number -->
        <table class="pagination">
            <tr>
                <!-- previous link -->
                <c:if test="${currentPage != 1}">
                    <td><a href="${noPageUri}page=${currentPage - 1}">
                    <button class="page-move-btn">Previous</button></a></td>
                </c:if>
                <!-- page number -->
                <c:forEach begin="${minPageDisplay}" end="${maxPageDisplay}" var="index">
                    <c:choose>
                        <c:when test="${currentPage eq index}">
                            <td class="page-num" id="active-page">${index}</td>
                        </c:when>
                        <c:otherwise>
                            <td class="page-num"><a href="${noPageUri}page=${index}">${index}</a></td>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
                <!-- next link -->
                <c:if test="${currentPage != totalPages}">
                    <td><a href="${noPageUri}page=${currentPage + 1}">
                    <button class="page-move-btn">Next</button></a><td>
                </c:if>
            </tr>
        </table>

    </div>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
    <script src="../lib/semantic/semantic.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
    <script src="js/api.js"></script>
    <script src="./js/login.js"></script>
</body>
</html>
