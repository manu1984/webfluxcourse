package br.com.valdircezar.webfluxcourse.controller.exceptions;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Builder
public class StandardError {

    private static final long serialVersionID = 1L;

    private LocalDateTime  timestamp;
    private String path;
    private Integer status;
    private String error;
    private String message;
}
