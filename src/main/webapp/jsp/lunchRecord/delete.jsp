<%@ page import="lunch.record.servlet.domain.LunchRecordRepository" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    LunchRecordRepository repository = LunchRecordRepository.getInstance();
    repository.delete(Integer.parseInt(request.getParameter("id")));
%>
<html>
    <head>
        <meta charset="UTF-8"/>
        <title>점심 기록</title>
    </head>
    <body>
        삭제 성공
        <a href="/jsp/lunchRecord/lunchRecords.jsp">조회</a>
    </body>
</html>
