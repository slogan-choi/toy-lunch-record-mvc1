package lunch.record.servlet.web.frontcontroller.controller;

import lunch.record.servlet.domain.LunchRecord;
import lunch.record.servlet.domain.LunchRecordRepository;
import lunch.record.servlet.web.frontcontroller.Controller;
import lunch.record.servlet.web.frontcontroller.MyView;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LunchRecordDeleteFormController implements Controller {

    private LunchRecordRepository repository = LunchRecordRepository.getInstance();

    @Override
    public MyView process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LunchRecord lunchRecord = repository.findById(Long.valueOf(request.getParameter("id")));

        // Model에 데이터를 담아서 보관한다.
        request.setAttribute("lunchRecord", lunchRecord);

        return new MyView("/WEB-INF/views/delete-form.jsp");
    }
}
