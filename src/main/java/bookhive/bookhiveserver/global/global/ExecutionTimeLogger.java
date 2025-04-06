package bookhive.bookhiveserver.global.global;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ExecutionTimeLogger {

    @Around("execution(* bookhive.bookhiveserver.domain.ai.controller..*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.nanoTime();
        Object result = joinPoint.proceed();
        long end = System.nanoTime();

        log.info("[ExecutionTime] {} executed in {} ms",
                joinPoint.getSignature(),
                (end - start) / 1_000_000);
        return result;
    }
}

