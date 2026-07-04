package com.example.internshipmanager.models.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DataResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private String errors;
    private LocalDateTime timestamp;
}
