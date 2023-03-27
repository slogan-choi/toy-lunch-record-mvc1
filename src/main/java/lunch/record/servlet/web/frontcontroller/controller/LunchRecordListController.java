package lunch.record.servlet.web.frontcontroller.controller;

import lunch.record.servlet.domain.LunchRecord;
import lunch.record.servlet.domain.LunchRecordRepository;
import lunch.record.servlet.web.frontcontroller.Controller;
import lunch.record.servlet.web.frontcontroller.ModelView;
import lunch.record.servlet.web.frontcontroller.RequestInfo;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class LunchRecordListController implements Controller {

    private LunchRecordRepository repository = LunchRecordRepository.getInstance();

    @Override
    public String process(Map<String, RequestInfo> paramMap, Map<String, Object> model) throws ServletException, IOException {
        List<LunchRecord> lunchRecords = repository.findAll();
        model.put("lunchRecords", lunchRecords);

        return "lunchRecords";
    }
}
