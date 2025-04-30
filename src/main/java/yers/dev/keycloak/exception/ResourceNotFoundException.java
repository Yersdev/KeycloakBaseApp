package yers.dev.keycloak.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, выбрасываемое, когда запрашиваемый ресурс не найден.
 * Например, если пользователь, роль или другой объект отсутствует в базе данных.
 *
 * Возвращает HTTP-статус 404 (NOT_FOUND).
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Конструктор исключения.
     *
     * @param resourceName имя ресурса (например, "User")
     * @param fieldName    имя поля, по которому велся поиск (например, "id")
     * @param fieldValue   значение поля, которое не было найдено (например, "42")
     */
    public ResourceNotFoundException(String resourceName, String fieldName, String fieldValue) {
        super(String.format("%s not found with the given input data %s : '%s'", resourceName, fieldName, fieldValue));
    }
}
