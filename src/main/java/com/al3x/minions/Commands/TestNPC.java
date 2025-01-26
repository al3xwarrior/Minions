package com.al3x.minions.Commands;

import com.al3x.minions.Enums.MinionType;
import com.al3x.minions.Instances.Minions.ArcherMinion;
import com.al3x.minions.Instances.Minions.FighterMinion;
import com.al3x.minions.Instances.Minions.WoodcutterMinion;
import com.al3x.minions.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.al3x.minions.Utils.Color.colorize;

public class TestNPC implements TabExecutor {

    private final Main main;

    public TestNPC(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) {
            return false;
        }

        Player player = (Player) commandSender;

        MinionType type;
        try {
            type = MinionType.valueOf(strings[0]);
        } catch (IllegalArgumentException e) {
            player.sendMessage(colorize("&cInvalid Minion Type"));
            return false;
        }

        if (type == MinionType.FIGHTER)
            main.getMinionManager().addMinion(player, new FighterMinion(player));
        else if (type == MinionType.WOODCUTTER)
            main.getMinionManager().addMinion(player, new WoodcutterMinion(player));
        else if (type == MinionType.ARCHER)
            main.getMinionManager().addMinion(player, new ArcherMinion(player));
        else
            player.sendMessage(colorize("&cMinion not implemented yet :("));

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        ArrayList<String> list = new ArrayList<>();
        if (strings.length == 1) {
            for (MinionType type : MinionType.values()) {
                list.add(type.toString());
            }
        }
        return list;
    }
}
