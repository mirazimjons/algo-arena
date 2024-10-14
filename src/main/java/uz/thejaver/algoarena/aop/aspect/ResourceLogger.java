package uz.thejaver.algoarena.aop.aspect;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class ResourceLogger {

    @Before("execution(* uz.thejaver.algoarena.resource.web.*.*(..))")
    public void logBefore(@NonNull JoinPoint joinPoint) {
        log.info("REST request to {} with arguments: {}",
                joinPoint.getSignature().toShortString(),
                Arrays.toString(joinPoint.getArgs())
        );
    }

    @AfterReturning(pointcut = "execution(* uz.thejaver.algoarena.resource.web.*.*(..))", returning = "result")
    public void logAfterReturning(@NonNull JoinPoint joinPoint, Object result) {
        log.info("REST result of {}: {}", joinPoint.getSignature().toShortString(), result);
    }

}
