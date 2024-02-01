package com.github.akahasky.ranks.listener;

import com.github.akahasky.ranks.cache.RankCache;
import com.github.akahasky.ranks.controller.RankController;
import com.github.akahasky.ranks.model.Rank;
import com.github.akahasky.ranks.repository.IRepository;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.concurrent.CompletableFuture;

public class ConnectionListener implements Listener {

    private final RankController rankController;
    private final RankCache rankCache;

    private final IRepository repository;

    public ConnectionListener(RankController rankController, RankCache rankCache, IRepository repository) {

        this.rankController = rankController;
        this.rankCache = rankCache;

        this.repository = repository;

    }

    @EventHandler
    void on(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        CompletableFuture.supplyAsync(() -> repository.selectOne(player.getName())).whenCompleteAsync((rank, throwable) -> {

            if (throwable != null) throwable.printStackTrace();

            if (rank == null) rank = rankController.getFirst();

            rankCache.add(player.getName(), rank);

        });

    }

    @EventHandler
    void on(PlayerQuitEvent event) {

        Player player = event.getPlayer();
        Rank rank = rankCache.get(player.getName());

        if (rank == null) return;

        repository.insertOne(player.getName(), rank.getName());
        rankCache.remove(player.getName());
    }

}
