package com.nepxion.discovery.plugin.strategy.service.tracer;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;

import com.nepxion.discovery.plugin.strategy.tracer.StrategyTracer;
import com.nepxion.matrix.proxy.aop.AbstractInterceptor;

public class TracerStrategyInterceptor extends AbstractInterceptor {
    @Autowired(required = false)
    private StrategyTracer strategyTracer;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        // 调用链追踪
        if (strategyTracer != null) {
            strategyTracer.traceHeader();
        }

        return invocation.proceed();
    }
}