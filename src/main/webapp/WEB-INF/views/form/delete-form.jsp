<%@ page import="lunch.record.util.Utils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <meta charset="UTF-8"/>
        <title>점심 기록</title>
    </head>
    <body>
        <table>
            <thead>
                <th>id</th>
                <th>restaurant</th>
                <th>menu</th>
                <th>image</th>
                <th>price</th>
                <th>grade</th>
                <th>averageGrade</th>
                <th>updateAt</th>
                <th>createAt</th>
            </thead>
            <tbody>
                <tr>
                    <td>${lunchRecord.id}</td>
                    <td>${lunchRecord.restaurant}</td>
                    <td>${lunchRecord.menu}</td>
                    <td> <img src="data:image/png;base64,${Utils.getBase64EncodeByte(lunchRecord.image.getBinaryStream().readAllBytes())}"/> </td>
                    <td>${lunchRecord.price}</td>
                    <td>${lunchRecord.grade}</td>
                    <td>${lunchRecord.averageGrade}</td>
                    <td>${lunchRecord.updateAt}</td>
                    <td>${lunchRecord.createAt}</td>
                </tr>
            </tbody>
        </table>
        <form action="delete" method="post">
            <input type="hidden" name="id" value=${lunchRecord.id} readonly />
            <button type="submit">삭제</button>
        </form>
    </body>
</html>
