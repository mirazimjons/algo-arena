package uz.thejaver.algoarena.exeption;

import lombok.Getter;
import uz.thejaver.springbootstarterexceptionsupporter.exception.exceptipTypes.Type;

import java.util.Map;

@Getter
public enum ExceptionType implements Type {
    PASSWORD_NOT_DEFINED(412, "Password error", "Password is not defined", null),
    INVALID_CREDENTIALS(401, "Invalid credentials", "Credential you provided is not valid", null),
    ;

    private final Integer code;
    private final String title;
    private final String message;
    private final Map<String, Object> attributes;

    ExceptionType(Integer code, String title, String message, Map<String, Object> attributes) {
        this.code = code;
        this.title = title;
        this.message = message;
        this.attributes = attributes;
    }

}
