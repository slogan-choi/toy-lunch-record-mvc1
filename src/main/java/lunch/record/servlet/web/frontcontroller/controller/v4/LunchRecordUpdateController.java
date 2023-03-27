package lunch.record.servlet.web.frontcontroller.controller.v4;

import lunch.record.servlet.domain.LunchRecord;
import lunch.record.servlet.domain.LunchRecordRepository;
import lunch.record.servlet.web.frontcontroller.ControllerV4;
import lunch.record.servlet.web.frontcontroller.RequestInfo;

import javax.servlet.ServletException;
import javax.servlet.http.Part;
import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class LunchRecordUpdateController implements ControllerV4 {

    private LunchRecordRepository repository = LunchRecordRepository.getInstance();

    @Override
    public String process(Map<String, RequestInfo> paramMap, Map<String, Object> model) throws ServletException, IOException {
        Blob blob;
        try {
            blob = new SerialBlob(((Part) paramMap.get("image").getInfo()).getInputStream().readAllBytes());
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        LocalTime now = LocalTime.now();

        LunchRecord lunchRecord = new LunchRecord(
                Integer.parseInt((String) paramMap.get("id").getInfo()),
                (String) paramMap.get("restaurant").getInfo(),
                (String) paramMap.get("menu").getInfo(),
                blob,
                BigDecimal.valueOf(Long.parseLong((String) paramMap.get("price").getInfo())),
                Float.parseFloat((String) paramMap.get("grade").getInfo()),
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

        LunchRecord updatedLunchRecord = repository.findById(Long.valueOf((String) paramMap.get("id").getInfo()));
        // Model에 데이터를 담아서 보관한다.
        model.put("lunchRecord", updatedLunchRecord);

        return "update";
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
