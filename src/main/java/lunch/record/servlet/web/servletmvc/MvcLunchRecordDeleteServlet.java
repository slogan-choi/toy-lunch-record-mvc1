package lunch.record.servlet.web.servletmvc;

import lunch.record.servlet.domain.LunchRecordRepository;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "mvcLunchRecordDeleteServlet", urlPatterns = "/servlet-mvc/lunchRecord/delete")
public class MvcLunchRecordDeleteServlet extends HttpServlet {

    private LunchRecordRepository repository = LunchRecordRepository.getInstance();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        repository.delete(Integer.valueOf(request.getParameter("id")));

        String viewPath = "/WEB-INF/views/delete-result.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }
}
