package yers.dev.keycloak.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import yers.dev.keycloak.entity.dto.ErrorResponseDto;

import java.time.LocalDateTime;

/**
 * Глобальный обработчик исключений для REST API.
 * Обрабатывает различные типы исключений и возвращает стандартизированные ответы с подробной информацией об ошибке.
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Обрабатывает все необработанные исключения.
     *
     * @param exception   выброшенное исключение
     * @param webRequest  текущий веб-запрос
     * @return {@link ResponseEntity} с {@link ErrorResponseDto} и статусом 500 INTERNAL_SERVER_ERROR
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception exception,
                                                                  WebRequest webRequest) {
        log.info("Exception message: ", exception);

        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Обрабатывает исключения, когда ресурс не найден.
     *
     * @param exception   исключение {@link ResourceNotFoundException}
     * @param webRequest  текущий веб-запрос
     * @return {@link ResponseEntity} с {@link ErrorResponseDto} и статусом 404 NOT_FOUND
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                                            WebRequest webRequest) {
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
    }

    /**
     * Обрабатывает исключения, когда аккаунт с такими данными уже существует.
     *
     * @param exception   исключение {@link SameAccountExistException}
     * @param webRequest  текущий веб-запрос
     * @return {@link ResponseEntity} с {@link ErrorResponseDto} и статусом 409 CONFLICT
     */
    @ExceptionHandler(SameAccountExistException.class)
    public ResponseEntity<ErrorResponseDto> handleSameAccountExistException(SameAccountExistException exception,
                                                                            WebRequest webRequest) {
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.CONFLICT,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.CONFLICT);
    }
}

