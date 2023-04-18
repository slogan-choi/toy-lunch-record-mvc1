package lunch.record.servlet.web.springmvc.v1;

import lunch.record.servlet.domain.LunchRecord;
import lunch.record.servlet.domain.LunchRecordRepository;
import lunch.record.util.Utils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.sql.rowset.serial.SerialBlob;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LunchRecordUpdateControllerTest {

    private MockMvc mockMvc;
    private LunchRecordRepository repository = LunchRecordRepository.getInstance();

    @BeforeEach
    void before() throws SQLException {
        this.mockMvc = MockMvcBuilders.standaloneSetup(
                LunchRecordUpdateController.class
        ).build();

        saveLunchRecord();
    }

    @AfterEach
    void after() {
        repository.deleteAll();
    }

    @Test
    void update() throws Exception {
        LunchRecord lunchRecord = repository.findById(1L);

        String restaurant = "testtest";
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/springmvc/lunchRecord/v1/update")
                        .contentType("application/x-www-form-urlencoded")
                        .param("id", String.valueOf(lunchRecord.getId()))
                        .param("restaurant", restaurant)
                        .param("menu", lunchRecord.getMenu())
                        .content(Utils.imageToByteArray("/Users/ghc/development/img/test.png"))
                        .param("price", lunchRecord.getPrice().toString())
                        .param("grade", lunchRecord.getGrade().toString())
                )
                .andExpect(MockMvcResultMatchers.forwardedUrl("update-result"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("lunchRecord"))
                .andReturn();

        assertThat(((LunchRecord) mvcResult.getModelAndView().getModel().get("lunchRecord")).getRestaurant())
                .isEqualTo(restaurant);
    }

    private void saveLunchRecord() throws SQLException {
        LocalTime now = LocalTime.now();
        LunchRecord lunchRecord = new LunchRecord(
                1,
                "test",
                "test",
                new SerialBlob(Utils.imageToByteArray("/Users/ghc/development/img/test.png")),
                BigDecimal.ONE,
                5.0f,
                now,
                now
        );
        repository.save(lunchRecord);
    }
}