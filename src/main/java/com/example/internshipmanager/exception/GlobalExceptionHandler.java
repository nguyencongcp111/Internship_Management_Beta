package com.example.internshipmanager.exception;

import com.example.internshipmanager.models.dto.response.InfoError;
<<<<<<< HEAD
import jakarta.servlet.ServletException;
import org.springframework.dao.DataIntegrityViolationException;
=======
>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import com.example.internshipmanager.models.dto.response.ErrorResponse;
<<<<<<< HEAD
=======
import org.springframework.web.server.ResponseStatusException;
>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointer(
            NullPointerException exception
    ) {

        ErrorResponse response = new ErrorResponse<>();
        response.setSuccess(false);
        response.setTimestamp(LocalDateTime.now());
        response.setMessage(exception.getMessage());
        response.setData(null);


        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(JWTAuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleJWT(
            JWTAuthenticationException exception
    ) {

        ErrorResponse response = new ErrorResponse<>();
        response.setSuccess(false);
        response.setTimestamp(LocalDateTime.now());
        response.setMessage(exception.getMessage());
        response.setData(null);


        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(WrongUsernameOrPasswordException.class)
    public ResponseEntity<ErrorResponse> handleWrongUsernameOrPassword(
            WrongUsernameOrPasswordException exception
    ) {

        ErrorResponse response = new ErrorResponse<>();
        response.setSuccess(false);
        response.setTimestamp(LocalDateTime.now());
        response.setMessage(exception.getMessage());
        response.setData(null);


        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException exception
    ) {
        ErrorResponse response = new ErrorResponse<>();
        response.setSuccess(false);
        response.setTimestamp(LocalDateTime.now());
        response.setMessage(exception.getMessage());
        response.setData(null);

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceFound(
            ResourceFoundException exception
    ) {
        ErrorResponse response = new ErrorResponse<>();
        response.setSuccess(false);
        response.setTimestamp(LocalDateTime.now());
        response.setMessage(exception.getMessage());
        response.setData(null);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse<InfoError>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception
    ) {

        List<InfoError> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError ->
                        new InfoError(
                                fieldError.getField(),
                                fieldError.getDefaultMessage()))
                .toList();

        ErrorResponse response = new ErrorResponse<>(
                false,
                "Dữ liệu không hợp lệ",
                null,
                errors,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse<InfoError>> handleBadCredentials(
            BadCredentialsException exception
    ) {

        ErrorResponse response = new ErrorResponse<>(
                false,
                "Sai tài khoản hoặc mật khẩu",
                null,
                null,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnexpectedRoleException.class)
    public ResponseEntity<ErrorResponse<InfoError>> handleUnexpectedRole(
            UnexpectedRoleException exception
    ) {

        ErrorResponse response = new ErrorResponse<>(
                false,
                exception.getMessage(),
                null,
                null,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

<<<<<<< HEAD
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(
            DataIntegrityViolationException exception) {

        return ResponseEntity.badRequest().body(
                new ErrorResponse(
                        false,
                        "Không thể xóa vì dữ liệu đang được tham chiếu.",
                        null,
                        null,
                        LocalDateTime.now()
                )
        );
=======
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatus(
            ResponseStatusException exception
    ) {

        ErrorResponse response = new ErrorResponse<>();
        response.setSuccess(false);
        response.setTimestamp(LocalDateTime.now());
        response.setMessage(exception.getReason() != null ? exception.getReason() : "Đã xảy ra lỗi");
        response.setData(null);


        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)
    }

}
