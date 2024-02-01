package com.github.akahasky.ranks.misc.hook;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconomyHook {

    private static Economy economy;

    static {

        if (Bukkit.getPluginManager().getPlugin("Vault") != null) {

            RegisteredServiceProvider<Economy> economyServiceProvider = Bukkit.getServicesManager().getRegistration(Economy.class);

            if (economyServiceProvider != null)
                economy = economyServiceProvider.getProvider();

        }

    }

    public static void depositBalance(Player player, double value) {

        if (economy == null)
            throw new UnsupportedOperationException("Vault plugin is not active");

        economy.depositPlayer(player, value);

    }

    public static void withdrawBalance(Player player, double value) {

        if (economy == null)
            throw new UnsupportedOperationException("Vault plugin is not active");

        economy.withdrawPlayer(player, value);

    }

    public static double getBalance(Player player) {

        if (economy == null)
            throw new UnsupportedOperationException("Vault plugin is not active");

        return economy.getBalance(player);

    }

}