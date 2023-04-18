package lunch.record.servlet.web.springmvc.v3;

import lunch.record.servlet.domain.LunchRecord;
import lunch.record.servlet.domain.LunchRecordRepository;
import lunch.record.util.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.sql.rowset.serial.SerialBlob;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

class LunchRecordControllerV3Test {

    private MockMvc mockMvc;
    private LunchRecordRepository repository = LunchRecordRepository.getInstance();

    @BeforeEach
    void before() throws SQLException {
        this.mockMvc = MockMvcBuilders.standaloneSetup(
                LunchRecordControllerV3.class
        ).build();
        saveLunchRecord();
    }

    @Test
    void newForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/springmvc/lunchRecord/v3/new-form"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("new-form-springmvc"));
    }

    @Test
    void lunchRecords() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/springmvc/lunchRecords/v3"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("lunchRecords-springmvc"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("lunchRecords"))
                .andReturn();

        for (Object lunchRecord : (List) mvcResult.getModelAndView().getModel().get("lunchRecords")) {
            LunchRecord record = (LunchRecord) lunchRecord;
            assertThat(record).isEqualTo(repository.findById(Long.valueOf(record.getId())));
        }
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

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart("/springmvc/lunchRecord/v3/save")
                        .file(new MockMultipartFile("image", "test", "png", new FileInputStream("/Users/ghc/development/img/test.png")))
                        .param("id", String.valueOf(lunchRecord.getId()))
                        .param("restaurant", lunchRecord.getRestaurant())
                        .param("menu", lunchRecord.getMenu())
                        .param("price", lunchRecord.getPrice().toString())
                        .param("grade", lunchRecord.getGrade().toString())
                )
                .andExpect(MockMvcResultMatchers.forwardedUrl("save-result-springmvc"))
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

    @Test
    void update() throws Exception {
        LunchRecord lunchRecord = repository.findById(Long.valueOf(getId()));

        String restaurant = "testtest";
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.multipart("/springmvc/lunchRecord/v3/update")
                        .file(new MockMultipartFile("image", "test", "png", new FileInputStream("/Users/ghc/development/img/test.png")))
                        .param("id", String.valueOf(lunchRecord.getId()))
                        .param("restaurant", restaurant)
                        .param("menu", lunchRecord.getMenu())
                        .param("price", lunchRecord.getPrice().toString())
                        .param("grade", lunchRecord.getGrade().toString())
                )
                .andExpect(MockMvcResultMatchers.forwardedUrl("update-springmvc"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("lunchRecord"))
                .andReturn();

        assertThat(((LunchRecord) mvcResult.getModelAndView().getModel().get("lunchRecord")).getRestaurant())
                .isEqualTo(restaurant);
    }

    @Test
    void updateForm() throws Exception {
        LunchRecord lunchRecord = repository.findById(Long.valueOf(getId()));

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/springmvc/lunchRecord/v3/update-form")
                        .param("id", String.valueOf(lunchRecord.getId()))
                )
                .andExpect(MockMvcResultMatchers.forwardedUrl("update-form-springmvc"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("lunchRecord"))
                .andReturn();

        assertThat(lunchRecord)
                .usingRecursiveComparison()
                .ignoringFields("averageGrade")
                .ignoringFields("updateAt")
                .ignoringFields("createAt")
                .isEqualTo((LunchRecord) mvcResult.getModelAndView().getModel().get("lunchRecord"));
    }

    @Test
    void delete() throws Exception {
        LunchRecord lunchRecord = repository.findById(Long.valueOf(getId()));
        this.mockMvc.perform(MockMvcRequestBuilders.post("/springmvc/lunchRecord/v3/delete")
                        .param("id", String.valueOf(lunchRecord.getId()))
                )
                .andExpect(MockMvcResultMatchers.forwardedUrl("delete-springmvc"));
    }

    @Test
    void deleteForm() throws Exception {
        LunchRecord lunchRecord = repository.findById(Long.valueOf(getId()));
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/springmvc/lunchRecord/v3/delete-form")
                        .param("id", String.valueOf(lunchRecord.getId()))
                )
                .andExpect(MockMvcResultMatchers.forwardedUrl("delete-form-springmvc"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("lunchRecord"))
                .andReturn();
        ;
        assertThat(lunchRecord)
                .usingRecursiveComparison()
                .ignoringFields("averageGrade")
                .ignoringFields("updateAt")
                .ignoringFields("createAt")
                .isEqualTo((LunchRecord) mvcResult.getModelAndView().getModel().get("lunchRecord"));
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