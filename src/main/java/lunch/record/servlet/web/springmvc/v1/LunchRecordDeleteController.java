package lunch.record.servlet.web.springmvc.v1;

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
public class LunchRecordDeleteController {

    private LunchRecordRepository repository = LunchRecordRepository.getInstance();

    @RequestMapping("/springmvc/lunchRecord/v1/delete")
    public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, RequestInfo> paramMap = createParamMap(request);
        repository.delete(Integer.valueOf((String) paramMap.get("id").getInfo()));

        return new ModelAndView("delete-result");
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
