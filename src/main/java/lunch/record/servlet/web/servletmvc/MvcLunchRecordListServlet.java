package lunch.record.servlet.web.servletmvc;

import lombok.extern.slf4j.Slf4j;
import lunch.record.servlet.domain.LunchRecord;
import lunch.record.servlet.domain.LunchRecordRepository;
import lunch.record.util.Utils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Slf4j
@WebServlet(name = "mvcLunchRecordListServlet", urlPatterns = "/servlet-mvc/lunchRecords")
public class MvcLunchRecordListServlet extends HttpServlet {

    private LunchRecordRepository repository = LunchRecordRepository.getInstance();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<LunchRecord> lunchRecords = repository.findAll();
        List<String> base64EncodeByte = new ArrayList<>();
        for (LunchRecord lunchRecord : lunchRecords) {
            try {
                base64EncodeByte.add(Utils.getBase64EncodeByte(lunchRecord.getImage().getBinaryStream().readAllBytes()));
                byte[] encode = Base64.getEncoder().encode(lunchRecord.getImage().getBytes(1, Integer.parseInt(String.valueOf(lunchRecord.getImage().length()))));
                log.info("Base64 Test={}", encode);
                log.info("Base64Utils Test={}", Utils.getBase64EncodeByte(lunchRecord.getImage().getBinaryStream().readAllBytes()));
            } catch (SQLException e) {
                throw new ServletException(e);
            }
        }

        request.setAttribute("lunchRecords", lunchRecords);
        request.setAttribute("base64EncodeByte", base64EncodeByte);
        String viewPath = "/WEB-INF/views/lunchRecords.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }
}
