package lunch.record.servlet.web.frontcontroller.controller;

import lombok.extern.slf4j.Slf4j;

import lunch.record.servlet.web.frontcontroller.MyView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@SpringBootTest(classes = LunchRecordFormController.class)
class LunchRecordFormControllerTest {

    MockHttpServletRequest request;
    MockHttpServletResponse response;

    @Autowired
    LunchRecordFormController controller;

    @BeforeEach
    void before() {
        request = new MockHttpServletRequest();
        request.setMethod(HttpMethod.GET.name());
        request.setRequestURI("/front-controller/lunchRecord/new-form");
        request.setContentType(APPLICATION_JSON_VALUE);

        response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("경로 확인")
    void checkViewPath() throws ServletException, IOException {
        // given
        // when
        MyView myView = controller.process(request, response);
        myView.render(request, response);

        // then
        assertThat(response.getForwardedUrl()).isEqualTo("/WEB-INF/views/new-form.jsp");
    }

}