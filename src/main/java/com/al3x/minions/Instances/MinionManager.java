package com.al3x.minions.Instances;

import com.al3x.minions.Instances.Minions.Minion;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class MinionManager {

    private ArrayList<Minion> minions;

    public MinionManager() {
           minions = new ArrayList<>();
    }

    public Minion getUserEquippedMinion(Player player) {
        for (Minion minion : minions) {
            if (minion.getOwner().equals(player)) {
                return minion;
            }
        }
        return null;
    }

    public void addMinion(Minion minion) {
        minions.add(minion);
    }
    public void removeMinion(Minion minion) {
        minions.remove(minion);
    }
    public ArrayList<Minion> getMinions() {
        return minions;
    }

    public Minion getMinionByUUID(String uuid) {
        for (Minion minion : minions) {
            if (minion.getUUID().equals(UUID.fromString(uuid))) {
                return minion;
            }
        }
        return null;
    }

}
