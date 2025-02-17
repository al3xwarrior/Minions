package com.al3x.minions.Enums;

import com.al3x.minions.Instances.Minions.ArcherMinion;
import com.al3x.minions.Instances.Minions.FighterMinion;
import com.al3x.minions.Instances.Minions.Minion;
import com.al3x.minions.Instances.Minions.WoodcutterMinion;

public enum MinionType {

    FIGHTER(FighterMinion.class),
    ARCHER(ArcherMinion.class),
//    HEALER(HealerMinion.class),
//    MINER(MinerMinion.class),
//    FARMER(FarmerMinion.class),
    WOODCUTTER(WoodcutterMinion.class);

    private final Class<? extends Minion> minionClass;

    MinionType(Class<? extends Minion> minionClass) {
        this.minionClass = minionClass;
    }

    public Class<? extends Minion> getMinionClass() {
        return minionClass;
    }

    public static MinionType fromString(String string) {
        for (MinionType type : values()) {
            if (type.name().equalsIgnoreCase(string)) {
                return type;
            }
        }
        return null;
    }

}
