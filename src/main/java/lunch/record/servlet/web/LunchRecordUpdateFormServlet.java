package lunch.record.servlet.web;

import lunch.record.util.Utils;
import lunch.record.servlet.domain.LunchRecord;
import lunch.record.servlet.domain.LunchRecordRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(name = "lunchRecordUpdateFormServlet", urlPatterns = "/servlet/lunchRecord/update-form/*")
public class LunchRecordUpdateFormServlet extends HttpServlet {

    private LunchRecordRepository repository = LunchRecordRepository.getInstance();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LunchRecord lunchRecord = repository.findById(Long.valueOf(request.getParameter("id")));

        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");

        PrintWriter writer = response.getWriter();
        writer.write("<html>");
        writer.write("<head>");
        writer.write("  <meta charset=\"UTF-8\">");
        writer.write("  <title>점심 기록</title>");
        writer.write("</head>");
        writer.write("<body>");
        writer.write("<table>");
        writer.write("  <thead>");
        writer.write("      <th>id</th>");
        writer.write("      <th>restaurant</th>");
        writer.write("      <th>menu</th>");
        writer.write("      <th>image</th>");
        writer.write("      <th>price</th>");
        writer.write("      <th>grade</th>");
        writer.write("      <th>averageGrade</th>");
        writer.write("      <th>updateAt</th>");
        writer.write("      <th>createAt</th>");
        writer.write("  </thead>");
        writer.write("  <tbody>");
        writer.write("  <tr>");
        writer.write("      <td>" + lunchRecord.getId() + "</td>");
        writer.write("      <td>" + lunchRecord.getRestaurant() + "</td>");
        writer.write("      <td>" + lunchRecord.getMenu() + "</td>");
        try {
            String base64EncodeByte = Utils.getBase64EncodeByte(lunchRecord.getImage().getBinaryStream().readAllBytes());
            writer.write("      <img src=\"data:image/png;base64," + base64EncodeByte + "\" />");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        writer.write("      <td>" + lunchRecord.getPrice() + "</td>");
        writer.write("      <td>" + lunchRecord.getGrade() + "</td>");
        writer.write("      <td>" + lunchRecord.getAverageGrade() + "</td>");
        writer.write("      <td>" + lunchRecord.getUpdateAt() + "</td>");
        writer.write("      <td>" + lunchRecord.getCreateAt() + "</td>");
        writer.write("  </tr>");
        writer.write("  </tbody>");
        writer.write("</table>");
        writer.write(
                "<form action=\"/servlet/lunchRecord/update\" method=\"post\" enctype=\"multipart/form-data\">\n" +
                        "   아이디:    <input type=\"text\" name=\"id\" value=\"" + lunchRecord.getId() + "\" readonly/>\n" +
                        "   식당:     <input type=\"text\" name=\"restaurant\" value=\"" + lunchRecord.getRestaurant() + "\"/>\n" +
                        "   메뉴:     <input type=\"text\" name=\"menu\" value=\"" + lunchRecord.getMenu() +"\"/>\n" +
                        "   이미지:    <input type=\"file\" name=\"image\" />\n" +
                        "   가격:     <input type=\"number\" name=\"price\" value=\"" + lunchRecord.getPrice() +"\"/>\n" +
                        "   평점:     <input type=\"number\" name=\"grade\" step=\"0.1\" value=\"" + lunchRecord.getGrade() + "\"/>\n" +
                        "   평균 평점: <input type=\"number\" name=\"averageGrade\" step=\"0.1\" value=\"" + lunchRecord.getAverageGrade() + "\" readonly/>\n" +
                        "<button type=\"submit\">수정</button>\n" +
                "</form>"
        );
        writer.write("<a href=\"/servlet/lunchRecord/list\">조회</a>\n");
        writer.write("</body>");
        writer.write("</html>");
    }
}
