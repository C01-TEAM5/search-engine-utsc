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
    <!-- search results -->
  <div class=search-results>
      ${totalResults} results found.
      <table class="results-table">
          <c:forEach var="result" items="${searchResults}">
              <tr>
                  <td>
                      Title: <c:out value="${result.title}"/><br>
                      Owner: <c:out value="${result.owner}"/><br>
                      Course Code: <c:out value="${result.courseCode}"/><br>
                      File Type: <c:out value="${result.fileType}"/><br>
                  </td>
              </tr>
          </c:forEach>
      </table>

      <!-- page number -->
      <table>
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
  <script src="js/api.js"></script>
  <script src="js/main.js"></script>
  <script src="./js/login.js"></script>
</body>
</html>
