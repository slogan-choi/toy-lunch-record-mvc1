package lunch.record.servlet.web.springmvc.v1;

import lunch.record.servlet.domain.LunchRecord;
import lunch.record.servlet.domain.LunchRecordRepository;
import lunch.record.util.Utils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.sql.rowset.serial.SerialBlob;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LunchRecordDeleteControllerTest {

    private MockMvc mockMvc;
    private LunchRecordRepository repository = LunchRecordRepository.getInstance();

    @BeforeEach
    void before() throws SQLException {
        this.mockMvc = MockMvcBuilders.standaloneSetup(
                LunchRecordDeleteController.class
        ).build();
        saveLunchRecord();
    }

    @AfterEach
    void after() {
        repository.deleteAll();
    }

    @Test
    void delete() throws Exception {
        LunchRecord lunchRecord = repository.findById(Long.valueOf(getId()));
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/springmvc/lunchRecord/v1/delete")
                        .param("id", String.valueOf(lunchRecord.getId()))
                )
                .andExpect(MockMvcResultMatchers.forwardedUrl("delete-springmvc"));
    }

    private void saveLunchRecord() throws SQLException {
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
        repository.save(lunchRecord);
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