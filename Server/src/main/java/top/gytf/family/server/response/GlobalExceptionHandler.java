package top.gytf.family.server.response;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.List;

/**
 * Project:     IntelliJ IDEA
 * ClassName:   GlobalExceptionHandler
 * Description  统一异常处理
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 * @date 2021/7/22 15:28
 */

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final static String TAG = GlobalExceptionHandler.class.getName();

    @ExceptionHandler(Exception.class)
    public Response<String> exceptionHandler(Exception e) {
        e.printStackTrace();
        StatusCarrier statusCarrier = null;
        if ((statusCarrier = e.getClass().getAnnotation(StatusCarrier.class)) != null) {
            return new Response<>(statusCarrier.value(), e.getMessage());
        }

        if (e instanceof BindException) {
            //参数校验错误
            BindException bindException = (BindException) e;
            String[] errors = new String[bindException.getErrorCount()];
            List<ObjectError> errorList = bindException.getAllErrors();
            int idx = 0;
            for (ObjectError objectError : errorList) {
                errors[idx++] = objectError.getDefaultMessage();
            }
            return new Response<>(StateCode.PARAM_IS_INVALID, Arrays.toString(errors));
        } else if (e instanceof AccessDeniedException) {
            //权限不足
            return new Response<>(StateCode.SECURITY_NO_PERMISSION, e.getMessage());
        }

        return new Response<>(StateCode.FAIL, e.getMessage());
    }
}
