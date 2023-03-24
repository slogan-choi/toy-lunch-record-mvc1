package lunch.record.servlet.web.frontcontroller;

import lombok.Data;

@Data
public class RequestInfo<T> {
    private T info;
}
