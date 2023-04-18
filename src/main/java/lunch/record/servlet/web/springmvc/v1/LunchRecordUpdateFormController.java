package lunch.record.servlet.web.springmvc.v1;

import lunch.record.servlet.domain.LunchRecord;
import lunch.record.servlet.domain.LunchRecordRepository;
import lunch.record.servlet.web.frontcontroller.RequestInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class LunchRecordUpdateFormController {

    private LunchRecordRepository repository = LunchRecordRepository.getInstance();

    @RequestMapping("/springmvc/lunchRecord/v1/update-form")
    public ModelAndView updateForm(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("LunchRecordUpdateFormController.updateForm");
        Map<String, RequestInfo> paramMap = createParamMap(request);
        LunchRecord lunchRecord = repository.findById(Long.valueOf((String) paramMap.get("id").getInfo()));

        // ModelAndView에 데이터를 담아서 보관한다.
        ModelAndView modelAndView = new ModelAndView("update-form-springmvc");
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
}
