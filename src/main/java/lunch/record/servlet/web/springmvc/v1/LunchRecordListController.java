package lunch.record.servlet.web.springmvc.v1;

import lunch.record.servlet.domain.LunchRecord;
import lunch.record.servlet.domain.LunchRecordRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class LunchRecordListController {

    private LunchRecordRepository repository = LunchRecordRepository.getInstance();

    @RequestMapping("/springmvc/lunchRecords/v1")
    public ModelAndView lunchRecords() {
        List<LunchRecord> lunchRecords = repository.findAll();

        ModelAndView modelAndView = new ModelAndView("lunchRecords-springmvc");
        modelAndView.addObject("lunchRecords", lunchRecords);
        return modelAndView;
    }
}
