package lunch.record.servlet.web.frontcontroller.controller;

import lombok.extern.slf4j.Slf4j;
import lunch.record.servlet.domain.LunchRecord;
import lunch.record.servlet.domain.LunchRecordRepository;
import lunch.record.util.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockPart;

import javax.servlet.ServletException;
import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@SpringBootTest(classes = LunchRecordDeleteController.class)
class LunchRecordDeleteControllerTest {

    MockHttpServletRequest request;
    MockHttpServletResponse response;

    LunchRecordRepository repository = new LunchRecordRepository();

    @Autowired
    LunchRecordDeleteController controller;

    @BeforeEach
    void before() {
        request = new MockHttpServletRequest();
        request.setMethod(HttpMethod.POST.name());
        request.setRequestURI("/front-controller/lunchRecord/delete");
        request.setContentType(APPLICATION_JSON_VALUE);

        response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("경로 확인")
    void checkViewPath() throws SQLException, ServletException, IOException {
        // given
        String restaurant = "test";
        String menu = "test";
        int maxId = 0;

        List<LunchRecord> all = repository.findAll();
        if (!all.isEmpty()) {
            maxId = all.stream()
                    .max(Comparator.comparing(LunchRecord::getId))
                    .orElseThrow()
                    .getId();
        }

        request.setParameter("id", String.valueOf(maxId+1));
        request.setParameter("restaurant", restaurant);
        request.setParameter("menu", menu);
        MockPart mockPart = new MockPart("image", "test.png", Utils.imageToByteArray("/Users/ghc/development/img/test.png"));
        request.addPart(mockPart);
        request.setParameter("price", String.valueOf(BigDecimal.ONE));
        request.setParameter("grade", String.valueOf(5.0f));

        LocalTime now = LocalTime.now();
        LunchRecord lunchRecord = new LunchRecord(
                maxId + 1,
                restaurant,
                menu,
                new SerialBlob(Utils.imageToByteArray("/Users/ghc/development/img/test.png")),
                BigDecimal.ONE,
                5.0f,
                now,
                now
        );

        repository.save(lunchRecord);


        // when
        controller.process(request, response);
        // then
        assertThat(response.getForwardedUrl()).isEqualTo("/WEB-INF/views/delete.jsp");
    }

    @Test
    @DisplayName("Attribute 확인")
    void checkRequestAttribute() throws SQLException, ServletException, IOException {
        // given
        String restaurant = "test";
        String menu = "test";
        int maxId = 0;

        List<LunchRecord> all = repository.findAll();
        if (!all.isEmpty()) {
            maxId = all.stream()
                    .max(Comparator.comparing(LunchRecord::getId))
                    .orElseThrow()
                    .getId();
        }

        request.setParameter("id", String.valueOf(maxId+1));
        request.setParameter("restaurant", restaurant);
        request.setParameter("menu", menu);
        MockPart mockPart = new MockPart("image", "test.png", Utils.imageToByteArray("/Users/ghc/development/img/test.png"));
        request.addPart(mockPart);
        request.setParameter("price", String.valueOf(BigDecimal.ONE));
        request.setParameter("grade", String.valueOf(5.0f));

        LocalTime now = LocalTime.now();
        LunchRecord lunchRecord = new LunchRecord(
                maxId + 1,
                restaurant,
                menu,
                new SerialBlob(Utils.imageToByteArray("/Users/ghc/development/img/test.png")),
                BigDecimal.ONE,
                5.0f,
                now,
                now
        );

        repository.save(lunchRecord);

        // when
        controller.process(request, response);

        // then
        assertThat(repository.findById((long) maxId + 1)).isNull();
    }

}