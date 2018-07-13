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
    
    <!-- search results -->
    <div class=search-results>
        ${totalResults} results found.
        <table>
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
        <!-- previous link -->
        <c:if test="${currentPage != 1}">
            <a href="${noPageUri}page=${currentPage - 1}">Previous</a>
        </c:if>
        
        <!-- page number --> 
        <table>
            <tr>
                <c:forEach begin="${minPageDisplay}" end="${maxPageDisplay+1}" var="index">
                    <c:choose>
                        <c:when test="${currentPage eq index}">
                            <td>${index}</td>
                        </c:when>
                        <c:otherwise>
                            <td><a href="${noPageUri}page=${index}">${index}</a></td>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </tr>
        </table>       
        
        <!-- next link -->
        <c:if test="${currentPage - 1 != totalPages}">
            <a href="${noPageUri}page=${currentPage + 1}">Next</a>
        </c:if>
    </div>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="js/api.js"></script>
    <script src="js/main.js"></script>
    <script src="./js/login.js"></script>
</body>
</html>
