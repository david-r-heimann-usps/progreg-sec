<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Home</title>
</head>
<body>
<%@ include file="/WEB-INF/jsp/includes/header.jspf" %>
<h1>${message}</h1>
<p>This is the home page for the progreg-sec web application.</p>
<p><a href="${pageContext.request.contextPath}/hello">Go to hello page</a></p>
</body>
</html>
