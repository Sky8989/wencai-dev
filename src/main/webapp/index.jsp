<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="mvc" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  User: _KeMing
  Date: 2017/4/7
  Time: 上午9:32
  verson: 1.0
  description: 
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<form action="${pageContext.request.contextPath}/user/login" method="post">
    <input type="text" name="username" /><br/>
    <input type="password" name="password" /><br/>
    <input type="submit" value="login" />
</form>

<h1>${msg}</h1>
<h1>${s}</h1>
<p>${userList}</p>

<mvc:forEach items="${userList }" var="list">
<p>${list.name}</p>
</mvc:forEach>
</body>
</html>
