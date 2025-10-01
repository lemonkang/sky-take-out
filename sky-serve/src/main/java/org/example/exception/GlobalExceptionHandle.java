package org.example.exception;

import lombok.extern.slf4j.Slf4j;
import org.example.config.Result;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandle {
    // 处理 @Valid 校验失败
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> handleValidationException(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        // 取第一个错误信息即可
        String errorMessage = e.getBindingResult().getFieldError().getDefaultMessage();
        return Result.error(errorMessage);
    }
    @ExceptionHandler(Exception.class)
    public Result<String> exception(Exception e) {
        log.info("GlobalExceptionHandle"+e.getMessage());
        return Result.error(e.getMessage());
    }

}
