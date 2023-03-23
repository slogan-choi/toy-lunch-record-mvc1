<%@ page import="lunch.record.servlet.domain.LunchRecord" %>
<%@ page import="lunch.record.servlet.domain.LunchRecordRepository" %>
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
        <form action="/servlet/lunchRecord/update" method="post" enctype="multipart/form-data">
            <%
                out.write("아이디:    <input type=\"text\" name=\"id\" value=\"" + lunchRecord.getId() + "\" readonly/> ");
                out.write("식당:     <input type=\"text\" name=\"restaurant\" value=\"" + lunchRecord.getRestaurant() + "\"/> ");
                out.write("메뉴:     <input type=\"text\" name=\"menu\" value=\"" + lunchRecord.getMenu() + "\"/> ");
                out.write("이미지:    <input type=\"file\" name=\"image\" /> ");
                out.write("가격:     <input type=\"number\" name=\"price\" value=\"" + lunchRecord.getPrice() + "\"/> ");
                out.write("평점:     <input type=\"number\" name=\"grade\" step=\"0.1\" value=\"" + lunchRecord.getGrade() + "\"/> ");
                out.write("평균 평점: <input type=\"number\" name=\"averageGrade\" step=\"0.1\" value=\"" + lunchRecord.getAverageGrade() + "\" readonly/> ");
            %>
            <button type="submit">수정</button>
        </form>
    </body>
</html>
