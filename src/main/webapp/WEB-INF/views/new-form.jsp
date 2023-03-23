<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <meta charset="UTF-8"/>
        <title>점심 기록</title>
    </head>
    <body>
        <form action="save" method="post" enctype="multipart/form-data">
            <input type="hidden" name="id" />
            식당:     <input type="text" name="restaurant" />
            메뉴:     <input type="text" name="menu" />
            이미지:    <input type="file" name="image">
            가격:     <input type="number" name="price" />
            평점:     <input type="number" name="grade" step="0.1"/>
            평균 평점: <input type="number" name="averageGrade" step="0.1" readonly/>
            <button type="submit">전송</button>
        </form>
    </body>
</html>
