package lunch.record.servlet.web.frontcontroller.controller;

import lunch.record.servlet.domain.LunchRecord;
import lunch.record.servlet.domain.LunchRecordRepository;
import lunch.record.servlet.web.frontcontroller.Controller;
import lunch.record.servlet.web.frontcontroller.ModelView;
import lunch.record.servlet.web.frontcontroller.RequestInfo;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Map;

public class LunchRecordUpdateFormController implements Controller {

    private LunchRecordRepository repository = LunchRecordRepository.getInstance();

    @Override
    public String process(Map<String, RequestInfo> paramMap, Map<String, Object> model) throws ServletException, IOException {
        LunchRecord lunchRecord = repository.findById(Long.valueOf((String) paramMap.get("id").getInfo()));

        // Model에 데이터를 담아서 보관한다.
        model.put("lunchRecord", lunchRecord);

        return "update-form";
    }
}
