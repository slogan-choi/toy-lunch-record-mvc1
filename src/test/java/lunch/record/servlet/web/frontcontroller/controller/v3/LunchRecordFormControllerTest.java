package lunch.record.servlet.web.frontcontroller.controller.v3;

import lombok.extern.slf4j.Slf4j;

import lunch.record.servlet.web.frontcontroller.ModelView;
import lunch.record.servlet.web.frontcontroller.MyView;
import lunch.record.servlet.web.frontcontroller.RequestInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@SpringBootTest(classes = LunchRecordFormController.class)
class LunchRecordFormControllerTest {

    private static MockHttpServletRequest request = new MockHttpServletRequest();
    private static MockHttpServletResponse response = new MockHttpServletResponse();

    @Autowired
    LunchRecordFormController controller;

    @BeforeEach
    void before() {
        request.setMethod(HttpMethod.GET.name());
        request.setRequestURI("/front-controller/v3/lunchRecord/new-form");
        request.setContentType(APPLICATION_JSON_VALUE);
    }

    @Test
    @DisplayName("경로 확인")
    void checkViewPath() throws ServletException, IOException {
        // given
        // when
        ModelView mv = controller.process(createParamMap());
        viewResolver(mv.getViewName()).render(mv.getModel(), request, response);

        // then
        assertThat(response.getForwardedUrl()).isEqualTo("/WEB-INF/views/new-form.jsp");
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