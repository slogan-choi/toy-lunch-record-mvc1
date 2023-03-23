package lunch.record.servlet.web.frontcontroller.controller;

import lunch.record.servlet.domain.LunchRecord;
import lunch.record.servlet.domain.LunchRecordRepository;
import lunch.record.servlet.web.frontcontroller.Controller;
import lunch.record.servlet.web.frontcontroller.MyView;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;

public class LunchRecordUpdateController implements Controller {

    private LunchRecordRepository repository = LunchRecordRepository.getInstance();

    @Override
    public MyView process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Blob blob;

        try {
            blob = new SerialBlob(request.getPart("image").getInputStream().readAllBytes());
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        LocalTime now = LocalTime.now();
        LunchRecord lunchRecord = new LunchRecord(
                Integer.parseInt(request.getParameter("id")),
                request.getParameter("restaurant"),
                request.getParameter("menu"),
                blob,
                BigDecimal.valueOf(Long.parseLong(request.getParameter("price"))),
                Float.parseFloat(request.getParameter("grade")),
                now,
                now
        );

        repository.update(
                lunchRecord.getId(),
                lunchRecord.getRestaurant(),
                lunchRecord.getMenu(),
                lunchRecord.getImage(),
                lunchRecord.getPrice(),
                lunchRecord.getGrade()
        );

        repository.updateAverageGradeByRestaurantMenu(
                getAverageGrade(lunchRecord),
                lunchRecord.getRestaurant(),
                lunchRecord.getMenu()
        );

        LunchRecord updatedLunchRecord = repository.findById(Long.valueOf(request.getParameter("id")));
        // Model에 데이터를 담아서 보관한다.
        request.setAttribute("lunchRecord", updatedLunchRecord);

        return new MyView("/WEB-INF/views/update.jsp");
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
