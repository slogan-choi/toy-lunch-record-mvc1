package lunch.record.servlet.web.frontcontroller.controller.v3;

import lunch.record.servlet.domain.LunchRecordRepository;
import lunch.record.servlet.web.frontcontroller.ControllerV3;
import lunch.record.servlet.web.frontcontroller.ModelView;
import lunch.record.servlet.web.frontcontroller.RequestInfo;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Map;

public class LunchRecordDeleteController implements ControllerV3 {

    private LunchRecordRepository repository = LunchRecordRepository.getInstance();

    @Override
    public ModelView process(Map<String, RequestInfo> paramMap) {
        repository.delete(Integer.valueOf((String) paramMap.get("id").getInfo()));
        ModelView mv = new ModelView("delete");
        return mv;
    }
}
