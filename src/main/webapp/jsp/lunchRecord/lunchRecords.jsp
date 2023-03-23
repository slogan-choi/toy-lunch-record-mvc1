<%@ page import="lunch.record.servlet.domain.LunchRecord" %>
<%@ page import="lunch.record.util.Utils" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.util.List" %>
<%@ page import="lunch.record.servlet.domain.LunchRecordRepository" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    LunchRecordRepository repository = LunchRecordRepository.getInstance();
    List<LunchRecord> lunchRecords = repository.findAll();
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
                <%
                    for (LunchRecord lunchRecord : lunchRecords) {
                        out.write("  <tr>");
                        out.write("      <td>" + lunchRecord.getId() + "</td>");
                        out.write("      <td>" + lunchRecord.getRestaurant() + "</td>");
                        out.write("      <td>" + lunchRecord.getMenu() + "</td>");
                        try {
                            String base64EncodeByte = Utils.getBase64EncodeByte(lunchRecord.getImage().getBinaryStream().readAllBytes());
                            out.write("   <td> <img src=\"data:image/png;base64," + base64EncodeByte + "\" /> </td>");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        out.write("      <td>" + lunchRecord.getPrice() + "</td>");
                        out.write("      <td>" + lunchRecord.getGrade() + "</td>");
                        out.write("      <td>" + lunchRecord.getAverageGrade() + "</td>");
                        out.write("      <td>" + lunchRecord.getUpdateAt() + "</td>");
                        out.write("      <td>" + lunchRecord.getCreateAt() + "</td>");
                        out.write("      <td> <a href=\"/jsp/lunchRecord/update-form.jsp?id=" + lunchRecord.getId() + "\">수정</a> </td>");
                        out.write("      <td> <a href=\"/jsp/lunchRecord/delete-form.jsp?id=" + lunchRecord.getId() + "\">삭제</a> </td>");
                        out.write("  </tr>");
                    }
                %>
            </tbody>
        </table>
    </body>
</html>
