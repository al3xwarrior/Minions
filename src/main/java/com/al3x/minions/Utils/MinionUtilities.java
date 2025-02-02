package com.al3x.minions.Utils;

import com.al3x.minions.Enums.BlockTarget;
import com.al3x.minions.Enums.EntityTarget;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.*;

import java.util.ArrayList;

public class MinionUtilities {

    /**
     * Get a block in a range of a location
     * @param owner owner of the minion
     * @param rangeXZ range in the x and z axis
     * @param rangeY range in the y axis
     * @param targets what should be whitelisted in the search
     * @return closest block based on the specifications or null if none are found
     */
    public static Block getClosestBlock(Player owner, int rangeXZ, int rangeY, BlockTarget targets) {
        Location location = owner.getLocation();
        ArrayList<Block> blocks = new ArrayList<>();

        for (int x = -rangeXZ; x < rangeXZ; x++) {
            for (int y = -rangeY; y < rangeY; y++) {
                for (int z = -rangeXZ; z < rangeXZ; z++) {
                    blocks.add(location.getWorld().getBlockAt(location.clone().add(x, y, z)));
                }
            }
        }

        sortBlocks(blocks, targets);

        Block closestBlock = null;
        double closestDistance = (rangeXZ * 2) + rangeY;

        for (Block block : blocks) {
            double distance = block.getLocation().distance(location);
            if (distance < closestDistance) {
                closestBlock = block;
                closestDistance = distance;
            }
        }

        return closestBlock;
    }

    private static void sortBlocks(ArrayList<Block> blocks, BlockTarget targets) {
        blocks.removeIf(block -> block.getType().isAir());

        if (targets == BlockTarget.WOOD) {
            blocks.removeIf(block -> !block.getType().name().toLowerCase().contains("log"));
        }
    }

    /**
     * Get entities in a range of a location
     * @param owner owner of the minion
     * @param rangeXZ range in the x and z axis
     * @param rangeY range in the y axis
     * @param targets what should be whitelisted in the search
     * @return closest player based on the specifications or null if none are found
     */
    public static Entity getClosestEntity(Player owner, int rangeXZ, int rangeY, EntityTarget targets) {
        Location location = owner.getLocation();
        ArrayList<Entity> entities = new ArrayList<>(location.getNearbyEntities(rangeXZ, rangeY, rangeXZ));
        entities.remove(owner);

        sortEntities(entities, targets);

        Entity closestEntity = null;
        double closestDistance = (rangeXZ * 2) + rangeY;

        for (Entity entity : entities) {
            double distance = entity.getLocation().distance(location);
            if (distance < closestDistance) {
                closestEntity = entity;
                closestDistance = distance;
            }
        }

        return closestEntity;
    }

    private static void sortEntities(ArrayList<Entity> entities, EntityTarget targets) {
        entities.removeIf(Entity::isDead);
        entities.removeIf(entity -> entity.hasMetadata("NPC"));
        entities.removeIf(entity -> entity instanceof ArmorStand);
        entities.removeIf(entity -> entity instanceof Item);

        if (targets == EntityTarget.FRIENDLY) {
            entities.removeIf(entity -> !isFriendlyMob(entity));
        }
        if (targets == EntityTarget.HOSTILE) {
            entities.removeIf(entity -> !isHostileMob(entity));
        }
        if (targets == EntityTarget.MOBS) {
            entities.removeIf(entity -> !isMob(entity));
        }
        if (targets == EntityTarget.PLAYER) {
            entities.removeIf(entity -> !isPlayer(entity));
        }
    }

    public static boolean isFriendlyMob(Entity entity) {
        return !(entity instanceof Monster);
    }

    public static boolean isHostileMob(Entity entity) {
        return entity instanceof Monster;
    }

    public static boolean isPlayer(Entity entity) {
        return entity instanceof Player;
    }

    // Prob could be replaced but whatever
    public static boolean isMob(Entity entity) {
        return entity instanceof Creature;
    }
}
