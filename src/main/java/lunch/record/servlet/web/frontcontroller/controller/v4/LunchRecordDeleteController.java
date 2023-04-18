package lunch.record.servlet.web.frontcontroller.controller.v4;

import lunch.record.servlet.domain.LunchRecordRepository;
import lunch.record.servlet.web.frontcontroller.ControllerV4;
import lunch.record.servlet.web.frontcontroller.RequestInfo;

import java.util.Map;

public class LunchRecordDeleteController implements ControllerV4 {

    private LunchRecordRepository repository = LunchRecordRepository.getInstance();

    @Override
    public String process(Map<String, RequestInfo> paramMap, Map<String, Object> model) {
        repository.delete(Integer.valueOf((String) paramMap.get("id").getInfo()));

        return "delete-result";
    }
}
