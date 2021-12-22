package kg.itacademy.util;

import kg.itacademy.exception.ApiErrorException;
import kg.itacademy.exception.ApiFailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = ApiFailException.class)
    public ResponseEntity<ResponseMessage<String>> handlerFailException(ApiFailException apiFailException) {
        ResponseMessage<String> failResponseMessage =
                new ResponseMessage<String>().prepareFailMessage(apiFailException.getMessage());
        String threwClassName = apiFailException.getStackTrace()[0].getClassName();
        log.warn(threwClassName + " : " + apiFailException.getMessage());
        return ResponseEntity.ok(failResponseMessage);
    }

    @ExceptionHandler(value = ApiErrorException.class)
    public ResponseEntity<ResponseMessage<String>> handlerErrorException(ApiErrorException apiErrorException) {
        ResponseMessage<String> errorResponseMessage =
                new ResponseMessage<String>().prepareErrorMessage(apiErrorException.getMessage());
        String threwClassName = apiErrorException.getStackTrace()[0].getClassName();
        log.error(threwClassName + " : " + apiErrorException.getMessage());
        return ResponseEntity.ok(errorResponseMessage);
    }
}