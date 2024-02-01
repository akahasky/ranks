package com.github.akahasky.ranks.repository;

import com.github.akahasky.ranks.model.Rank;

public interface IRepository {

    void init();

    void shutdown();

    Rank selectOne(String name);

    void insertOne(String name, String rankName);

}
