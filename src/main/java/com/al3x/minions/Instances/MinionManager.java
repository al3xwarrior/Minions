package com.al3x.minions.Instances;

import com.al3x.minions.Enums.MinionType;
import com.al3x.minions.Instances.Minions.ArcherMinion;
import com.al3x.minions.Instances.Minions.FighterMinion;
import com.al3x.minions.Instances.Minions.Minion;
import com.al3x.minions.Instances.Minions.WoodcutterMinion;
import com.al3x.minions.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class MinionManager {

    private Main main;
    private HashMap<Player, Minion[]> minions;

    public MinionManager(Main main) {
        this.main = main;
        minions = new HashMap<>();
        initMinionUpdater();
    }

    private void initMinionUpdater() {
        // TODO: Can this be made better?
        Bukkit.getScheduler().runTaskTimer(main, () -> {
            for (Minion minion : getAllMinions()) {
                if (minion.getType() == MinionType.FIGHTER) ((FighterMinion) minion).update();
                else if (minion.getType() == MinionType.WOODCUTTER) ((WoodcutterMinion) minion).update();
                else if (minion.getType() == MinionType.ARCHER) ((ArcherMinion) minion).update();
            }
        }, 0, 2);
    }

    public void addMinion(Player player, Minion minion) {
        Minion[] playerMinions = minions.get(player);
        if (playerMinions == null) {
            playerMinions = new Minion[3];
            playerMinions[0] = minion;
            minions.put(player, playerMinions);
        } else {
            for (int i = 0; i < playerMinions.length; i++) {
                if (playerMinions[i] == null) {
                    playerMinions[i] = minion;
                    break;
                }
            }
        }
    }
    public void removeMinion(Player player, Minion minion) {
        Minion[] playerMinions = minions.get(player);
        if (playerMinions == null) return;
        for (int i = 0; i < playerMinions.length; i++) {
            if (playerMinions[i] == minion) {
                playerMinions[i].destroy();
                playerMinions[i] = null;
                break;
            }
        }
    }

    @NotNull
    public Minion[] getUserEquippedMinions(Player player) {
        if (!minions.containsKey(player)) return new Minion[3];
        return minions.get(player);
    }

    public HashMap<Player, Minion[]> getMinions() {
        return minions;
    }

    public ArrayList<Minion> getAllMinions() {
        ArrayList<Minion> allMinions = new ArrayList<>();
        Collection<Minion[]> minions = this.minions.values();
        for (Minion[] minionArray : minions) {
            for (Minion minion : minionArray) {
                if (minion != null) {
                    allMinions.add(minion);
                }
            }
        }
        return allMinions;
    }

    public Minion getMinionByUUID(String uuid) {
        Collection<Minion[]> minions = this.minions.values();
        for (Minion[] minionArray : minions) {
            for (Minion minion : minionArray) {
                if (minion != null && minion.getUUID().equals(UUID.fromString(uuid))) {
                    return minion;
                }
            }
        }
        return null;
    }

}
