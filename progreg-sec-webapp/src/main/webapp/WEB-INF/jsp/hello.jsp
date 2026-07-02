<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Hello</title>
</head>
<body>
<h1>${message}</h1>
<p>This JSP lives in WEB-INF/jsp and is rendered by a Spring MVC controller.</p>

<h2>Authenticated User</h2>
<p><strong>Username:</strong> ${username}</p>

<h2>Roles</h2>
<ul>
    <c:forEach var="role" items="${roles}">
        <li>${role}</li>
    </c:forEach>
</ul>
</body>
</html>
