package lunch.record.servlet.web.frontcontroller.controller;

import lunch.record.servlet.domain.LunchRecordRepository;
import lunch.record.servlet.web.frontcontroller.Controller;
import lunch.record.servlet.web.frontcontroller.ModelView;
import lunch.record.servlet.web.frontcontroller.RequestInfo;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Map;

public class LunchRecordDeleteController implements Controller {

    private LunchRecordRepository repository = LunchRecordRepository.getInstance();

    @Override
    public String process(Map<String, RequestInfo> paramMap, Map<String, Object> model) throws ServletException, IOException {
        repository.delete(Integer.valueOf((String) paramMap.get("id").getInfo()));

        return "delete";
    }
}
