package com.nepxion.discovery.plugin.strategy.gateway.filter;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */


public interface GatewayStrategyRouteFilter extends GlobalFilter, Ordered {
    String getRouteVersion();

    String getRouteRegion();

    String getRouteAddress();

    String getRouteVersionWeight();

    String getRouteRegionWeight();
}