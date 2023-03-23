<%@ page import="lunch.record.servlet.domain.LunchRecordRepository" %>
<%@ page import="lunch.record.servlet.domain.LunchRecord" %>
<%@ page import="lunch.record.util.Utils" %>
<%@ page import="java.sql.SQLException" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    LunchRecordRepository repository = LunchRecordRepository.getInstance();
    LunchRecord lunchRecord = repository.findById(Long.valueOf(request.getParameter("id")));
    String base64EncodeByte = "";
    try {
        base64EncodeByte = Utils.getBase64EncodeByte(lunchRecord.getImage().getBinaryStream().readAllBytes());
    } catch (SQLException e) {
        e.printStackTrace();
    }
%>
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
                    <%
                        out.write("         <td>" + lunchRecord.getId() + "</td>");
                        out.write("         <td>" + lunchRecord.getRestaurant() + "</td>");
                        out.write("         <td>" + lunchRecord.getMenu() + "</td>");
                        out.write("         <td> <img src=\"data:image/png;base64," + base64EncodeByte + "\" /> </td>");
                        out.write("         <td>" + lunchRecord.getPrice() + "</td>");
                        out.write("         <td>" + lunchRecord.getGrade() + "</td>");
                        out.write("         <td>" + lunchRecord.getAverageGrade() + "</td>");
                        out.write("         <td>" + lunchRecord.getUpdateAt() + "</td>");
                        out.write("         <td>" + lunchRecord.getCreateAt() + "</td>");
                    %>
                </tr>
            </tbody>
        </table>
        <form action="/jsp/lunchRecord/delete.jsp" method="delete">
            <%
                out.write("<input type=\"hidden\" name=\"id\" value=\"" + lunchRecord.getId() + "\"readonly/> ");
            %>
            <button type="submit">삭제</button>
        </form>
    </body>
</html>
