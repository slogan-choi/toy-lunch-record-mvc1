<%@ page import="lunch.record.util.Utils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <meta charset="UTF-8"/>
        <title>점심 기록</title>
    </head>
    <body>
        성공
        <ul>
            <li>id=${lunchRecord.id}</li>
            <li>restaurant=${lunchRecord.restaurant}</li>
            <li>menu=${lunchRecord.menu}</li>
            <li>image=<img src="data:image/png;base64,${Utils.getBase64EncodeByte(lunchRecord.image.getBinaryStream().readAllBytes())}"/>
            <li>price=${lunchRecord.price}</li>
            <li>grade=${lunchRecord.grade}</li>
            <li>averageGrade=${lunchRecord.averageGrade}</li>
            <li>updateAt=${lunchRecord.updateAt}</li>
            <li>createAt=${lunchRecord.createAt}</li>
        </ul>
        <a href="/servlet-mvc/lunchRecords">조회</a>
    </body>
</html>
