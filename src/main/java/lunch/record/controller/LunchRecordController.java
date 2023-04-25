package lunch.record.controller;

import lombok.extern.slf4j.Slf4j;
import lunch.record.domain.LunchRecord;
import lunch.record.domain.LunchRecordRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/springmvc/lunchRecords")
@Slf4j
public class LunchRecordController {

    private LunchRecordRepository repository = LunchRecordRepository.getInstance();

    @GetMapping ("/new")
    public String newForm() {
        return "form/new-form";
    }

    @GetMapping("/new/{id}")
    String lunchRecord(@PathVariable Integer id, Model model) throws SQLException, IOException {
        LunchRecord lunchRecord = repository.findById((long) id);

        model.addAttribute("lunchRecords", lunchRecord);
        return "save-result";
    }

    @GetMapping("/edit/{id}")
    String update(@PathVariable Integer id, Model model) throws SQLException, IOException {
        LunchRecord lunchRecord = repository.findById((long) id);

        model.addAttribute("lunchRecords", lunchRecord);
        return "update-result";
    }

    @GetMapping("/delete/{id}")
    String delete(@PathVariable Integer id, Model model) {
        LunchRecord lunchRecord = new LunchRecord();
        lunchRecord.setId(id);
        model.addAttribute("lunchRecords", lunchRecord);
        return "delete-result";
    }

    @GetMapping
    public String lunchRecords(Model model) throws SQLException, IOException {
        List<LunchRecord> lunchRecords = repository.findAll();

        model.addAttribute("lunchRecords", lunchRecords);
        return "lunch-records";
    }

    @PostMapping("/new")
    public String save(
            @ModelAttribute LunchRecord<MultipartFile> lunchRecord,
            RedirectAttributes redirectAttributes
    ) throws ServletException, IOException, SQLException {
        Blob blob;
        try {
            blob = new SerialBlob(lunchRecord.getImage().getInputStream().readAllBytes());
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        lunchRecord.setAverageGrade(getAverageGrade(lunchRecord));
        LocalDateTime now = LocalDateTime.now();
        repository.save(new LunchRecord(
                lunchRecord.getRestaurant(),
                lunchRecord.getMenu(),
                blob,
                lunchRecord.getPrice(),
                lunchRecord.getGrade(),
                now,
                now
        ));

        int maxId = repository.findAll().stream()
                .max(Comparator.comparing(LunchRecord::getId))
                .orElseThrow()
                .getId();

        repository.updateAverageGradeByRestaurantMenu(
                getAverageGrade(new LunchRecord(
                        maxId,
                        lunchRecord.getRestaurant(),
                        lunchRecord.getMenu(),
                        blob,
                        lunchRecord.getPrice(),
                        lunchRecord.getGrade(),
                        now,
                        now
                )),
                lunchRecord.getRestaurant(),
                lunchRecord.getMenu()
        );
        LunchRecord<String> savedLunchRecord = repository.findById((long) maxId);

        // model에 데이터를 보관한다.
        redirectAttributes.addFlashAttribute("lunchRecord", savedLunchRecord);
        redirectAttributes.addAttribute("id", savedLunchRecord.getId());
        return "redirect:/springmvc/lunchRecords/new/{id}";
    }

    @PostMapping("/{id}/edit")
    public String update(
            @PathVariable Integer id,
            @ModelAttribute LunchRecord<MultipartFile> lunchRecord,
            RedirectAttributes redirectAttributes
    ) throws ServletException, IOException, SQLException {
        Blob blob;
        try {
            blob = new SerialBlob(lunchRecord.getImage().getInputStream().readAllBytes());
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        repository.update(
                id,
                lunchRecord.getRestaurant(),
                lunchRecord.getMenu(),
                blob,
                lunchRecord.getPrice(),
                lunchRecord.getGrade()
        );

        LocalDateTime now = LocalDateTime.now();
        repository.updateAverageGradeByRestaurantMenu(
                getAverageGrade(new LunchRecord(
                        id,
                        lunchRecord.getRestaurant(),
                        lunchRecord.getMenu(),
                        blob,
                        lunchRecord.getPrice(),
                        lunchRecord.getGrade(),
                        now,
                        now
                )),
                lunchRecord.getRestaurant(),
                lunchRecord.getMenu()
        );
        LunchRecord updatedLunchRecord = repository.findById((long) id);
        // model에 데이터를 담아서 보관한다.
        redirectAttributes.addFlashAttribute("lunchRecord", updatedLunchRecord);
        redirectAttributes.addAttribute("id", updatedLunchRecord.getId());

        return "redirect:/springmvc/lunchRecords/edit/{id}";
    }

    @GetMapping("/edit")
    public String updateForm(@RequestParam Integer id, Model model) throws SQLException, IOException {
        LunchRecord lunchRecord = repository.findById((long) id);

        // model에 데이터를 담아서 보관한다.
        model.addAttribute("lunchRecord", lunchRecord);

        return "form/update-form";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        repository.delete(id);
        redirectAttributes.addAttribute("id", id);

        return "redirect:/springmvc/lunchRecords/delete/{id}";
    }

    @GetMapping("/delete")
    public String deleteForm(@RequestParam Integer id, Model model) throws SQLException, IOException {
        LunchRecord lunchRecord = repository.findById((long) id);

        // model에 데이터를 담아서 보관한다.
        model.addAttribute("lunchRecord", lunchRecord);

        return "form/delete-form";
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
