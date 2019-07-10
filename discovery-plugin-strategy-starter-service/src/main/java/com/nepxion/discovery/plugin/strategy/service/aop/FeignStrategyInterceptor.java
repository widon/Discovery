package com.nepxion.discovery.plugin.strategy.service.aop;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import feign.RequestInterceptor;
import feign.RequestTemplate;

import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.nepxion.discovery.common.constant.DiscoveryConstant;
import com.nepxion.discovery.plugin.strategy.service.adapter.FeignStrategyInterceptorAdapter;
import com.nepxion.discovery.plugin.strategy.service.constant.ServiceStrategyConstant;

public class FeignStrategyInterceptor extends AbstractStrategyInterceptor implements RequestInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger(FeignStrategyInterceptor.class);

    @Autowired(required = false)
    private List<FeignStrategyInterceptorAdapter> feignStrategyInterceptorAdapterList;

    public FeignStrategyInterceptor(String requestHeaders) {
        super(requestHeaders);

        LOG.info("----------- Feign Intercept Information ----------");
        LOG.info("Feign desires to intercept customer headers are {}", requestHeaderList);
        LOG.info("--------------------------------------------------");
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        interceptInputHeader();

        applyInnerHeader(requestTemplate);
        applyOuterHeader(requestTemplate);

        interceptOutputHeader(requestTemplate);

        if (CollectionUtils.isNotEmpty(feignStrategyInterceptorAdapterList)) {
            for (FeignStrategyInterceptorAdapter feignStrategyInterceptorAdapter : feignStrategyInterceptorAdapterList) {
                feignStrategyInterceptorAdapter.apply(requestTemplate);
            }
        }
    }

    private void applyInnerHeader(RequestTemplate requestTemplate) {
        requestTemplate.header(DiscoveryConstant.N_D_SERVICE_TYPE, pluginAdapter.getServiceType());
        requestTemplate.header(DiscoveryConstant.N_D_SERVICE_ID, pluginAdapter.getServiceId());
        requestTemplate.header(DiscoveryConstant.N_D_SERVICE_ADDRESS, pluginAdapter.getHost() + ":" + pluginAdapter.getPort());
        requestTemplate.header(DiscoveryConstant.N_D_SERVICE_GROUP, pluginAdapter.getGroup());
        requestTemplate.header(DiscoveryConstant.N_D_SERVICE_VERSION, pluginAdapter.getVersion());
        requestTemplate.header(DiscoveryConstant.N_D_SERVICE_REGION, pluginAdapter.getRegion());
    }

    private void applyOuterHeader(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = serviceStrategyContextHolder.getRestAttributes();
        if (attributes == null) {
            return;
        }

        HttpServletRequest previousRequest = attributes.getRequest();
        Enumeration<String> headerNames = previousRequest.getHeaderNames();
        if (headerNames == null) {
            return;
        }

        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = previousRequest.getHeader(headerName);
            boolean isHeaderContains = isHeaderContainsExcludeInner(headerName.toLowerCase());
            if (isHeaderContains) {
                requestTemplate.header(headerName, headerValue);
            }
        }
    }

    private void interceptOutputHeader(RequestTemplate requestTemplate) {
        Boolean interceptDebugEnabled = environment.getProperty(ServiceStrategyConstant.SPRING_APPLICATION_STRATEGY_REST_INTERCEPT_DEBUG_ENABLED, Boolean.class, Boolean.FALSE);
        if (!interceptDebugEnabled) {
            return;
        }

        System.out.println("------- Intercept Output Header Information ------");
        Map<String, Collection<String>> headers = requestTemplate.headers();
        for (Map.Entry<String, Collection<String>> entry : headers.entrySet()) {
            String headerName = entry.getKey();
            boolean isHeaderContains = isHeaderContains(headerName.toLowerCase());
            if (isHeaderContains) {
                Collection<String> headerValue = entry.getValue();

                System.out.println(headerName + "=" + headerValue);
            }
        }
        System.out.println("--------------------------------------------------");
    }
}