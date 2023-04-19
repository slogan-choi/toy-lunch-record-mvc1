package lunch.record.servlet.web.springmvc.v1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LunchRecordFormController {

    @RequestMapping("/springmvc/lunchRecords/v1/new-form")
    public ModelAndView newForm() {
        return new ModelAndView("form/new-form");
    }
}
