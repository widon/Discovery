package com.nepxion.discovery.plugin.strategy.zuul.filter;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.nepxion.discovery.common.entity.RuleEntity;
import com.nepxion.discovery.common.entity.StrategyConditionEntity;
import com.nepxion.discovery.common.entity.StrategyCustomizationEntity;
import com.nepxion.discovery.common.entity.StrategyRouteEntity;
import com.nepxion.discovery.common.entity.StrategyType;

public class CustomizationZuulStrategyRouteFilter extends DefaultZuulStrategyRouteFilter {
    // 从远程配置中心或者本地配置文件获取版本路由配置。如果是远程配置中心，则值会动态改变
    @Override
    public String getRouteVersion() {
        StrategyConditionEntity strategyConditionEntity = getTriggeredStrategyConditionEntity();
        if (strategyConditionEntity != null) {
            String versionId = strategyConditionEntity.getVersionId();
            StrategyRouteEntity strategyRouteEntity = getTriggeredStrategyRouteEntity(versionId, StrategyType.VERSION);
            if (strategyRouteEntity != null) {
                return strategyRouteEntity.getValue();
            }
        }

        return super.getRouteVersion();
    }

    // 从远程配置中心或者本地配置文件获取区域路由配置。如果是远程配置中心，则值会动态改变
    @Override
    public String getRouteRegion() {
        StrategyConditionEntity strategyConditionEntity = getTriggeredStrategyConditionEntity();
        if (strategyConditionEntity != null) {
            String regionId = strategyConditionEntity.getRegionId();
            StrategyRouteEntity strategyRouteEntity = getTriggeredStrategyRouteEntity(regionId, StrategyType.REGION);
            if (strategyRouteEntity != null) {
                return strategyRouteEntity.getValue();
            }
        }

        return super.getRouteRegion();
    }

    // 从远程配置中心或者本地配置文件获取IP地址和端口路由配置。如果是远程配置中心，则值会动态改变
    @Override
    public String getRouteAddress() {
        StrategyConditionEntity strategyConditionEntity = getTriggeredStrategyConditionEntity();
        if (strategyConditionEntity != null) {
            String addressId = strategyConditionEntity.getAddressId();
            StrategyRouteEntity strategyRouteEntity = getTriggeredStrategyRouteEntity(addressId, StrategyType.ADDRESS);
            if (strategyRouteEntity != null) {
                return strategyRouteEntity.getValue();
            }
        }

        return super.getRouteAddress();
    }

    // 从远程配置中心或者本地配置文件获取版本权重配置。如果是远程配置中心，则值会动态改变
    @Override
    public String getRouteVersionWeight() {
        StrategyConditionEntity strategyConditionEntity = getTriggeredStrategyConditionEntity();
        if (strategyConditionEntity != null) {
            String versionWeightId = strategyConditionEntity.getVersionWeightId();
            StrategyRouteEntity strategyRouteEntity = getTriggeredStrategyRouteEntity(versionWeightId, StrategyType.VERSION_WEIGHT);
            if (strategyRouteEntity != null) {
                return strategyRouteEntity.getValue();
            }
        }

        return super.getRouteVersionWeight();
    }

    // 从远程配置中心或者本地配置文件获取区域权重配置。如果是远程配置中心，则值会动态改变
    @Override
    public String getRouteRegionWeight() {
        StrategyConditionEntity strategyConditionEntity = getTriggeredStrategyConditionEntity();
        if (strategyConditionEntity != null) {
            String regionWeightId = strategyConditionEntity.getRegionWeightId();
            StrategyRouteEntity strategyRouteEntity = getTriggeredStrategyRouteEntity(regionWeightId, StrategyType.REGION_WEIGHT);
            if (strategyRouteEntity != null) {
                return strategyRouteEntity.getValue();
            }
        }

        return super.getRouteRegionWeight();
    }

    private StrategyRouteEntity getTriggeredStrategyRouteEntity(String id, StrategyType type) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }

        RuleEntity ruleEntity = pluginAdapter.getRule();
        if (ruleEntity != null) {
            StrategyCustomizationEntity strategyCustomizationEntity = ruleEntity.getStrategyCustomizationEntity();
            if (strategyCustomizationEntity != null) {
                List<StrategyRouteEntity> strategyRouteEntityList = strategyCustomizationEntity.getStrategyRouteEntityList();
                for (StrategyRouteEntity strategyRouteEntity : strategyRouteEntityList) {
                    if (StringUtils.equals(strategyRouteEntity.getId(), id) && strategyRouteEntity.getType() == type) {
                        return strategyRouteEntity;
                    }
                }
            }
        }

        return null;
    }

    private StrategyConditionEntity getTriggeredStrategyConditionEntity() {
        RuleEntity ruleEntity = pluginAdapter.getRule();
        if (ruleEntity != null) {
            StrategyCustomizationEntity strategyCustomizationEntity = ruleEntity.getStrategyCustomizationEntity();
            if (strategyCustomizationEntity != null) {
                List<StrategyConditionEntity> strategyConditionEntityList = strategyCustomizationEntity.getStrategyConditionEntityList();
                for (StrategyConditionEntity strategyConditionEntity : strategyConditionEntityList) {
                    boolean isTriggered = isTriggered(strategyConditionEntity);
                    if (isTriggered) {
                        return strategyConditionEntity;
                    }
                }
            }
        }

        return null;
    }

    private boolean isTriggered(StrategyConditionEntity strategyConditionEntity) {
        Map<String, String> headerMap = strategyConditionEntity.getHeaderMap();
        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            String headerName = entry.getKey();
            String headerValue = entry.getValue();

            String value = strategyContextHolder.getHeader(headerName);
            if (!StringUtils.equals(headerValue, value)) {
                return false;
            }
        }

        return true;
    }
}