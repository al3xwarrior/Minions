package com.al3x.minions;

import com.al3x.minions.Commands.Minions;
import com.al3x.minions.Instances.MinionManager;
import com.al3x.minions.Instances.Minions.Minion;
import com.al3x.minions.Listeners.JoinLeave;
import com.al3x.minions.Listeners.MenuListener;
import com.al3x.minions.Listeners.PhysicalMinionListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private MinionManager minionManager;

    @Override
    public void onEnable() {
        minionManager = new MinionManager(this);

        // Cmds
        getCommand("minions").setExecutor(new Minions());

        // Events
        Bukkit.getPluginManager().registerEvents(new JoinLeave(this), this);
        Bukkit.getPluginManager().registerEvents(new MenuListener(), this);
        Bukkit.getPluginManager().registerEvents(new PhysicalMinionListener(), this);

        Bukkit.getLogger().info("Plugin enabled");
    }

    @Override
    public void onDisable() {

        for (Minion minion : minionManager.getAllMinions()) {
            minion.destroy();
        }

        Bukkit.getLogger().info("Plugin disabled");
    }

    public MinionManager getMinionManager() {
        return minionManager;
    }

    public static Main getInstance() {
        return getPlugin(Main.class);
    }
}
