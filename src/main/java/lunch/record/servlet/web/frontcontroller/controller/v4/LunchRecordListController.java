package lunch.record.servlet.web.frontcontroller.controller.v4;

import lunch.record.servlet.domain.LunchRecord;
import lunch.record.servlet.domain.LunchRecordRepository;
import lunch.record.servlet.web.frontcontroller.ControllerV4;
import lunch.record.servlet.web.frontcontroller.RequestInfo;

import java.util.List;
import java.util.Map;

public class LunchRecordListController implements ControllerV4 {

    private LunchRecordRepository repository = LunchRecordRepository.getInstance();

    @Override
    public String process(Map<String, RequestInfo> paramMap, Map<String, Object> model) {
        List<LunchRecord> lunchRecords = repository.findAll();
        model.put("lunchRecords", lunchRecords);

        return "lunchRecords";
    }
}
