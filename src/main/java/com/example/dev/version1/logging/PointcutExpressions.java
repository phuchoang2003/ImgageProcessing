package com.example.dev.version1.logging;


import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class PointcutExpressions {


    @Pointcut("execution(* com.example.dev.version1.auth.*.*(..))")
    public void forAuthPackage(){}


    @Pointcut("execution(* com.example.dev.version1.user.*.*(..))")
    public void forUserPackage(){}


    @Pointcut("execution(* com.example.dev.version1.config.*.*(..))")
    public void forConfigPackage(){}


    @Pointcut("forAuthPackage() && forUserPackage() && forConfigPackage()")
    public void commonPointcut(){}
}
