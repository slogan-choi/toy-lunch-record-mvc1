package lunch.record.servlet.web.frontcontroller.controller;

import lunch.record.servlet.domain.LunchRecord;
import lunch.record.servlet.domain.LunchRecordRepository;
import lunch.record.servlet.web.frontcontroller.Controller;
import lunch.record.servlet.web.frontcontroller.ModelView;
import lunch.record.servlet.web.frontcontroller.RequestInfo;
import lunch.record.util.Utils;

import javax.servlet.ServletException;
import javax.servlet.http.Part;
import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class LunchRecordSaveController implements Controller {

    private LunchRecordRepository repository = LunchRecordRepository.getInstance();

    @Override
    public ModelView process(Map<String, RequestInfo> paramMap) throws ServletException, IOException {
        Blob blob;

        try {
            blob = new SerialBlob(((Part) paramMap.get("image").getInfo()).getInputStream().readAllBytes());
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        LocalTime now = LocalTime.now();

        LunchRecord lunchRecord = new LunchRecord(
                (String) paramMap.get("restaurant").getInfo(),
                (String) paramMap.get("menu").getInfo(),
                blob,
                BigDecimal.valueOf(Long.parseLong((String) paramMap.get("price").getInfo())),
                Float.parseFloat((String) paramMap.get("grade").getInfo()),
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

        // Model에 데이터를 보관한다.
        ModelView mv = new ModelView("save-result");
        mv.getModel().put("lunchRecord", savedLunchRecord);

        return mv;
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
