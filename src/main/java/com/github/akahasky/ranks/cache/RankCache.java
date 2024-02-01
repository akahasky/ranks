package com.github.akahasky.ranks.cache;

import com.github.akahasky.ranks.model.Rank;
import com.google.common.collect.Maps;

import java.util.Map;

public class RankCache {

    private final Map<String, Rank> cache = Maps.newHashMap();

    public void add(String name, Rank rank) { cache.put(name, rank); }

    public void remove(String name) { cache.remove(name); }

    public Rank get(String name) { return cache.get(name); }

    public Map<String, Rank> cloneMap() { return Maps.newHashMap(cache); }

}
