package lunch.record.servlet.web.frontcontroller;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Map;

public interface Controller {

    String process(Map<String, RequestInfo> paramMap, Map<String, Object> model) throws ServletException, IOException;
}
