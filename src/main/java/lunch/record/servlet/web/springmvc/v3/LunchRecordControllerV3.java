package lunch.record.servlet.web.springmvc.v3;

import lunch.record.servlet.domain.LunchRecord;
import lunch.record.servlet.domain.LunchRecordRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/springmvc")
public class LunchRecordControllerV3 {

    private LunchRecordRepository repository = LunchRecordRepository.getInstance();

    @GetMapping ("/lunchRecord/v3/new-form")
    public String newForm() {
        return "new-form-springmvc";
    }

    @GetMapping("/lunchRecords/v3")
    public String lunchRecords(Model model) {
        List<LunchRecord> lunchRecords = repository.findAll();

        model.addAttribute("lunchRecords", lunchRecords);
        return "lunchRecords-springmvc";
    }

    @PostMapping("/lunchRecord/v3/save")
    public String save(
            @RequestParam("restaurant") String restaurant,
            @RequestParam("menu") String menu,
            @RequestParam("image") MultipartFile image,
            @RequestParam("price") BigDecimal price,
            @RequestParam("grade") Float grade,
            Model model
    ) throws ServletException, IOException {
        Blob blob;
        try {
            blob = new SerialBlob(image.getInputStream().readAllBytes());
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        LocalTime now = LocalTime.now();

        LunchRecord lunchRecord = new LunchRecord(
                restaurant,
                menu,
                blob,
                price,
                grade,
                now,
                now
        );

        lunchRecord.setAverageGrade(getAverageGrade(lunchRecord));
        repository.save(lunchRecord);

        int maxId = repository.findAll().stream()
                .max(Comparator.comparing(LunchRecord::getId))
                .orElseThrow()
                .getId();

        LunchRecord savedLunchRecord = repository.findById((long) maxId);

        // model에 데이터를 보관한다.
        model.addAttribute("lunchRecord", savedLunchRecord);
        return "save-result-springmvc";
    }

    @PostMapping("/lunchRecord/v3/update")
    public String update(
            @RequestParam("id") Integer id,
            @RequestParam("restaurant") String restaurant,
            @RequestParam("menu") String menu,
            @RequestParam("image") MultipartFile image,
            @RequestParam("price") BigDecimal price,
            @RequestParam("grade") Float grade,
            Model model
    ) throws ServletException, IOException {
        Blob blob;
        try {
            blob = new SerialBlob(image.getInputStream().readAllBytes());
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        LocalTime now = LocalTime.now();

        LunchRecord lunchRecord = new LunchRecord(
                id,
                restaurant,
                menu,
                blob,
                price,
                grade,
                now,
                now
        );

        repository.update(
                lunchRecord.getId(),
                lunchRecord.getRestaurant(),
                lunchRecord.getMenu(),
                lunchRecord.getImage(),
                lunchRecord.getPrice(),
                lunchRecord.getGrade()
        );

        repository.updateAverageGradeByRestaurantMenu(
                getAverageGrade(lunchRecord),
                lunchRecord.getRestaurant(),
                lunchRecord.getMenu()
        );

        LunchRecord updatedLunchRecord = repository.findById(Long.valueOf(id));
        // model에 데이터를 담아서 보관한다.
        model.addAttribute("lunchRecord", updatedLunchRecord);

        return "update-springmvc";
    }

    @GetMapping("/lunchRecord/v3/update-form")
    public String updateForm(@RequestParam("id") Integer id, Model model) {
        LunchRecord lunchRecord = repository.findById(Long.valueOf(id));

        // model에 데이터를 담아서 보관한다.
        model.addAttribute("lunchRecord", lunchRecord);

        return "update-form-springmvc";
    }

    @PostMapping("/lunchRecord/v3/delete")
    public String delete(@RequestParam("id") Integer id) throws ServletException, IOException {
        repository.delete(id);

        return "delete-springmvc";
    }

    @GetMapping("/lunchRecord/v3/delete-form")
    public String deleteForm(@RequestParam("id") Integer id, Model model) {
        LunchRecord lunchRecord = repository.findById(Long.valueOf(id));

        // model에 데이터를 담아서 보관한다.
        model.addAttribute("lunchRecord", lunchRecord);

        return "delete-form-springmvc";
    }

    private Float getAverageGrade(LunchRecord lunchRecord) {
        float averageGrade;
        // 평점 획득
        Float grade = lunchRecord.getGrade();
        // 식당의 메뉴 기록 조회
        List<LunchRecord> byRestaurantMenu = repository.findByRestaurantMenu(lunchRecord.getRestaurant(), lunchRecord.getMenu());

        if (byRestaurantMenu.isEmpty()) {
            averageGrade = grade;
        } else {
            // 평점 적용
            if (lunchRecord.getId() == null) {
                // 식당의 메뉴 기록 추가
                byRestaurantMenu.add(lunchRecord);
            } else {
                byRestaurantMenu.stream()
                        .filter(it -> it.getId().equals(lunchRecord.getId()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException())
                        .setGrade(grade);
            }

            // 식당의 같은 메뉴에 대한 기록 평점의 평균을 반환
            averageGrade = (float) byRestaurantMenu.stream()
                    .mapToDouble(LunchRecord::getGrade)
                    .average()
                    .getAsDouble();
        }

        return (float) (Math.round(averageGrade * 1000) / 1000.0);
    }
}
