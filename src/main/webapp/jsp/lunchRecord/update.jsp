<%@ page import="lunch.record.servlet.domain.LunchRecord" %>
<%@ page import="lunch.record.servlet.domain.LunchRecordRepository" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    LunchRecordRepository repository = LunchRecordRepository.getInstance();
    LunchRecord updatedLunchRecord = repository.findById(Long.valueOf(request.getParameter("id")));
%>
<html>
    <head>
        <meta charset="UTF-8"/>
        <title>점심 기록</title>
    </head>
    <body>
        성공
        <ul>
            <%
                out.write("<li>id=\"" + updatedLunchRecord.getId() + "</li>");
                out.write("<li>restaurant=\"" + updatedLunchRecord.getRestaurant() + "</li>");
                out.write("<li>menu=\"" + updatedLunchRecord.getMenu() + "</li>");
                out.write("<li>image=\"" + updatedLunchRecord.getImage() + "</li>");
                out.write("<li>price=\"" + updatedLunchRecord.getPrice() + "</li>");
                out.write("<li>grade=\"" + updatedLunchRecord.getGrade() + "</li>");
                out.write("<li>averageGrade=\"" + updatedLunchRecord.getAverageGrade() + "</li>");
                out.write("<li>updateAt=\"" + updatedLunchRecord.getUpdateAt() + "</li>");
                out.write("<li>createAt=\"" + updatedLunchRecord.getCreateAt() + "</li>");
            %>
        </ul>
        <a href="/jsp/lunchRecord/list.jsp">조회</a>
    </body>
</html>
