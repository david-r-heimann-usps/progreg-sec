<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<title>GFG Login Page</title>
<div>
Active:<spring:eval expression="@environment.getActiveProfiles()" /> 
Default:<spring:eval expression="@environment.getDefaultProfiles()" />
</div>
<body bgcolor="blue">
    <h1>Custom Login Page</h1>
    
    <form:form action="j_security_check" method="post">
    
        Username : <input type="text" name="j_username">
        <br/>
        Password : <input type="password" name="j_password">
        <br/>
        <input type="submit" value="Login">
    
    </form:form>
    
</body>
</html>