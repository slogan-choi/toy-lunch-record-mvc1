package lunch.record.servlet.web.frontcontroller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Controller {

    MyView process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
