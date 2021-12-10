package kg.itacademy.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Around(value = "@annotation(LogMethod)")
    public Object logMethodInvocation(ProceedingJoinPoint joinPoint) throws Throwable {
        //before
        log.info(joinPoint.getSignature().toString());

        long startTime = System.currentTimeMillis();
        Object methodResult = joinPoint.proceed();

        long endTime = System.currentTimeMillis();
        log.info("method invocation took " + (endTime - startTime) + " millis");
        //after
        return methodResult;
    }
}