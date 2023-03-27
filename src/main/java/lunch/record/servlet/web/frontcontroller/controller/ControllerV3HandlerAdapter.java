package lunch.record.servlet.web.frontcontroller.controller;

import lunch.record.servlet.web.frontcontroller.ControllerV3;
import lunch.record.servlet.web.frontcontroller.ModelView;
import lunch.record.servlet.web.frontcontroller.MyHandlerAdapter;
import lunch.record.servlet.web.frontcontroller.RequestInfo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ControllerV3HandlerAdapter implements MyHandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        return (handler instanceof ControllerV3);
    }

    @Override
    public ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException {
        ControllerV3 controller = (ControllerV3) handler;

        Map<String, RequestInfo> paramMap = createParamMap(request);
        return controller.process(paramMap);
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
