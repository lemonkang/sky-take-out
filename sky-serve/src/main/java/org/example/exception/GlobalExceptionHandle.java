package org.example.exception;

import lombok.extern.slf4j.Slf4j;
import org.example.config.Result;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandle {
    @ExceptionHandler(DeficiencyException.class)
    public Result deficiencyException(DeficiencyException e) {
        log.warn("DeficiencyException"+e.getMessage()+e.getData());
        return Result.error(e.getMessage(),e.getData());
    }
    // 处理 @Valid 校验失败
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return Result.error(ex.getMessage(),errors);

    }
    @ExceptionHandler(Exception.class)
    public Result<String> exception(Exception e) {
        log.info("GlobalExceptionHandle"+e.getMessage());
        return Result.error(e.getMessage());
    }


}
