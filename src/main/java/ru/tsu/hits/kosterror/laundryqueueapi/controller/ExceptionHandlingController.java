package ru.tsu.hits.kosterror.laundryqueueapi.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.ApiError;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.ApiResponse;
import ru.tsu.hits.kosterror.laundryqueueapi.exception.AbstractCustomException;

@ControllerAdvice
@Slf4j
public class ExceptionHandlingController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(HttpServletRequest request, Exception exception) {
        logError(request, exception, 0);
        ApiResponse<Void> response = buildApiResponse(0, "Непредвиденная внутренняя ошибка сервера");
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(AbstractCustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleAbstractCustomException(HttpServletRequest request,
                                                                           AbstractCustomException exception) {
        logError(request, exception, exception.getCode());
        ApiResponse<Void> response = buildApiResponse(
                exception.getCode(),
                exception.getMessage()
        );
        return ResponseEntity.ok(response);
    }

    private ApiResponse<Void> buildApiResponse(int code, String message) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setError(new ApiError(code, message));
        return apiResponse;
    }

    private void logError(HttpServletRequest request, Exception exception, int code) {
        log.error("Возникла ошибка при запросе: {} {}. Код ошибки: {}",
                request.getMethod(),
                request.getRequestURL(),
                code);
        log.error(exception.getMessage(), exception);
    }

}
