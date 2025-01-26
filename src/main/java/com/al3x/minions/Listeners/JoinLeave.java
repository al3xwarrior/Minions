package com.al3x.minions.Listeners;

import com.al3x.minions.Instances.MinionManager;
import com.al3x.minions.Instances.Minions.Minion;
import com.al3x.minions.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeave implements Listener {

    private Main main;
    private MinionManager minionManager;

    public JoinLeave(Main main) {
        this.main = main;
        this.minionManager = main.getMinionManager();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        Minion[] minions = minionManager.getUserEquippedMinions(player);
        for (Minion minion : minions) {
            if (minion == null) continue;
            minion.destroy();
        }
    }

}
