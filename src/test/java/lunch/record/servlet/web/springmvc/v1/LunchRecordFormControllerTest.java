package lunch.record.servlet.web.springmvc.v1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


class LunchRecordFormControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void before() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(
                LunchRecordFormController.class
        ).build();
    }

    @Test
    void newForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/springmvc/lunchRecord/v1/newform"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("new-form"));
    }
}