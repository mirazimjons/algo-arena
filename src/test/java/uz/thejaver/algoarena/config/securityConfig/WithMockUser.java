package uz.thejaver.algoarena.config.securityConfig;

import org.springframework.security.test.context.support.WithSecurityContext;
import uz.thejaver.algoarena.domain.enums.Permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockPermissionSecurityContextFactory.class)
public @interface WithMockUser {

    String username() default DummyData.USERNAME;

    Permission[] permissions() default {};

    interface DummyData {
        String USERNAME = "user";
    }

}
