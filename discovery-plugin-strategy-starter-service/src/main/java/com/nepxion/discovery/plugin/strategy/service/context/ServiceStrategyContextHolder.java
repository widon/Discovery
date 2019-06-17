package com.nepxion.discovery.plugin.strategy.service.context;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @author Fan Yang
 * @version 1.0
 */

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.nepxion.discovery.plugin.strategy.context.StrategyContextHolder;

public class ServiceStrategyContextHolder implements StrategyContextHolder {
    private static final Logger LOG = LoggerFactory.getLogger(ServiceStrategyContextHolder.class);

    public ServletRequestAttributes getRestAttributes() {
        RequestAttributes requestAttributes = RestStrategyContext.getCurrentContext().getRequestAttributes();
        if (requestAttributes == null) {
            requestAttributes = RequestContextHolder.getRequestAttributes();
        }

        return (ServletRequestAttributes) requestAttributes;
    }

    public Map<String, Object> getRpcAttributes() {
        return RpcStrategyContext.getCurrentContext().getAttributes();
    }
    
    public Object getRpcAttributes(String key) {
        return RpcStrategyContext.getCurrentContext().get(key);
    }
    
    public RpcStrategyContext setRpcAttributes(String key, Object value) {
        return RpcStrategyContext.getCurrentContext().add(key,value);
    }
    
    

    @Override
    public String getHeader(String name) {
        ServletRequestAttributes attributes = getRestAttributes();
        if (attributes == null) {
            LOG.warn("The ServletRequestAttributes object is null");

            return null;
        }

        return attributes.getRequest().getHeader(name);
    }
}