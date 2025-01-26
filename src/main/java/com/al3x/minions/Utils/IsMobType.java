package com.al3x.minions.Utils;

import org.bukkit.entity.*;

public class IsMobType {

    public static boolean isFriendlyMob(Entity entity) {
        return
                entity instanceof Pig ||
                entity instanceof Cow ||
                entity instanceof Sheep ||
                entity instanceof Chicken
                ;
    }

}
