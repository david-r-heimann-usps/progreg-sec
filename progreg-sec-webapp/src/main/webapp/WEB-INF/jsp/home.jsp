<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Home</title>
</head>
<body>
<a href="${pageContext.request.contextPath}/logoff.action">Log Off</a>
<h1>${message}</h1>
<p>This is the home page for the progreg-sec web application.</p>
<p>${username}</p>
<p>${roles}</p>
<p>${token}</p>
</body>
</html>
