package com.al3x.minions.Instances.Minions;

import com.al3x.minions.Enums.MinionType;
import com.al3x.minions.Instances.MinionManager;
import com.al3x.minions.Main;
import com.al3x.minions.Utils.ItemBuilder;
import com.al3x.minions.Utils.RayCastUtility;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.ai.Navigator;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.trait.HologramTrait;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

import static com.al3x.minions.Utils.Color.colorize;

public class Minion {

    private MinionManager minionManager = Main.getInstance().getMinionManager();

    // Minion Data
    private Player owner;
    private String name;
    private UUID uuid;
    private MinionType type;

    // Minion Stats
    private int level;
    private int rangeXZ;
    private int rangeY;
    private double reach;

    // Type Specific Data
    private boolean needsToSeeTarget;

    // Citizens Data
    private NPC npc;

    public Minion(Player owner, String name, MinionType type) {
        this.owner = owner;
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.type = type;

        this.level = 1;
        this.reach = 2.5;
        this.rangeXZ = 5;
        this.rangeY = 5;

        needsToSeeTarget = false;

        this.npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, colorize("&e" + name));
        setupNPC();
    }

    private void setupNPC() {
        npc.getOrAddTrait(SkinTrait.class).setSkinPersistent(owner);
        npc.getOrAddTrait(HologramTrait.class).addLine(colorize("&e" + name));
        npc.getOrAddTrait(HologramTrait.class).addLine(colorize("&7Level: &e" + level));

        Location location = owner.getLocation().clone().add(owner.getLocation().getDirection().normalize().multiply(-1.5));
        location.setY(owner.getLocation().getY());
        npc.spawn(location);

        owner.getWorld().spawnParticle(Particle.CLOUD, location.clone().add(0, 0.2, 0), 10, 0.4, 0.4, 0.4, 0.1);

        LivingEntity livingEntity = (LivingEntity) npc.getEntity();
        livingEntity.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(0.45);
        livingEntity.setCollidable(false);
        livingEntity.setGravity(false);
    }

    /**
     * Set the navigation point and run a task when the minion is close enough (based on their reach)
     * @param location The Target Location (ex: block, entity, etc)
     * @param closeEnough The task to run when the minion is close enough (ex: break block, hit entity, etc)
     */
    public void setGoal(Location location, Runnable closeEnough) {
        npc.faceLocation(location);
        npc.getEntity().setGravity(true);

        Navigator navigator = npc.getNavigator();
        if (npc.getEntity().getLocation().distance(location) < reach) {

            // If the minion requires that it can SEE the target and doesnt
            // due to something in its way, dont stop the navigation
            // TODO: Doesnt work like at all
            if (needsToSeeTarget) {
                RayCastUtility.RayCastResult result = RayCastUtility.rayCastEntities((LivingEntity) npc.getEntity(), reach + 1, false, RayCastUtility.Precision.IMPRECISE_ENTITY);
                if (result.isEmpty() || result.getType() == RayCastUtility.ResultType.EMPTY) {
                    return;
                }
            }

            if (navigator.isNavigating()) navigator.cancelNavigation();
            closeEnough.run();
            return;
        }

        if (!navigator.isNavigating() || navigator.isPaused()) {
            navigator.setTarget(location);
        }
    }

    public void updateLocation() {
        if (getNPC().getNavigator().isNavigating()) getNPC().getNavigator().cancelNavigation();
        npc.getEntity().setGravity(false);
        Location baseLocation = owner.getLocation().clone().add(owner.getLocation().getDirection().normalize().multiply(-1.5));
        baseLocation.setY(owner.getLocation().getY());

        Minion[] playerMinions = minionManager.getUserEquippedMinions(owner);
        ArrayList<Minion> sortedMinions = new ArrayList<>();

        for (Minion minion : playerMinions) {
            if (minion != null) {
                sortedMinions.add(minion);
            }
        }

        if (sortedMinions.size() == 1) {
            npc.getEntity().teleport(baseLocation);
            return;
        }

        int index = sortedMinions.indexOf(this);
        if (sortedMinions.size() == 2) {
            if (index == 0) {
                // Slightly more left (behind the player and 1 block left based on their direction)
                Location leftLocation = baseLocation.clone().add(owner.getLocation().getDirection().normalize().multiply(-1)).add(owner.getLocation().getDirection().normalize().rotateAroundY(Math.toRadians(90)).multiply(1));
                npc.getEntity().teleport(leftLocation);
            } else {
                // Slightly more right (behind the player and 1 block right based on their direction)
                Location rightLocation = baseLocation.clone().add(owner.getLocation().getDirection().normalize().multiply(-1)).add(owner.getLocation().getDirection().normalize().rotateAroundY(Math.toRadians(-90)).multiply(1));
                npc.getEntity().teleport(rightLocation);
            }
            return;
        }

        if (sortedMinions.size() == 3) {
            if (index == 0) {
                // Left
                Location leftLocation = baseLocation.clone().add(owner.getLocation().getDirection().normalize().multiply(-1)).add(owner.getLocation().getDirection().normalize().rotateAroundY(Math.toRadians(90)).multiply(1.5));
                npc.getEntity().teleport(leftLocation);
            } else if (index == 1) {
                // Middle
                npc.getEntity().teleport(baseLocation);
            } else {
                // Right
                Location rightLocation = baseLocation.clone().add(owner.getLocation().getDirection().normalize().multiply(-1)).add(owner.getLocation().getDirection().normalize().rotateAroundY(Math.toRadians(-90)).multiply(1.5));
                npc.getEntity().teleport(rightLocation);
            }
        }
    }

    public void updatePersonality() {
        npc.setSneaking(owner.isSneaking());
    }

    public void updateEquipment(Equipment.EquipmentSlot slot, ItemStack item) {
        npc.getOrAddTrait(Equipment.class).set(slot, item);
    }

    /**
     * Destroy the citizens npc, delete it, and remove it from the registry
     */
    public void destroy() {
        Location location = owner.getLocation().clone().add(owner.getLocation().getDirection().normalize().multiply(-1.5));
        location.setY(owner.getLocation().getY());
        owner.getWorld().spawnParticle(Particle.CLOUD, location.clone().add(0, 0.2, 0), 8, 0.4, 0.4, 0.4, 0.1);

        npc.despawn();
        npc.destroy();
    }

    public ItemStack getMenuItem() {
        return new ItemBuilder(Material.NETHER_STAR)
                .setName("&6" + name)
                .setLore(
                        "&7Level: &e" + level +
                        "\n&7Type: &a" + type +
                        "\n\n&e&lCLICK to Un-Equip"
                )
                .build();
    }
    public ItemStack getPhysicalItem() {
        return new ItemBuilder(Material.NETHER_STAR)
                .setName("&6" + name)
                .setLore("&7Level: &e" + level + "\n&7Type: &a" + type)
                .setNBTString("minionUUID", uuid.toString())
                .setNBTInt("minionLevel", level)
                .setNBTString("minionType", type.toString())
                .build();
    }
    public static ItemStack getPhysicalItem(MinionType type) {
        return new ItemBuilder(Material.NETHER_STAR)
                .setName("&6" + type)
                .setNBTString("minionUUID", String.valueOf(UUID.randomUUID()))
                .setNBTInt("minionLevel", 1)
                .setNBTString("minionType", type.toString())
                .build();
    }

    public MinionType getType() {
        return type;
    }
    public Player getOwner() {
        return owner;
    }
    public NPC getNPC() {
        return npc;
    }
    public String getName() {
        return name;
    }
    public UUID getUUID() {
        return uuid;
    }
    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public void setReach(double reach) {
        this.reach = reach;
    }
    public boolean isNeedsToSeeTarget() {
        return needsToSeeTarget;
    }
    public void setNeedsToSeeTarget(boolean needsToSeeTarget) {
        this.needsToSeeTarget = needsToSeeTarget;
    }
    public double getReach() {
        return reach;
    }

    public int getRangeXZ() {
        return rangeXZ;
    }

    public void setRangeXZ(int rangeXZ) {
        this.rangeXZ = rangeXZ;
    }

    public int getRangeY() {
        return rangeY;
    }

    public void setRangeY(int rangeY) {
        this.rangeY = rangeY;
    }
}
