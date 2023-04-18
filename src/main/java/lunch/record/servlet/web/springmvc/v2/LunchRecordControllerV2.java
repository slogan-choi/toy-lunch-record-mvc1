package lunch.record.servlet.web.springmvc.v2;

import lunch.record.servlet.domain.LunchRecord;
import lunch.record.servlet.domain.LunchRecordRepository;
import lunch.record.servlet.web.frontcontroller.RequestInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequestMapping("/springmvc")
public class LunchRecordControllerV2 {

    private LunchRecordRepository repository = LunchRecordRepository.getInstance();

    @RequestMapping("/lunchRecord/v2/newform")
    public ModelAndView newForm() {
        return new ModelAndView("new-form");
    }

    @RequestMapping("/lunchRecords/v2")
    public ModelAndView lunchRecords() {
        List<LunchRecord> lunchRecords = repository.findAll();

        ModelAndView modelAndView = new ModelAndView("lunchRecords");
        modelAndView.addObject("lunchRecords", lunchRecords);
        return modelAndView;
    }

    @RequestMapping("/lunchRecord/v2/save")
    public ModelAndView save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, RequestInfo> paramMap = createParamMap(request);
        Blob blob;
        try {
            blob = new SerialBlob(request.getInputStream().readAllBytes());
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        LocalTime now = LocalTime.now();

        LunchRecord lunchRecord = new LunchRecord(
                (String) paramMap.get("restaurant").getInfo(),
                (String) paramMap.get("menu").getInfo(),
                blob,
                BigDecimal.valueOf(Long.parseLong((String) paramMap.get("price").getInfo())),
                Float.parseFloat((String) paramMap.get("grade").getInfo()),
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

        // ModelAndView에 데이터를 보관한다.
        ModelAndView modelAndView = new ModelAndView("save-result");
        modelAndView.addObject("lunchRecord", savedLunchRecord);

        return modelAndView;
    }

    @RequestMapping("/lunchRecord/v2/update")
    public ModelAndView update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, RequestInfo> paramMap = createParamMap(request);
        Blob blob;
        try {
            blob = new SerialBlob(request.getInputStream().readAllBytes());
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        LocalTime now = LocalTime.now();

        LunchRecord lunchRecord = new LunchRecord(
                Integer.parseInt((String) paramMap.get("id").getInfo()),
                (String) paramMap.get("restaurant").getInfo(),
                (String) paramMap.get("menu").getInfo(),
                blob,
                BigDecimal.valueOf(Long.parseLong((String) paramMap.get("price").getInfo())),
                Float.parseFloat((String) paramMap.get("grade").getInfo()),
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

        LunchRecord updatedLunchRecord = repository.findById(Long.valueOf((String) paramMap.get("id").getInfo()));
        // Model에 데이터를 담아서 보관한다.
        ModelAndView modelAndView = new ModelAndView("update-result");
        modelAndView.addObject("lunchRecord", updatedLunchRecord);

        return modelAndView;
    }

    @RequestMapping("/lunchRecord/v2/updateform")
    public ModelAndView updateForm(HttpServletRequest request, HttpServletResponse response) {
        Map<String, RequestInfo> paramMap = createParamMap(request);
        LunchRecord lunchRecord = repository.findById(Long.valueOf((String) paramMap.get("id").getInfo()));

        // ModelAndView에 데이터를 담아서 보관한다.
        ModelAndView modelAndView = new ModelAndView("update-form");
        modelAndView.addObject("lunchRecord", lunchRecord);

        return modelAndView;
    }

    @RequestMapping("/lunchRecord/v2/delete")
    public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, RequestInfo> paramMap = createParamMap(request);
        repository.delete(Integer.valueOf((String) paramMap.get("id").getInfo()));

        return new ModelAndView("delete-result");
    }

    @RequestMapping("/lunchRecord/v2/deleteform")
    public ModelAndView deleteForm(HttpServletRequest request, HttpServletResponse response) {
        Map<String, RequestInfo> paramMap = createParamMap(request);
        LunchRecord lunchRecord = repository.findById(Long.valueOf((String) paramMap.get("id").getInfo()));

        // ModelAndView에 데이터를 담아서 보관한다.
        ModelAndView modelAndView = new ModelAndView("delete-form");
        modelAndView.addObject("lunchRecord", lunchRecord);

        return modelAndView;
    }

    private Map<String, RequestInfo> createParamMap(HttpServletRequest request) {
        Map<String, RequestInfo> paramMap = new ConcurrentHashMap<>();

        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> {
                    setParameter(request, paramMap, paramName);
                });

        return paramMap;
    }

    private void setParameter(HttpServletRequest request, Map<String, RequestInfo> paramMap, String paramName) {
        RequestInfo<String> requestInfo = new RequestInfo<>();
        requestInfo.setInfo(request.getParameter(paramName));
        paramMap.put(paramName, requestInfo);
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
