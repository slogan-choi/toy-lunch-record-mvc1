package lunch.record.servlet.web.frontcontroller.controller.v4;

import lunch.record.servlet.web.frontcontroller.ControllerV4;
import lunch.record.servlet.web.frontcontroller.RequestInfo;

import java.util.Map;

public class LunchRecordFormController implements ControllerV4 {

    @Override
    public String process(Map<String, RequestInfo> paramMap, Map<String, Object> model) {
        return "form/new-form";
    }
}
