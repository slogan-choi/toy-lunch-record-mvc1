package lunch.record.servlet.web.frontcontroller.controller.v3;

import lunch.record.servlet.web.frontcontroller.ControllerV3;
import lunch.record.servlet.web.frontcontroller.ModelView;
import lunch.record.servlet.web.frontcontroller.RequestInfo;

import java.util.Map;

public class LunchRecordFormController implements ControllerV3 {

    @Override
    public ModelView process(Map<String, RequestInfo> paramMap) {
        return new ModelView("form/new-form");
    }
}
