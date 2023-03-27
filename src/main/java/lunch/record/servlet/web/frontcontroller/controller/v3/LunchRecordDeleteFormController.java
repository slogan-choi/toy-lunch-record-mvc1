package lunch.record.servlet.web.frontcontroller.controller.v3;

import lunch.record.servlet.domain.LunchRecord;
import lunch.record.servlet.domain.LunchRecordRepository;
import lunch.record.servlet.web.frontcontroller.ControllerV3;
import lunch.record.servlet.web.frontcontroller.ModelView;
import lunch.record.servlet.web.frontcontroller.RequestInfo;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Map;

public class LunchRecordDeleteFormController implements ControllerV3 {

    private LunchRecordRepository repository = LunchRecordRepository.getInstance();

    @Override
    public ModelView process(Map<String, RequestInfo> paramMap) {
        LunchRecord lunchRecord = repository.findById(Long.valueOf((String) paramMap.get("id").getInfo()));

        // Model에 데이터를 담아서 보관한다.
        ModelView mv = new ModelView("delete-form");
        mv.getModel().put("lunchRecord", lunchRecord);

        return mv;
    }
}
