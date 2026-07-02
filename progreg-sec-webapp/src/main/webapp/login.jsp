<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Login – progreg-sec</title>
</head>
<body>
<h1>Program Registry – Login</h1>

<%-- WebSphere sets this attribute when credentials are invalid (form error page). --%>
<% if (request.getParameter("error") != null) { %>
    <p style="color:red;">Invalid username or password. Please try again.</p>
<% } %>

<%-- Standard J2EE FORM login: action must be j_security_check;
     field names must be j_username and j_password. --%>
<form method="post" action="j_security_check">
    <table>
        <tr>
            <td><label for="j_username">Username:</label></td>
            <td><input type="text" id="j_username" name="j_username" autofocus required/></td>
        </tr>
        <tr>
            <td><label for="j_password">Password:</label></td>
            <td><input type="password" id="j_password" name="j_password" required/></td>
        </tr>
        <tr>
            <td colspan="2">
                <button type="submit">Log In</button>
            </td>
        </tr>
    </table>
</form>
</body>
</html>
