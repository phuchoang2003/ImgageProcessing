package com.example.dev.version1.logging;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class AspectLogging {
    private final Logger logging = LoggerFactory.getLogger(this.getClass());


    @Before("com.example.dev.version1.logging.PointcutExpressions.commonPointcut()")
        public void beforeAdvice(JoinPoint joinPoint){

            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

            //Get intercepted method details
            String className = methodSignature.getDeclaringType().getSimpleName();
            String methodName = methodSignature.getName();

            //get parameter name
            System.out.println(Arrays.toString(methodSignature.getParameterNames()));

            // logging
             logging.info("call " + methodName + " method in " + className);

            // get argument value
            Object[] objects = joinPoint.getArgs();
            System.out.println(Arrays.toString(objects));
            System.out.println(joinPoint.toString());

        }


}
