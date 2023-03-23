package lunch.record.servlet.web;

import lombok.extern.slf4j.Slf4j;
import lunch.record.util.Utils;
import lunch.record.servlet.domain.LunchRecord;
import lunch.record.servlet.domain.LunchRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(classes = LunchRecordListServlet.class)
class LunchRecordListServletTest {

    MockHttpServletRequest request;
    MockHttpServletResponse response;

    @Autowired
    LunchRecordListServlet servlet;

    LunchRecordRepository repository = LunchRecordRepository.getInstance();

    @BeforeEach
    void before() {
        request = new MockHttpServletRequest();
        request.setMethod(HttpMethod.POST.name());
        request.setRequestURI("/servlet/lunchRecord/save");
        request.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("조회")
    void list() throws SQLException, ServletException, IOException {
        // given
        LunchRecord lunchRecord = initLunchRecordSave();

        // when
        servlet.service(request, response);

        // then
        LunchRecord lunchRecordById = repository.findById(Long.valueOf(lunchRecord.getId()));
        assertThat(lunchRecord.getId()).isEqualTo(lunchRecordById.getId());
        assertThat(lunchRecord.getRestaurant()).isEqualTo(lunchRecordById.getRestaurant());
        assertThat(lunchRecord.getMenu()).isEqualTo(lunchRecordById.getMenu());
        assertThat(lunchRecord.getImage()).isEqualTo(lunchRecordById.getImage());
        assertThat(lunchRecord.getPrice()).isEqualTo(lunchRecordById.getPrice());
        assertThat(lunchRecord.getGrade()).isEqualTo(lunchRecordById.getGrade());
        assertThat(lunchRecord.getAverageGrade()).isEqualTo(lunchRecordById.getAverageGrade());
        assertThat(lunchRecord.getUpdateAt()).isEqualTo(lunchRecordById.getUpdateAt());
        assertThat(lunchRecord.getCreateAt()).isEqualTo(lunchRecordById.getCreateAt());
    }

    private LunchRecord initLunchRecordSave() throws SQLException {
        Blob blob;
        try {
            blob = new SerialBlob(Utils.imageToByteArray("/Users/ghc/development/img/test.png"));
        } catch (SQLException e) {
            log.error("blob error");
            throw e;
        }

        LocalTime now = LocalTime.now();
        LunchRecord lunchRecord = new LunchRecord(
                "test",
                "test",
                blob,
                BigDecimal.ONE,
                5.0f,
                now,
                now
        );

        lunchRecord.setAverageGrade(getAverageGrade(lunchRecord));
        return repository.save(lunchRecord);
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