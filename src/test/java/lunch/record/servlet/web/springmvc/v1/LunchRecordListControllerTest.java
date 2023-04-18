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
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class LunchRecordListControllerTest {

    private MockMvc mockMvc;
    private LunchRecordRepository repository = LunchRecordRepository.getInstance();

    @BeforeEach
    void before() throws SQLException {
        this.mockMvc = MockMvcBuilders.standaloneSetup(
                LunchRecordListController.class
        ).build();

        saveLunchRecord();
    }

    @AfterEach
    void after() {
        repository.deleteAll();
    }

    @Test
    void lunchRecords() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/springmvc/lunchRecords/v1"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("lunchRecords"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("lunchRecords"))
                .andReturn();

        for (Object lunchRecord : (List) mvcResult.getModelAndView().getModel().get("lunchRecords")) {
            LunchRecord record = (LunchRecord) lunchRecord;
            assertThat(record).isEqualTo(repository.findById(Long.valueOf(record.getId())));
        }
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