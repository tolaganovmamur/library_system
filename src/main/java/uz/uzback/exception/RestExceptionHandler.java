package uz.uzback.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uz.uzback.payload.ApiResult;


@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = InputDataExistsException.class)
    public ResponseEntity<ApiResult> exceptionHandler(InputDataExistsException ex) {
        ApiResult apiResult = ApiResult.failResponse(ex != null ? ex.getMessage() : HttpStatus.CONFLICT.getReasonPhrase(), HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }

    @ExceptionHandler(value = NullPointerException.class)
    public ResponseEntity<ApiResult> exceptionHandler(NullPointerException ex) {
        ApiResult apiResult = ApiResult.failResponse(ex != null ? ex.getMessage() : HttpStatus.CONFLICT.getReasonPhrase(), HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }

    @ExceptionHandler(value = InFormattingException.class)
    public ResponseEntity<ApiResult> exceptionHandler(InFormattingException ex) {
        ApiResult apiResult = ApiResult.failResponse(ex != null ? ex.getMessage() : HttpStatus.CONFLICT.getReasonPhrase(), HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }

    @ExceptionHandler(value = DataNotFoundException.class)
    public ResponseEntity<ApiResult> exceptionHandler(DataNotFoundException ex) {
        ApiResult apiResult = ApiResult.failResponse(ex != null ? ex.getMessage() : HttpStatus.CONFLICT.getReasonPhrase(), HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }

    @ExceptionHandler(value = IncorrectDataException.class)
    public ResponseEntity<ApiResult> exceptionHandler(IncorrectDataException ex) {
        ApiResult apiResult = ApiResult.failResponse(ex != null ? ex.getMessage() : HttpStatus.CONFLICT.getReasonPhrase(), HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }

}
