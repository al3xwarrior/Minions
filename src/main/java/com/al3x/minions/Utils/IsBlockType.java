package com.al3x.minions.Utils;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class IsBlockType {

    public static boolean isWood(Block block) {
        Material material = block.getType();
        return
                material == Material.OAK_LOG ||
                material == Material.BIRCH_LOG ||
                material == Material.SPRUCE_LOG ||
                material == Material.JUNGLE_LOG ||
                material == Material.ACACIA_LOG ||
                material == Material.DARK_OAK_LOG
                ;
    }

}
