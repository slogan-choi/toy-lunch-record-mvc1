package lunch.record.servlet.web.servletmvc;

import lunch.record.servlet.domain.LunchRecord;
import lunch.record.servlet.domain.LunchRecordRepository;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "mvcLunchRecordUpdateFormServlet", urlPatterns = "/servlet-mvc/lunchRecord/update-form/*")
public class MvcLunchRecordUpdateFormServlet extends HttpServlet {

    private LunchRecordRepository repository = LunchRecordRepository.getInstance();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LunchRecord lunchRecord = repository.findById(Long.valueOf(request.getParameter("id")));

        // Model에 데이터를 담아서 보관한다.
        request.setAttribute("lunchRecord", lunchRecord);

        String viewPath = "/WEB-INF/views/form/update-form.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }

}
