package lunch.record.controller;

import lunch.record.domain.LunchRecord;
import lunch.record.domain.LunchRecordRepository;
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
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LunchRecordControllerTest {

    private MockMvc mockMvc;
    private LunchRecordRepository repository = LunchRecordRepository.getInstance();

    @BeforeEach
    void before() throws SQLException, IOException {
        this.mockMvc = MockMvcBuilders.standaloneSetup(
                LunchRecordController.class
        ).build();
        saveLunchRecord();
    }

    @Test
    void newForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/springmvc/lunchRecords/new"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("form/new-form"));
    }

    @Test
    void lunchRecords() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/springmvc/lunchRecords"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("lunch-records"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("lunchRecords"))
                .andReturn();

        for (Object lunchRecord : (List) mvcResult.getModelAndView().getModel().get("lunchRecords")) {
            LunchRecord record = (LunchRecord) lunchRecord;
            assertThat(record).isEqualTo(repository.findById((long) record.getId()));
        }
    }

    @Test
    void save() throws Exception {
        LocalDateTime now = LocalDateTime.now();
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

        String expectedUrl = "/springmvc/lunchRecords/new/" + (lunchRecord.getId() + 1);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart("/springmvc/lunchRecords/new")
                        .file(new MockMultipartFile("image", "test", "png", new FileInputStream("/Users/ghc/development/img/test.png")))
                        .param("id", String.valueOf(lunchRecord.getId()))
                        .param("restaurant", lunchRecord.getRestaurant())
                        .param("menu", lunchRecord.getMenu())
                        .param("price", lunchRecord.getPrice().toString())
                        .param("grade", lunchRecord.getGrade().toString())
                )
                .andExpect(MockMvcResultMatchers.redirectedUrl(expectedUrl))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("lunchRecord"))
                .andReturn();

        assertThat(convert(lunchRecord))
                .usingRecursiveComparison()
                .ignoringFields("id")
                .ignoringFields("averageGrade")
                .ignoringFields("updateAt")
                .ignoringFields("createAt")
                .isEqualTo(mvcResult.getFlashMap().get("lunchRecord"));

        mockMvc.perform(MockMvcRequestBuilders.get(expectedUrl))
                .andExpect(MockMvcResultMatchers.forwardedUrl("save-result"));
    }

    @Test
    void update() throws Exception {
        LunchRecord lunchRecord = repository.findById((long) getId());

        String restaurant = "testtest";
        String expectedUrl = "/springmvc/lunchRecords/edit/" + (lunchRecord.getId());
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.multipart("/springmvc/lunchRecords/{id}/edit",lunchRecord.getId())
                        .file(new MockMultipartFile("image", "test", "png", new FileInputStream("/Users/ghc/development/img/test.png")))
                        .param("id", String.valueOf(lunchRecord.getId()))
                        .param("restaurant", restaurant)
                        .param("menu", lunchRecord.getMenu())
                        .param("price", lunchRecord.getPrice().toString())
                        .param("grade", lunchRecord.getGrade().toString())
                )
                .andExpect(MockMvcResultMatchers.redirectedUrl(expectedUrl))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("lunchRecord"))
                .andReturn();

        assertThat(((LunchRecord) mvcResult.getFlashMap().get("lunchRecord")).getRestaurant())
                .isEqualTo(restaurant);

        this.mockMvc.perform(MockMvcRequestBuilders.get(expectedUrl))
                .andExpect(MockMvcResultMatchers.forwardedUrl("update-result"));
    }

    @Test
    void updateForm() throws Exception {
        LunchRecord lunchRecord = repository.findById((long) getId());

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/springmvc/lunchRecords/edit")
                        .param("id", String.valueOf(lunchRecord.getId()))
                )
                .andExpect(MockMvcResultMatchers.forwardedUrl("form/update-form"))
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
        LunchRecord lunchRecord = repository.findById((long) getId());
        String expectedUrl = "/springmvc/lunchRecords/delete/" + (lunchRecord.getId());
        this.mockMvc.perform(MockMvcRequestBuilders.post("/springmvc/lunchRecords/{id}/delete", lunchRecord.getId())
                        .param("id", String.valueOf(lunchRecord.getId()))
                )
                .andExpect(MockMvcResultMatchers.redirectedUrl(expectedUrl));

        this.mockMvc.perform(MockMvcRequestBuilders.get(expectedUrl))
                .andExpect(MockMvcResultMatchers.forwardedUrl("delete-result"));
    }

    @Test
    void deleteForm() throws Exception {
        LunchRecord lunchRecord = repository.findById((long) getId());
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/springmvc/lunchRecords/delete")
                        .param("id", String.valueOf(lunchRecord.getId()))
                )
                .andExpect(MockMvcResultMatchers.forwardedUrl("form/delete-form"))
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

    private void saveLunchRecord() throws SQLException, IOException {
        LocalDateTime now = LocalDateTime.now();
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

    private int getId() throws SQLException, IOException {
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

    private <T> T convert(T lunchRecords) throws SQLException, IOException {
        if (lunchRecords.getClass() == LunchRecord.class) {
            LunchRecord<Blob> storedLunchRecord = (LunchRecord) lunchRecords;
            LunchRecord<String> lunchRecord = new LunchRecord<>();

            lunchRecord.setId(storedLunchRecord.getId());
            lunchRecord.setRestaurant(storedLunchRecord.getRestaurant());
            lunchRecord.setMenu(storedLunchRecord.getMenu());
            lunchRecord.setImage(new String(Base64.getEncoder().encode(storedLunchRecord.getImage().getBinaryStream().readAllBytes()), StandardCharsets.UTF_8));
            lunchRecord.setGrade(storedLunchRecord.getGrade());
            lunchRecord.setPrice(storedLunchRecord.getPrice());
            lunchRecord.setAverageGrade(storedLunchRecord.getAverageGrade());
            lunchRecord.setUpdateAt(storedLunchRecord.getUpdateAt());
            lunchRecord.setCreateAt(storedLunchRecord.getCreateAt());

            return (T) lunchRecord;
        } else {
            Collection<LunchRecord> collection = new ArrayList<>();
            for (LunchRecord<Blob> storedLunchRecord : (Collection<LunchRecord>) lunchRecords) {
                LunchRecord<String> lunchRecord = new LunchRecord<>();

                lunchRecord.setId(storedLunchRecord.getId());
                lunchRecord.setRestaurant(storedLunchRecord.getRestaurant());
                lunchRecord.setMenu(storedLunchRecord.getMenu());
                lunchRecord.setImage(new String(Base64.getEncoder().encode(storedLunchRecord.getImage().getBinaryStream().readAllBytes()), StandardCharsets.UTF_8));
                lunchRecord.setGrade(storedLunchRecord.getGrade());
                lunchRecord.setPrice(storedLunchRecord.getPrice());
                lunchRecord.setAverageGrade(storedLunchRecord.getAverageGrade());
                lunchRecord.setUpdateAt(storedLunchRecord.getUpdateAt());
                lunchRecord.setCreateAt(storedLunchRecord.getCreateAt());

                collection.add(lunchRecord);
            }
            return (T) collection;
        }
    }
}