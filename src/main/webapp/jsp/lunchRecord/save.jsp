<%@ page import="lunch.record.servlet.domain.LunchRecord" %>
<%@ page import="lunch.record.servlet.domain.LunchRecordRepository" %>
<%@ page import="java.util.Comparator" %>
<%@ page import="java.sql.Blob" %>
<%@ page import="javax.sql.rowset.serial.SerialBlob" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.time.LocalTime" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    LunchRecordRepository repository = LunchRecordRepository.getInstance();

    Blob blob;
    try {
        blob = new SerialBlob(request.getPart("image").getInputStream().readAllBytes());
    } catch (SQLException e) {
        throw new ServletException(e);
    }

    LocalTime now = LocalTime.now();
    LunchRecord lunchRecord = new LunchRecord(
            request.getParameter("restaurant"),
            request.getParameter("menu"),
            blob,
            BigDecimal.valueOf(Long.parseLong(request.getParameter("price"))),
            Float.parseFloat(request.getParameter("grade")),
            now,
            now
    );

    lunchRecord.setAverageGrade(getAverageGrade(lunchRecord));
    repository.save(lunchRecord);

    int maxId = repository.findAll().stream()
            .max(Comparator.comparing(LunchRecord::getId))
            .orElseThrow()
            .getId();
    LunchRecord savedLunchRecord = repository.findById((long) maxId);

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
                out.write("<li>id=\"" + savedLunchRecord.getId() + "</li>");
                out.write("<li>restaurant=\"" + savedLunchRecord.getRestaurant() + "</li>");
                out.write("<li>menu=\"" + savedLunchRecord.getMenu() + "</li>");
                out.write("<li>image=\"" + savedLunchRecord.getImage() + "</li>");
                out.write("<li>price=\"" + savedLunchRecord.getPrice() + "</li>");
                out.write("<li>grade=\"" + savedLunchRecord.getGrade() + "</li>");
                out.write("<li>averageGrade=\"" + savedLunchRecord.getAverageGrade() + "</li>");
                out.write("<li>updateAt=\"" + savedLunchRecord.getUpdateAt() + "</li>");
                out.write("<li>createAt=\"" + savedLunchRecord.getCreateAt() + "</li>");
            %>
        </ul>
        <a href="/jsp/lunchRecord/list.jsp">조회</a>
    </body>
</html>
<%!
    LunchRecordRepository repository = LunchRecordRepository.getInstance();

    private Float getAverageGrade(LunchRecord lunchRecord) {
        float averageGrade;
        // 평점 획득
        Float grade = lunchRecord.getGrade();
        // 식당의 메뉴 기록 조회
        List<LunchRecord> byRestaurantMenu = repository.findByRestaurantMenu(lunchRecord.getRestaurant(), lunchRecord.getMenu());

        if (byRestaurantMenu.isEmpty()) {
            averageGrade = grade;
        } else {
            // 평점 적용
            if (lunchRecord.getId() == null) {
                // 식당의 메뉴 기록 추가
                byRestaurantMenu.add(lunchRecord);
            } else {
                byRestaurantMenu.stream()
                        .filter(it -> it.getId().equals(lunchRecord.getId()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException())
                        .setGrade(grade);
            }

            // 식당의 같은 메뉴에 대한 기록 평점의 평균을 반환
            averageGrade = (float) byRestaurantMenu.stream()
                    .mapToDouble(LunchRecord::getGrade)
                    .average()
                    .getAsDouble();
        }

        return (float) (Math.round(averageGrade * 1000) / 1000.0);
    }
%>