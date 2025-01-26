package com.al3x.minions.Instances.Minions;

import com.al3x.minions.Enums.MinionType;
import com.al3x.minions.Utils.IsMobType;
import net.citizensnpcs.api.trait.trait.Equipment;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Collection;

public class ArcherMinion extends Minion {

    private int rangeXZ;
    private int rangeY;
    private int cooldown;
    private int cooldownTimer = 0;

    public ArcherMinion(Player player) {
        super(player, "Archer Minion", MinionType.ARCHER);
        equipNPC();
        this.rangeXZ = 12;
        this.rangeY = 5;
        this.cooldown = 5;

        setReach(4.5);
        setNeedsToSeeTarget(true);
    }

    public void update() {
        super.updatePersonality();

        // Get Entities in Chunk
        Collection<Entity> entities = getOwner().getLocation().getNearbyEntities(rangeXZ, rangeY, rangeXZ);

        Entity closestEntity = null;
        double closestDistance = 99;

        for (Entity entity : entities) {
            if (IsMobType.isFriendlyMob(entity) && !entity.isDead() && !entity.isInvisible()) {
                double distance = entity.getLocation().distance(getNPC().getEntity().getLocation());
                if (distance < closestDistance) {
                    closestEntity = entity;
                    closestDistance = distance;
                }
            }
        }

        if (closestEntity != null) {
            super.setGoal(closestEntity.getLocation(), () -> {
                // Shoot arrow from npc
                if (cooldownTimer <= 0) {
                    cooldownTimer = cooldown;
                    getNPC().getEntity().getWorld().spawnArrow(getNPC().getEntity().getLocation().clone().add(0, 0.6, 0), getNPC().getEntity().getLocation().getDirection(), 2.5f, 1.0f);
                } else {
                    cooldownTimer--;
                }
            });
        } else {
            super.updateLocation();
        }
    }

    private void equipNPC() {
        super.updateEquipment(Equipment.EquipmentSlot.HAND, new ItemStack(Material.BOW));
        super.updateEquipment(Equipment.EquipmentSlot.HELMET, new ItemStack(Material.LEATHER_HELMET));
        super.updateEquipment(Equipment.EquipmentSlot.CHESTPLATE, new ItemStack(Material.CHAINMAIL_CHESTPLATE));
        super.updateEquipment(Equipment.EquipmentSlot.LEGGINGS, new ItemStack(Material.CHAINMAIL_LEGGINGS));
        super.updateEquipment(Equipment.EquipmentSlot.BOOTS, new ItemStack(Material.LEATHER_BOOTS));
    }

}
