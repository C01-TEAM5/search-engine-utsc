<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<!DOCTYPE html>
<html>
<body>
    <object data="../${path}"
        type="text/html" width="100%" height="800px">
        <p><b>Example fallback content</b>
        : This browser does not support docxs. Please download the
        docx to view it: <a href="../${path}">Download</a>.</p>
    </object>

    <%-- <c:choose>
        <c:when test = "${type=='pdf'} ">
            <object data="../${path}"
                type="application/pdf" width="100%" height="800px">
                <p><b>Example fallback content</b>
                : This browser does not support docxs. Please download the
                docx to view it: <a href="../${path}">Download</a>.</p>
            </object>
        </c:when>

        <c:when test = "${type=='txt'} ">
            <object data="../${path}"
                <type="text/plain" width="100%" height="800px">
                <p><b>Example fallback content</b>
                : This browser does not support docxs. Please download the
                docx to view it: <a href="../${path}">Download</a>.</p>
            </object>
        </c:when>

        <c:when test = "${type=='html'} ">
            <object data="../${path}"
                <type="text/html" width="100%" height="800px">
                <p><b>Example fallback content</b>
                : This browser does not support docxs. Please download the
                docx to view it: <a href="../${path}">Download</a>.</p>
            </object>
        </c:when>
    </c:choose> --%>

</body>
</html>
