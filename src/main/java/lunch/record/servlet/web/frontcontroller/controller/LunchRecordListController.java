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
    public ModelView process(Map<String, RequestInfo> paramMap) throws ServletException, IOException {
        List<LunchRecord> lunchRecords = repository.findAll();

        ModelView mv = new ModelView("lunchRecords");
        mv.getModel().put("lunchRecords", lunchRecords);

        return mv;
    }
}
