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
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LunchRecordSaveControllerTest {

    private MockMvc mockMvc;
    private LunchRecordRepository repository = LunchRecordRepository.getInstance();

    @BeforeEach
    void before() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(
                LunchRecordSaveController.class
        ).build();
    }

    @AfterEach
    void after() {
        repository.deleteAll();
    }

    @Test
    void save() throws Exception {
        LocalTime now = LocalTime.now();
        LunchRecord lunchRecord = new LunchRecord(
                getId(),
                "test",
                "test",
                new SerialBlob(Utils.imageToByteArray("/Users/ghc/development/img/test.png")),
                BigDecimal.ONE,
                5.0f,
                now,
                now
        );

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/springmvc/lunchRecords/v1/save")
                        .contentType("application/x-www-form-urlencoded")
                        .param("id", String.valueOf(lunchRecord.getId()))
                        .param("restaurant", lunchRecord.getRestaurant())
                        .param("menu", lunchRecord.getMenu())
                        .content(Utils.imageToByteArray("/Users/ghc/development/img/test.png"))
                        .param("price", lunchRecord.getPrice().toString())
                        .param("grade", lunchRecord.getGrade().toString())
                )
                .andExpect(MockMvcResultMatchers.forwardedUrl("save-result"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("lunchRecord"))
                .andReturn();

        assertThat(lunchRecord)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .ignoringFields("averageGrade")
                .ignoringFields("updateAt")
                .ignoringFields("createAt")
                .isEqualTo(mvcResult.getModelAndView().getModel().get("lunchRecord"));
    }

    private int getId() {
        List<LunchRecord> all = repository.findAll();
        int maxId = 0;
        if (!all.isEmpty()) {
            maxId = all.stream()
                    .max(Comparator.comparing(LunchRecord::getId))
                    .orElseThrow()
                    .getId();
        }
        return maxId;
    }
}