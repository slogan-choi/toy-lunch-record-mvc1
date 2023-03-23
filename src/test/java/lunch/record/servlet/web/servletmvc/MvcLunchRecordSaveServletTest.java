package lunch.record.servlet.web.servletmvc;

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

@Slf4j
@SpringBootTest(classes = MvcLunchRecordSaveServlet.class)
class MvcLunchRecordSaveServletTest {

    MockHttpServletRequest request;
    MockHttpServletResponse response;

    LunchRecordRepository repository = LunchRecordRepository.getInstance();

    @Autowired
    MvcLunchRecordSaveServlet servlet;

    @BeforeEach
    void before() {
        request = new MockHttpServletRequest();
        request.setMethod(HttpMethod.POST.name());
        request.setRequestURI("/servlet/lunchRecord/save");
        request.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("저장")
    void save() throws SQLException, ServletException, IOException {
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
        request.addPart(new MockPart("image", "test.png", Utils.imageToByteArray("/Users/ghc/development/img/test.png")));
        request.setParameter("price", String.valueOf(BigDecimal.ONE));
        request.setParameter("grade", String.valueOf(5.0f));

        LocalTime now = LocalTime.now();
        LunchRecord requestLunchRecord = new LunchRecord(
                maxId+1,
                restaurant,
                menu,
                new SerialBlob(Utils.imageToByteArray("/Users/ghc/development/img/test.png")),
                BigDecimal.ONE,
                5.0f,
                now,
                now
        );

        // when
        servlet.service(request, response); // 저장

        // then
        LunchRecord savedLunchRecord = repository.findById((long) (maxId + 1));
        assertThat(requestLunchRecord)
                .usingRecursiveComparison()
                .ignoringFields("averageGrade")
                .ignoringFields("updateAt")
                .ignoringFields("createAt")
                .isEqualTo(savedLunchRecord);
    }

}