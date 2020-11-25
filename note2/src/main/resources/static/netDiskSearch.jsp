<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta charset="UTF-8">
    <title>Search</title>
</head>
<body>
<h2>Search File in Net Disk</h2>
<form action="/api/netDisk2" method="POST">
    Search For: <input type="text" name="filter" /> <br>
    <input type="submit" value="Go !"/>
</form>

</body>
</html>