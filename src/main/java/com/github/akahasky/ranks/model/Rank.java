package com.github.akahasky.ranks.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data @Builder
public class Rank {

    private final String name;
    private final int priority;

    private final double priceToUpgrade;
    private final List<String> upgradeCommands;

}
