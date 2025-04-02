package com.develop.backend.application.dto.response;

import com.develop.backend.insfraestructure.util.Global;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenericResponse<T> {
    private String message;
    private int status;
    private T data;


    public static <T> GenericResponse<T> success(String message, T data) {
        return new GenericResponse<>(message, Global.SUCCESS, data);
    }
    public static <T> GenericResponse<T> error(String message, int status) {
        return new GenericResponse<>(message, status, null);
    }
}
