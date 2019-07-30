package lingxi.shop.common.advice;

import lingxi.shop.common.exception.lingxiException;
import lingxi.shop.common.vo.ExceptionResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommonExceptionHandler {
    @ExceptionHandler(lingxiException.class)
    public ResponseEntity<ExceptionResult> handleException(lingxiException e){
        return ResponseEntity.status(e.getExceptionEnum().getCode()).body(new ExceptionResult(e.getExceptionEnum()));
    }
}
