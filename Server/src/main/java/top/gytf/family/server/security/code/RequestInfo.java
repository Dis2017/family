package top.gytf.family.server.security.code;

import java.util.Objects;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 请求信息<br>
 * CreateDate:  2021/12/14 23:27 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public class RequestInfo {
    private final String url;
    private final String method;

    public RequestInfo(String url, String method) {
        this.url = url;
        this.method = method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RequestInfo that = (RequestInfo) o;
        return url.equals(that.url) && method.equals(that.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, method);
    }
}
