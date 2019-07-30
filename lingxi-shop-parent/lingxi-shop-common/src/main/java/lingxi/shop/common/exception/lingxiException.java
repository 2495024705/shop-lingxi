package lingxi.shop.common.exception;

import lingxi.shop.common.enums.ExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class lingxiException extends RuntimeException {
public ExceptionEnum exceptionEnum;

}
