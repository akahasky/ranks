package com.github.akahasky.ranks;

import com.github.akahasky.ranks.cache.RankCache;
import com.github.akahasky.ranks.command.RankCommand;
import com.github.akahasky.ranks.command.RankupCommand;
import com.github.akahasky.ranks.controller.RankController;
import com.github.akahasky.ranks.listener.ConnectionListener;
import com.github.akahasky.ranks.model.Rank;
import com.github.akahasky.ranks.repository.IRepository;
import com.github.akahasky.ranks.repository.impl.MySQLRepositoryImpl;
import com.github.akahasky.ranks.repository.impl.SQLiteRepositoryImpl;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;

public class RanksPlugin extends JavaPlugin {

    IRepository repository;
    RankCache rankCache;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        RankController rankController = new RankController(getConfig().getConfigurationSection("ranks"));

        repository = getRepository(rankController);
        rankCache = new RankCache();

        Bukkit.getPluginManager().registerEvents(new ConnectionListener(rankController, rankCache, repository), this);

        registerCommand(new RankCommand(rankCache));
        registerCommand(new RankupCommand(rankController, rankCache));

        Bukkit.getOnlinePlayers().forEach(player -> {

            Rank rank = repository.selectOne(player.getName());

            if (rank == null) rank = rankController.getFirst();

            rankCache.add(player.getName(), rank);

        });

    }

    @Override
    public void onDisable() {

        rankCache.cloneMap().forEach((name, rank) -> repository.insertOne(name, rank.getName()));

        repository.shutdown();

    }

    private IRepository getRepository(RankController rankController) {

        ConfigurationSection configurationSection = getConfig().getConfigurationSection("repository");

        if (configurationSection.getString("type").equalsIgnoreCase("MYSQL"))
            return new MySQLRepositoryImpl(rankController, configurationSection);

        return new SQLiteRepositoryImpl(rankController, getDataFolder().toPath().resolve("storage").toFile());

    }

    private void registerCommand(Command command) {

        try {

            String packageName = getServer().getClass().getPackage().getName();

            Class<?> craftServerClass = Class.forName(String.format("org.bukkit.craftbukkit.%s.CraftServer", packageName.substring(packageName.lastIndexOf('.') + 1)));
            Method getServerMethod = Bukkit.class.getMethod("getServer");

            Object craftServer = getServerMethod.invoke(null);

            Class<?> simpleCommandMapClass = Class.forName("org.bukkit.command.SimpleCommandMap");

            Method getCommandMapMethod = craftServerClass.getMethod("getCommandMap");

            Object simpleCommandMap = getCommandMapMethod.invoke(craftServer);

            Method registerMethod = simpleCommandMapClass.getMethod("register", String.class, Command.class);
            registerMethod.invoke(simpleCommandMap, command.getName(), command);

        }

        catch (Exception exception) { exception.printStackTrace(); }

    }

}
