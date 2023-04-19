package lunch.record.servlet.web.springmvc.v1;

import lunch.record.servlet.domain.LunchRecord;
import lunch.record.servlet.domain.LunchRecordRepository;
import lunch.record.servlet.web.frontcontroller.RequestInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class LunchRecordDeleteFormController {

    private LunchRecordRepository repository = LunchRecordRepository.getInstance();

    @RequestMapping("/springmvc/lunchRecords/v1/delete-form")
    public ModelAndView deleteForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, RequestInfo> paramMap = createParamMap(request);
        LunchRecord lunchRecord = repository.findById(Long.valueOf((String) paramMap.get("id").getInfo()));

        // ModelAndView에 데이터를 담아서 보관한다.
        ModelAndView modelAndView = new ModelAndView("form/delete-form");
        modelAndView.addObject("lunchRecord", lunchRecord);

        return modelAndView;
    }

    private Map<String, RequestInfo> createParamMap(HttpServletRequest request) throws ServletException, IOException {
        Map<String, RequestInfo> paramMap = new ConcurrentHashMap<>();

        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> {
                    setParameter(request, paramMap, paramName);
                });

        String contentType = request.getContentType();
        if (contentType != null) {
            if (contentType.contains("multipart/")) {
                for (Part part : request.getParts()) {
                    if (part.getContentType() != null) {
                        setPart(request, paramMap, part.getName());
                    }
                }
            }
        }
        return paramMap;
    }

    private void setPart(HttpServletRequest request, Map<String, RequestInfo> paramMap, String name) throws ServletException, IOException {
        RequestInfo<Part> requestInfo = new RequestInfo<>();
        requestInfo.setInfo(request.getPart(name));
        paramMap.put(name, requestInfo);
    }

    private void setParameter(HttpServletRequest request, Map<String, RequestInfo> paramMap, String paramName) {
        RequestInfo<String> requestInfo = new RequestInfo<>();
        requestInfo.setInfo(request.getParameter(paramName));
        paramMap.put(paramName, requestInfo);
    }
}
