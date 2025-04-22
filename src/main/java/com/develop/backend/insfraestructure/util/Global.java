package com.develop.backend.insfraestructure.util;

import lombok.Getter;

@Getter
public enum Global {
    ACTIVE(1, "ACTIVE"),
    INACTIVE(0, "INACTIVE"),
    SUCCESS(200, "SUCCESS"),
    CREATED(201, "CREATED"),
    NO_CONTENT(204, "NO CONTENT"),
    BAD_REQUEST(400, "BAD REQUEST"),
    UNAUTHORIZED(401, "UNAUTHORIZED"),
    FORBIDDEN(403, "FORBIDDEN"),
    NOT_FOUND(404, "NOT FOUND"),
    CONFLICT(409, "CONFLICT"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL SERVER ERROR");

    private final int code;
    private final String message;

    Global(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
