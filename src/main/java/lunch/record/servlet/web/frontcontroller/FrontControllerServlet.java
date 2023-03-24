package lunch.record.servlet.web.frontcontroller;

import lombok.extern.slf4j.Slf4j;
import lunch.record.servlet.web.frontcontroller.controller.LunchRecordDeleteController;
import lunch.record.servlet.web.frontcontroller.controller.LunchRecordDeleteFormController;
import lunch.record.servlet.web.frontcontroller.controller.LunchRecordFormController;
import lunch.record.servlet.web.frontcontroller.controller.LunchRecordListController;
import lunch.record.servlet.web.frontcontroller.controller.LunchRecordSaveController;
import lunch.record.servlet.web.frontcontroller.controller.LunchRecordUpdateController;
import lunch.record.servlet.web.frontcontroller.controller.LunchRecordUpdateFormController;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@WebServlet(name = "frontControllerServlet", urlPatterns = "/front-controller/*")
@MultipartConfig
public class FrontControllerServlet extends HttpServlet {

    private Map<String, Controller> controllerMap = new ConcurrentHashMap<>();

    public FrontControllerServlet() {
        controllerMap.put("/front-controller/lunchRecord/new-form", new LunchRecordFormController());
        controllerMap.put("/front-controller/lunchRecord/save", new LunchRecordSaveController());
        controllerMap.put("/front-controller/lunchRecords", new LunchRecordListController());
        controllerMap.put("/front-controller/lunchRecord/update-form", new LunchRecordUpdateFormController());
        controllerMap.put("/front-controller/lunchRecord/update", new LunchRecordUpdateController());
        controllerMap.put("/front-controller/lunchRecord/delete-form", new LunchRecordDeleteFormController());
        controllerMap.put("/front-controller/lunchRecord/delete", new LunchRecordDeleteController());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        Controller controller = controllerMap.get(requestURI);
        if (controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Map<String, RequestInfo> paramMap = createParamMap(request);
        ModelView mv = controller.process(paramMap);

        String viewName = mv.getViewName();
        MyView view = viewResolver(viewName);
        view.render(mv.getModel(), request, response);
    }

    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
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
