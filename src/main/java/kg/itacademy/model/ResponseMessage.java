package kg.itacademy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ResponseMessage<T> {
    private T value;
    private Integer status;
    private String details;

    public ResponseMessage<T> prepareSuccessMessage(T value) {
        ResponseMessage<T> successMessage = new ResponseMessage<>();
        successMessage.setValue(value);
        successMessage.setStatus(HttpStatus.OK.value());
        successMessage.setDetails("");
        return successMessage;
    }

    public ResponseMessage<T> prepareFailMessage(Integer status, String details) {
        ResponseMessage<T> failMessage = new ResponseMessage<>();
        failMessage.setStatus(status);
        failMessage.setDetails(details);
        return failMessage;
    }
}