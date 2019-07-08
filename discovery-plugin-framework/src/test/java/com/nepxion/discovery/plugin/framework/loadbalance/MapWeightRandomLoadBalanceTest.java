package com.nepxion.discovery.plugin.framework.loadbalance;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.nepxion.discovery.common.exception.DiscoveryException;

public class MapWeightRandomLoadBalanceTest {
    public static void main(String[] args) {
        test(1000000);
    }

    public static void test(int totolCount) {
        long t = System.currentTimeMillis();

        List<Pair<String, Double>> list = new ArrayList<Pair<String, Double>>();
        list.add(new ImmutablePair<String, Double>("1.0", 10D));
        list.add(new ImmutablePair<String, Double>("2.0", 50D));
        list.add(new ImmutablePair<String, Double>("3.0", 20D));
        list.add(new ImmutablePair<String, Double>("1.0", 10D));
        list.add(new ImmutablePair<String, Double>("2.0", 50D));
        list.add(new ImmutablePair<String, Double>("3.0", 20D));
        list.add(new ImmutablePair<String, Double>("4.0", 5D));
        list.add(new ImmutablePair<String, Double>("4.0", 5D));
        list.add(new ImmutablePair<String, Double>("5.0", 15D));
        list.add(new ImmutablePair<String, Double>("5.0", 15D));

        int v1Count = 0;
        int v2Count = 0;
        int v3Count = 0;
        int v4Count = 0;
        int v5Count = 0;
        for (int i = 0; i < totolCount; i++) {
            MapWeightRandom<String, Double> weightRandom = new MapWeightRandom<String, Double>(list);
            String server = weightRandom.random();
            if (server.startsWith("1.0")) {
                v1Count++;
            }
            if (server.startsWith("2.0")) {
                v2Count++;
            }
            if (server.startsWith("3.0")) {
                v3Count++;
            }
            if (server.startsWith("4.0")) {
                v4Count++;
            }
            if (server.startsWith("5.0")) {
                v5Count++;
            }
        }

        System.out.println("------------------------------");
        System.out.println(totolCount + "次循环，散列方式随机权重准确度和性能：");
        DecimalFormat format = new DecimalFormat("0.0000");
        System.out.println("1.0版本服务随机权重=" + format.format((double) v1Count * 100 / totolCount) + "%");
        System.out.println("2.0版本服务随机权重=" + format.format((double) v2Count * 100 / totolCount) + "%");
        System.out.println("3.0版本服务随机权重=" + format.format((double) v3Count * 100 / totolCount) + "%");
        System.out.println("4.0版本服务随机权重=" + format.format((double) v4Count * 100 / totolCount) + "%");
        System.out.println("5.0版本服务随机权重=" + format.format((double) v5Count * 100 / totolCount) + "%");
        System.out.println("耗时时间：" + (System.currentTimeMillis() - t));
        System.out.println("------------------------------");
    }

    public static class MapWeightRandom<K, V extends Number> {
        private TreeMap<Double, K> weightMap = new TreeMap<Double, K>();

        public MapWeightRandom(List<Pair<K, V>> pairlist) {
            for (Pair<K, V> pair : pairlist) {
                double value = pair.getValue().doubleValue();
                if (value <= 0) {
                    continue;
                }

                double lastWeight = weightMap.size() == 0 ? 0 : weightMap.lastKey().doubleValue();
                weightMap.put(value + lastWeight, pair.getKey());
            }
        }

        public K random() {
            if (MapUtils.isEmpty(weightMap)) {
                throw new DiscoveryException("No weight value is configed");
            }

            double randomWeight = weightMap.lastKey() * Math.random();
            SortedMap<Double, K> tailMap = weightMap.tailMap(randomWeight, false);

            return weightMap.get(tailMap.firstKey());
        }
    }
}