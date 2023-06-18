package ru.tsu.hits.kosterror.laundryqueueapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class ApiError {

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss:SSS")
    private LocalDateTime timestamp;

    private int code;

    private String message;

    private Map<String, List<String>> validationMessages;

    public ApiError(int code, String message) {
        this.timestamp = LocalDateTime.now();
        this.code = code;
        this.message = message;
        this.validationMessages = new HashMap<>();
    }

    public ApiError(int code, String message, Map<String, List<String>> validationMessages) {
        this.code = code;
        this.message = message;
        this.validationMessages = validationMessages;
    }

}
