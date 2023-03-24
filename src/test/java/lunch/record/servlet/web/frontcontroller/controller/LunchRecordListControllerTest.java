package lunch.record.servlet.web.frontcontroller.controller;

import lombok.extern.slf4j.Slf4j;
import lunch.record.servlet.domain.LunchRecord;
import lunch.record.servlet.domain.LunchRecordRepository;
import lunch.record.servlet.web.frontcontroller.ModelView;
import lunch.record.servlet.web.frontcontroller.MyView;
import lunch.record.servlet.web.frontcontroller.RequestInfo;
import lunch.record.util.Utils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockPart;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@SpringBootTest(classes = LunchRecordListController.class)
class LunchRecordListControllerTest {

    private static MockHttpServletRequest request = new MockHttpServletRequest();
    private static MockHttpServletResponse response = new MockHttpServletResponse();
    private static LunchRecordRepository repository = LunchRecordRepository.getInstance();

    @Autowired
    LunchRecordListController controller;

    @BeforeEach
    void before() {
        request.setMethod(HttpMethod.GET.name());
        request.setRequestURI("/front-controller/lunchRecords");
        request.setContentType(APPLICATION_JSON_VALUE);
    }

    @AfterEach
    void after() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("경로 확인")
    void checkViewPath() throws ServletException, IOException {
        // given
        // when
        ModelView mv = controller.process(createParamMap());
        viewResolver(mv.getViewName()).render(mv.getModel(), request, response);

        // then
        assertThat(response.getForwardedUrl()).isEqualTo("/WEB-INF/views/lunchRecords.jsp");
    }

    @ParameterizedTest(name = "Attribute 확인")
    @MethodSource("save")
    void checkRequestAttribute() throws ServletException, IOException {
        // given
        // when
        ModelView mv = controller.process(createParamMap());
        viewResolver(mv.getViewName()).render(mv.getModel(), request, response);

        // then
        assertThat(request.getAttribute("lunchRecords"))
                .usingRecursiveComparison()
                .ignoringFields("averageGrade")
                .ignoringFields("updateAt")
                .ignoringFields("createAt")
                .isEqualTo(repository.findAll());

    }

    private static LunchRecord saveLunchRecord() throws SQLException {
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
        return lunchRecord;
    }

    static Stream<Arguments> save() throws SQLException {
        return Stream.of(
                Arguments.arguments(saveLunchRecord())
        );
    }

    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

    private Map<String, RequestInfo> createParamMap() throws ServletException, IOException {
        Map<String, RequestInfo> paramMap = new ConcurrentHashMap<>();

        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> {
                    setParameter(request, paramMap, paramName);
                });

        for (Part part : request.getParts()) {
            setPart(request, paramMap, part.getName());
        }
        return paramMap;
    }

    private void setPart(HttpServletRequest request, Map<String, RequestInfo> paramMap, String name) throws ServletException, IOException {
        RequestInfo<Part> requestInfo = new RequestInfo<>();
        requestInfo.setInfo(request.getPart(name));
        paramMap.put(name, requestInfo);
    }

    private void setParameter(HttpServletRequest request, Map<String, RequestInfo> paramMap, String paramName) {
        RequestInfo<String> requestInfo = new RequestInfo<>();
        requestInfo.setInfo(request.getParameter(paramName));
        paramMap.put(paramName, requestInfo);
    }
}