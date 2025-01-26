package com.al3x.minions;

import com.al3x.minions.Commands.TestNPC;
import com.al3x.minions.Enums.MinionType;
import com.al3x.minions.Instances.MinionManager;
import com.al3x.minions.Instances.Minions.ArcherMinion;
import com.al3x.minions.Instances.Minions.FighterMinion;
import com.al3x.minions.Instances.Minions.Minion;
import com.al3x.minions.Instances.Minions.WoodcutterMinion;
import com.al3x.minions.Listeners.JoinLeave;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;

public final class Main extends JavaPlugin {

    private MinionManager minionManager;

    @Override
    public void onEnable() {

        minionManager = new MinionManager();

        // Cmds
        getCommand("testnpc").setExecutor(new TestNPC(this));

        // Events
        Bukkit.getPluginManager().registerEvents(new JoinLeave(), this);

        // Update Minion Positioning
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Minion minion : minionManager.getMinions()) {
                if (minion.getType() == MinionType.FIGHTER) ((FighterMinion) minion).update();
                else if (minion.getType() == MinionType.WOODCUTTER) ((WoodcutterMinion) minion).update();
                else if (minion.getType() == MinionType.ARCHER) ((ArcherMinion) minion).update();
            }
        }, 0, 2);

        Bukkit.getLogger().info("Plugin enabled");
    }

    @Override
    public void onDisable() {

        for (Minion minion : minionManager.getMinions()) {
            minion.destroy();
        }

        Bukkit.getLogger().info("Plugin disabled");
    }

    public MinionManager getMinionManager() {
        return minionManager;
    }
}
