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
import java.util.List;

@WebServlet(name = "lunchRecordListServlet", urlPatterns = "/servlet/lunchRecord/list")
public class LunchRecordListServlet extends HttpServlet {

    private LunchRecordRepository repository = LunchRecordRepository.getInstance();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<LunchRecord> lunchRecords = repository.findAll();

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
        for (LunchRecord lunchRecord : lunchRecords) {
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
            writer.write("      <td> <a href=\"/servlet/lunchRecord/update-form?id=" + lunchRecord.getId() + "\">수정</a> </td>");
            writer.write("      <td> <a href=\"/servlet/lunchRecord/delete-form?id=" + lunchRecord.getId() + "\">삭제</a> </td>");
            writer.write("  </tr>");
        }
        writer.write("  </tbody>");
        writer.write("</table>");

        writer.write("</body>");
        writer.write("</html>");
    }
}
