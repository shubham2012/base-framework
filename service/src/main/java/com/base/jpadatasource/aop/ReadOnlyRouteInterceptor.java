package com.base.jpadatasource.aop;

import com.base.jpadatasource.RoutingDataSource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Aspect
@Component
@Order(0)
@Slf4j
public class ReadOnlyRouteInterceptor {

    /**
     * This AOP is being used for intercepting the transactional database call and redirecting it to read DB if
     * mentioned @Transactional(readOnly = true)
     *
     * @param proceedingJoinPoint
     * @param transactional
     * @return
     * @throws Throwable
     */
    @Around("@annotation(transactional)")
    public Object proceed(ProceedingJoinPoint proceedingJoinPoint, Transactional transactional) throws Throwable {
        try {
            if (transactional.readOnly()) {
                RoutingDataSource.setReplicaRoute();
                log.info("Routing database call to the read replica");
            }
            return proceedingJoinPoint.proceed();
        } finally {
            RoutingDataSource.clearReplicaRoute();
        }
    }
}
