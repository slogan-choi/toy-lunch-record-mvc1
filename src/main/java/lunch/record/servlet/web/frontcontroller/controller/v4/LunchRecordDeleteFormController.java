package lunch.record.servlet.web.frontcontroller.controller.v4;

import lunch.record.servlet.domain.LunchRecord;
import lunch.record.servlet.domain.LunchRecordRepository;
import lunch.record.servlet.web.frontcontroller.ControllerV4;
import lunch.record.servlet.web.frontcontroller.RequestInfo;

import java.util.Map;

public class LunchRecordDeleteFormController implements ControllerV4 {

    private LunchRecordRepository repository = LunchRecordRepository.getInstance();

    @Override
    public String process(Map<String, RequestInfo> paramMap, Map<String, Object> model) {
        LunchRecord lunchRecord = repository.findById(Long.valueOf((String) paramMap.get("id").getInfo()));

        // Model에 데이터를 담아서 보관한다.
        model.put("lunchRecord", lunchRecord);

        return "form/delete-form";
    }
}
