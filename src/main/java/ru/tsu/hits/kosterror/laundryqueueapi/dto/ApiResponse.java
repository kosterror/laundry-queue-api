package ru.tsu.hits.kosterror.laundryqueueapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {

    private ApiError error;

    private T content;

    public ApiResponse(ApiError error) {
        this.error = error;
    }

    public ApiResponse(T content) {
        this.content = content;
    }

}
