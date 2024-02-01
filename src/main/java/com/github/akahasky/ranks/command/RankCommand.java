package com.github.akahasky.ranks.command;

import com.github.akahasky.ranks.cache.RankCache;
import com.github.akahasky.ranks.model.Rank;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class RankCommand extends Command {

    private final RankCache rankCache;

    public RankCommand(RankCache rankCache) {

        super("rank");

        this.rankCache = rankCache;

    }

    @Override
    public boolean execute(CommandSender commandSender, String label, String[] arguments) {

        if (commandSender instanceof ConsoleCommandSender) return false;

        Rank currentRank = rankCache.get(commandSender.getName());

        if (currentRank == null) {

            commandSender.sendMessage("§cYour information is being loaded, wait a few seconds and try again.");
            return false;

        }

        commandSender.sendMessage(String.format("§a§lRANK! §fYour current rank is: §7%s", currentRank.getName()));
        return false;

    }

}
