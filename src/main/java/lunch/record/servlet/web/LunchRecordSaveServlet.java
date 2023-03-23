package lunch.record.servlet.web;

import lombok.extern.slf4j.Slf4j;
import lunch.record.servlet.domain.LunchRecord;
import lunch.record.servlet.domain.LunchRecordRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

@Slf4j
@WebServlet(name = "lunchRecordSaveServlet", urlPatterns = "/servlet/lunchRecord/save")
@MultipartConfig()
public class LunchRecordSaveServlet extends HttpServlet {

    private LunchRecordRepository repository = LunchRecordRepository.getInstance();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        writer.write("<html>\n" +
                "<head>\n" +
                "   <meta charset=\"utf-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "성공\n" +
                "<ul>\n" +
                "   <li>id=" + savedLunchRecord.getId() + "</li>\n" +
                "   <li>restaurant=" + savedLunchRecord.getRestaurant() + "</li>\n" +
                "   <li>menu=" + savedLunchRecord.getMenu() + "</li>\n" +
                "   <li>image=" + savedLunchRecord.getImage() + "</li>\n" +
                "   <li>price=" + savedLunchRecord.getPrice() + "</li>\n" +
                "   <li>grade=" + savedLunchRecord.getGrade() + "</li>\n" +
                "   <li>averageGrade=" + savedLunchRecord.getAverageGrade() + "</li>\n" +
                "   <li>updateAt=" + savedLunchRecord.getUpdateAt() + "</li>\n" +
                "   <li>createAt=" + savedLunchRecord.getCreateAt() + "</li>\n" +
                "</ul>\n" +
                "<a href=\"/servlet/lunchRecord/list\">조회</a>\n" +
                "</body>\n" +
                "</html>\n"
        );
    }

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
}
