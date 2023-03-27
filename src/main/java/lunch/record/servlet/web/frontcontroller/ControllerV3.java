package lunch.record.servlet.web.frontcontroller;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Map;

public interface ControllerV3 {

    ModelView process(Map<String, RequestInfo> paramMap) throws ServletException, IOException;
}
