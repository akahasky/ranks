package com.github.akahasky.ranks.command;

import com.github.akahasky.ranks.cache.RankCache;
import com.github.akahasky.ranks.controller.RankController;
import com.github.akahasky.ranks.misc.formatter.NumberFormatter;
import com.github.akahasky.ranks.misc.hook.EconomyHook;
import com.github.akahasky.ranks.model.Rank;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class RankupCommand extends Command {

    private final RankController rankController;
    private final RankCache rankCache;

    public RankupCommand(RankController rankController, RankCache rankCache) {

        super("rankup");

        this.rankController = rankController;
        this.rankCache = rankCache;

    }

    @Override
    public boolean execute(CommandSender commandSender, String label, String[] arguments) {

        if (commandSender instanceof ConsoleCommandSender) return false;

        Player player = (Player) commandSender;
        Rank currentRank = rankCache.get(player.getName());

        if (currentRank == null) {

            player.sendMessage("§cYour information is being loaded, wait a few seconds and try again.");
            return false;

        }

        Rank nextRank = rankController.getNext(currentRank);

        if (nextRank == null) {

            player.sendMessage("§cYou're already in the last rank.");
            return false;

        }

        if (EconomyHook.getBalance(player) < nextRank.getPriceToUpgrade()) {

            player.sendMessage(String.format("§cYou need to own %s coins to progress to the %s§c rank.", NumberFormatter.format(nextRank.getPriceToUpgrade()), nextRank.getName()));
            return false;

        }

        EconomyHook.withdrawBalance(player, nextRank.getPriceToUpgrade());
        rankCache.add(player.getName(), nextRank);

        nextRank.getUpgradeCommands().stream()
                .map(command -> command.replace("<player>", player.getName()))
                .map(command -> command.replace("<rank>", nextRank.getName()))
                .forEach(formattedCommand -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), formattedCommand));

        commandSender.sendMessage(String.format("§a§lRANK! §fYou have evolved to rank: §7%s", nextRank.getName()));
        return false;

    }

}
