package com.al3x.minions.Commands;

import com.al3x.minions.Enums.MinionType;
import com.al3x.minions.Instances.Minions.Minion;
import com.al3x.minions.Menus.MinionsMenu;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.al3x.minions.Utils.Color.colorize;

public class Minions implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) return false;
        Player player = (Player) commandSender;

        if (!player.hasPermission("minions.use")) {
            player.sendMessage(colorize("&cYou do not have permission to use this command."));
            return true;
        }

        if (strings.length == 0 || strings[0].equalsIgnoreCase("menu")) {
            new MinionsMenu(player).open();
            return true;
        }

        if (strings.length == 3 && strings[0].equalsIgnoreCase("give") && player.hasPermission("minions.admin")) {
            Player target = Bukkit.getPlayer(strings[1]);
            if (target == null) {
                player.sendMessage(colorize("&cPlayer not found."));
                return true;
            }

            MinionType minionType;
            try {
                minionType = MinionType.valueOf(strings[2]);
            } catch (IllegalArgumentException ex) {
                player.sendMessage(colorize("&cInvalid minion type!"));
                return true;
            }

            target.getInventory().addItem(Minion.getPhysicalItem(minionType));
            target.sendMessage(colorize("&aYou have been given a &e" + strings[2] + " &aminion."));
            player.sendMessage(colorize("&aYou have given a &e" + strings[2] + "&a minion to " + target.getDisplayName()));
            return true;
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) return null;
        Player player = (Player) commandSender;

        ArrayList<String> completions = new ArrayList<>();

        if (strings.length == 1) {
            completions.add("menu");

            if (player.hasPermission("minions.admin")) {
                completions.add("give");
            }
        }

        if (strings.length == 2 && player.hasPermission("minions.admin")) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                completions.add(onlinePlayer.getName());
            }
        }

        if (strings.length == 3 && player.hasPermission("minions.admin")) {
            for (MinionType minionType : MinionType.values()) {
                completions.add(minionType.name());
            }
        }

        return completions;
    }
}
