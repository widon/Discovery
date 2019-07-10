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

//public abstract class GatewayStrategyRouteFilter implements Ordered, GlobalFilter {
//    String getRouteVersion() {return null;};
//
//    String getRouteRegion() {return null;};
//
//    String getRouteAddress() {return null;};
//
//    String getRouteVersionWeight() {return null;};
//
//    String getRouteRegionWeight() {return null;};
//}


public interface GatewayStrategyRouteFilter {
    String getRouteVersion();

    String getRouteRegion();

    String getRouteAddress();

    String getRouteVersionWeight();

    String getRouteRegionWeight();
}