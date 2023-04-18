package lunch.record.servlet.web.springmvc.v1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LunchRecordFormController {

    @RequestMapping("/springmvc/lunchRecord/v1/newform")
    public ModelAndView newForm() {
        return new ModelAndView("new-form");
    }
}
