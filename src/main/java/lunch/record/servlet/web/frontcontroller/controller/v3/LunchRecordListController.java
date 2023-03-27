package lunch.record.servlet.web.frontcontroller.controller.v3;

import lunch.record.servlet.domain.LunchRecord;
import lunch.record.servlet.domain.LunchRecordRepository;
import lunch.record.servlet.web.frontcontroller.ControllerV3;
import lunch.record.servlet.web.frontcontroller.ModelView;
import lunch.record.servlet.web.frontcontroller.RequestInfo;

import java.util.List;
import java.util.Map;

public class LunchRecordListController implements ControllerV3 {

    private LunchRecordRepository repository = LunchRecordRepository.getInstance();

    @Override
    public ModelView process(Map<String, RequestInfo> paramMap) {
        List<LunchRecord> lunchRecords = repository.findAll();

        ModelView mv = new ModelView("lunchRecords");
        mv.getModel().put("lunchRecords", lunchRecords);

        return mv;
    }
}
