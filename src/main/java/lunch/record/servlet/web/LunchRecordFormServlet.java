package lunch.record.servlet.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "lunchRecordFormServlet", urlPatterns = "/servlet/lunchRecord/new-form")
public class LunchRecordFormServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");

        PrintWriter writer = response.getWriter();
        writer.write("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "   <meta charset=\"UTF-8\">\n" +
                "   <title>점심 기록</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<form action=\"/servlet/lunchRecord/save\" method=\"post\" enctype=\"multipart/form-data\">\n" +
                "   <input type=\"hidden\" name=\"id\" />\n" +
                "   식당:     <input type=\"text\" name=\"restaurant\" />\n" +
                "   메뉴:     <input type=\"text\" name=\"menu\" />\n" +
                "   이미지:    <input type=\"file\" name=\"image\">\n" +
                "   가격:     <input type=\"number\" name=\"price\" />\n" +
                "   평점:     <input type=\"number\" name=\"grade\" step=\"0.1\"/>\n" +
                "   평균 평점: <input type=\"number\" name=\"averageGrade\" step=\"0.1\" readonly/>\n" +
                "<button type=\"submit\">전송</button>\n" +
                "</form>\n" +
                "</body>\n" +
                "</html>\n"
                );
    }
}
