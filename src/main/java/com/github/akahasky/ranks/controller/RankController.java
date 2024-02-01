package com.github.akahasky.ranks.controller;

import com.github.akahasky.ranks.model.Rank;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RankController {

    private final List<Rank> ranks;

    public RankController(ConfigurationSection configurationSection) {

        ranks = configurationSection.getKeys(false).stream().map(key -> {

            String name = ChatColor.translateAlternateColorCodes('&', configurationSection.getString(key + ".name"));
            int priority = configurationSection.getInt(key + ".priority");

            double priceToUpgrade = configurationSection.getDouble(key + ".priceToUpgrade");
            List<String> upgradeCommands = configurationSection.getStringList(key + ".upgradeCommands");

            return Rank.builder().name(name).priority(priority)
                    .priceToUpgrade(priceToUpgrade).upgradeCommands(upgradeCommands).build();

        }).sorted(Comparator.comparingInt(Rank::getPriority)).collect(Collectors.toList());

    }

    public Rank get(String rankName) { return ranks.stream()
            .filter(rank -> rank.getName().equals(rankName)).findFirst().orElse(null); }

    public Rank getFirst() { return ranks.get(0); }

    public Rank getNext(Rank rank) {

        int indexOf = ranks.indexOf(rank);

        if (indexOf < 0 || indexOf >= ranks.size() - 1) return null;

        return ranks.get(indexOf + 1);

    }

}
