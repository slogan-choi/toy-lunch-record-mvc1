package lunch.record.servlet.web;

import lombok.extern.slf4j.Slf4j;
import lunch.record.servlet.domain.LunchRecordRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@WebServlet(name = "lunchRecordDeleteServlet", urlPatterns = "/servlet/lunchRecord/delete")
public class LunchRecordDeleteServlet extends HttpServlet {

    private LunchRecordRepository repository = LunchRecordRepository.getInstance();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");

        repository.delete(Integer.parseInt(request.getParameter("id")));

        PrintWriter writer = response.getWriter();
        writer.write("<!DOCTYPE>\n" +
                "<html>\n" +
                    "<head>\n" +
                        "<meta charset=\"utf-8\">\n" +
                        "<title>점심 기록</title>\n" +
                    "</head>\n" +
                    "<body> 삭제 성공\n" +
                        "<a href=\"/servlet/lunchRecord/list\">조회</a>\n" +
                    "</body>\n" +
                "</html>\n"
        );
    }
}
