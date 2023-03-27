package lunch.record.servlet.web.frontcontroller;

import lombok.extern.slf4j.Slf4j;
import lunch.record.servlet.web.frontcontroller.controller.ControllerV3HandlerAdapter;
import lunch.record.servlet.web.frontcontroller.controller.ControllerV4HandlerAdapter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@WebServlet(name = "frontControllerServlet", urlPatterns = "/front-controller/*")
@MultipartConfig
public class FrontControllerServlet extends HttpServlet {

    private final Map<String, Object> handlerMappingMap = new ConcurrentHashMap<>();
    private final List<MyHandlerAdapter> handlerAdapters = new ArrayList<>();



    public FrontControllerServlet() {
        initHandlerMappingMap();
        initHandlerAdapter();
    }

    private void initHandlerMappingMap() {
        handlerMappingMap.put("/front-controller/v3/lunchRecord/new-form", new lunch.record.servlet.web.frontcontroller.controller.v3.LunchRecordFormController());
        handlerMappingMap.put("/front-controller/v3/lunchRecord/save", new lunch.record.servlet.web.frontcontroller.controller.v3.LunchRecordSaveController());
        handlerMappingMap.put("/front-controller/v3/lunchRecords", new lunch.record.servlet.web.frontcontroller.controller.v3.LunchRecordListController());
        handlerMappingMap.put("/front-controller/v3/lunchRecord/update-form", new lunch.record.servlet.web.frontcontroller.controller.v3.LunchRecordUpdateFormController());
        handlerMappingMap.put("/front-controller/v3/lunchRecord/update", new lunch.record.servlet.web.frontcontroller.controller.v3.LunchRecordUpdateController());
        handlerMappingMap.put("/front-controller/v3/lunchRecord/delete-form", new lunch.record.servlet.web.frontcontroller.controller.v3.LunchRecordDeleteFormController());
        handlerMappingMap.put("/front-controller/v3/lunchRecord/delete", new lunch.record.servlet.web.frontcontroller.controller.v3.LunchRecordDeleteController());

        handlerMappingMap.put("/front-controller/v4/lunchRecord/new-form", new lunch.record.servlet.web.frontcontroller.controller.v4.LunchRecordFormController());
        handlerMappingMap.put("/front-controller/v4/lunchRecord/save", new lunch.record.servlet.web.frontcontroller.controller.v4.LunchRecordSaveController());
        handlerMappingMap.put("/front-controller/v4/lunchRecords", new lunch.record.servlet.web.frontcontroller.controller.v4.LunchRecordListController());
        handlerMappingMap.put("/front-controller/v4/lunchRecord/update-form", new lunch.record.servlet.web.frontcontroller.controller.v4.LunchRecordUpdateFormController());
        handlerMappingMap.put("/front-controller/v4/lunchRecord/update", new lunch.record.servlet.web.frontcontroller.controller.v4.LunchRecordUpdateController());
        handlerMappingMap.put("/front-controller/v4/lunchRecord/delete-form", new lunch.record.servlet.web.frontcontroller.controller.v4.LunchRecordDeleteFormController());
        handlerMappingMap.put("/front-controller/v4/lunchRecord/delete", new lunch.record.servlet.web.frontcontroller.controller.v4.LunchRecordDeleteController());
    }

    private void initHandlerAdapter() {
        handlerAdapters.add(new ControllerV3HandlerAdapter());
        handlerAdapters.add(new ControllerV4HandlerAdapter());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Object handler = getHandler(request);

        if (handler == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        MyHandlerAdapter adapter = getHandlerAdapter(handler);
        ModelView modelView = adapter.handle(request, response, handler);

        String viewName = modelView.getViewName();
        MyView view = viewResolver(viewName);
        view.render(modelView.getModel(), request, response);
    }

    private MyHandlerAdapter getHandlerAdapter(Object handler) {
        for (MyHandlerAdapter adapter : handlerAdapters) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }
        throw new IllegalArgumentException("handler adapter 를 찾을 수 없습니다. handler = " + handler);
    }

    private Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return handlerMappingMap.get(requestURI);
    }

    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

}
