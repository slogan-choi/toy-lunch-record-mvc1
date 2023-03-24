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
    public ModelView process(Map<String, RequestInfo> paramMap) throws ServletException, IOException {
        LunchRecord lunchRecord = repository.findById(Long.valueOf((String) paramMap.get("id").getInfo()));

        // Model에 데이터를 담아서 보관한다.
        ModelView mv = new ModelView("update-form");
        mv.getModel().put("lunchRecord", lunchRecord);

        return mv;
    }
}
