package com.al3x.minions.Utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Version independent ray-cast. Will ray-cast from an entities eye location with its pitch and yaw.
 * Will stop on hitting either an entity or a block. If it hits neither it will return an
 * {@link ResultType#EMPTY} with a null entity and block.
 * <br>
 * All tests were run using a 1.16.5 server with 2GB of RAM using the highest preciseness. For {@link #rayCastBlocks(LivingEntity, double, boolean, Precision)} I used
 * {@link Precision#PRECISE_BLOCK}, for {@link #rayCastEntities(LivingEntity, double, boolean, Precision)} I used {@link Precision#PRECISE_ENTITY} and for {@link #rayCast(LivingEntity, double, boolean, Precision)}
 * I used {@link Precision#PRECISE_ENTITY}
 * <br>
 * <b>TEST RESULTS:</b>
 * <br>
 * <b>NOTE:</b> Entity results will very, based on how many entities are loaded.
 * <ol>
 *     Block Ray-Cast: 80.07 ms, Precision: {@link Precision#PRECISE_BLOCK}
 *     <ol>
 *         Successfully completed 1000 ray-casts <br>
 *         TPS: No noticeable TPS change.
 *     </ol> <br>
 *     Entity Ray-Cast: 395.43 ms, Precision: {@link Precision#PRECISE_ENTITY}
 *     <ol>
 *         Successfully completed 1000 ray-casts <br>
 *          TPS: No noticeable TPS change.
 *     </ol> <br>
 *     Full Ray-Cast (Block and entity): 437.01 ms, Precision: {@link Precision#PRECISE_ENTITY}
 *     <ol>
 *            Successfully completed 1000 ray-casts <br>
 *            TPS: No noticeable TPS change.
 *     </ol> <br>
 *
 *     Block Ray-Cast: 61.64 ms, Precision: {@link Precision#ACCURATE_BLOCK}
 *     <ol>
 *         Successfully completed 1000 ray-casts <br>
 *         TPS: No noticeable TPS change.
 *     </ol> <br>
 *     Entity Ray-Cast: 225.14 ms, Precision: {@link Precision#ACCURATE_ENTITY}
 *     <ol>
 *         Successfully completed 1000 ray-casts <br>
 *          TPS: No noticeable TPS change.
 *     </ol> <br>
 *     Full Ray-Cast (Block and entity): 253.52 ms, Precision: {@link Precision#ACCURATE_ENTITY}
 *     <ol>
 *            Successfully completed 1000 ray-casts <br>
 *            TPS: No noticeable TPS change.
 *     </ol> <br>
 *
 *     Block Ray-Cast: 71.90 ms (Lol, wow), Precision: {@link Precision#SEMI_ACCURATE_BLOCK}
 *     <ol>
 *         Successfully completed 1000 ray-casts <br>
 *         TPS: No noticeable TPS change.
 *     </ol> <br>
 *     Entity Ray-Cast: 97.18 ms (Wow that's a lot of improvement), Precision: {@link Precision#SEMI_ACCURATE_ENTITY}
 *     <ol>
 *         Successfully completed 1000 ray-casts <br>
 *          TPS: No noticeable TPS change.
 *     </ol> <br>
 *     Full Ray-Cast (Block and entity): 177.97 ms, Precision: {@link Precision#SEMI_ACCURATE_ENTITY}
 *     <ol>
 *            Successfully completed 1000 ray-casts <br>
 *            TPS: No noticeable TPS change.
 *     </ol> <br>
 *
 *     Block Ray-Cast: 61.22 ms, Precision: {@link Precision#IMPRECISE_BLOCK}
 *     <ol>
 *         Successfully completed 1000 ray-casts <br>
 *         TPS: No noticeable TPS change.
 *     </ol> <br>
 *     Entity Ray-Cast: 71.96 ms, Precision: {@link Precision#IMPRECISE_ENTITY}
 *     <ol>
 *         Successfully completed 1000 ray-casts <br>
 *          TPS: No noticeable TPS change.
 *     </ol> <br>
 *     Full Ray-Cast (Block and entity): 120.24 ms, Precision: {@link Precision#IMPRECISE_ENTITY}
 *     <ol>
 *            Successfully completed 1000 ray-casts <br>
 *            TPS: No noticeable TPS change.
 *     </ol> <br>
 *
 * </ol>
 * <b>VERDICT:</b> You should probably use {@link Precision#ACCURATE_BLOCK} and I think you should probably use {@link Precision#PRECISE_ENTITY} or {@link Precision#ACCURATE_ENTITY}. Despite those
 * being resource hogs they are the only accurate way. (Sadly)
 */
public class RayCastUtility {

    /**
     * Ray-casts only blocks. Doesn't affect performance very much. Using a 1.16.5 server and calling this 1000 times, there was minimal tps issue. (Using the highest preciseness)
     * This will ignore entities! If you'd like this to not happen please use
     {@link #rayCast(LivingEntity, double, boolean, Precision)
      *
      * @param entity        Entity to ray-cast from
      * @param maxDistance   Maximum distance to ray-cast
      * @param ignoreLiquids Whether to factor in liquids. If true, will not stop ray-casting at a liquid.
      * @param precision     How many blocks (or fractions of) to advance before every next check
      * @return A ray-casted block result or an empty block result
     */
    public static BlockRayCastResult rayCastBlocks(LivingEntity entity, double maxDistance, boolean ignoreLiquids, Precision precision) {

        Location starting = entity.getEyeLocation();
        Vector direction = starting.getDirection();
        Location check = starting.clone();
        Location last = starting.clone();

        double distanceTraveled = 0;
        while (distanceTraveled < maxDistance) {
            last = check.clone();
            check = getRayTraceLocation(check, direction, precision.getAdvance());
            if (check.getBlock().getType() != Material.AIR && !(check.getBlock().isLiquid() && ignoreLiquids)) {
                break;
            }
            distanceTraveled += precision.getAdvance();
        }

        Block block = check.getBlock();
        BlockFace face = block.getFace(last.getBlock());


        if (block.getType() == Material.AIR || (ignoreLiquids && block.isLiquid())) {
            return new BlockRayCastResult(ResultType.EMPTY, block, face);
        }
        return new BlockRayCastResult(ResultType.BLOCK, block, face);
    }

    /**
     * Ray-casts only entities. Affect performance quite a bit. Using a 1.16.5 server and calling this 1000 time, it took quite a bit of time. (Using the highest preciseness)
     *
     * @param entity        Entity to ray-cast from
     * @param maxDistance   Maximum distance to ray-cast
     * @param ignoreLiquids Whether to factor in liquids. If true, will not stop ray-casting at a liquid.
     * @param precision     How many blocks (or fractions of) to advance before every next check
     * @return A ray-casted entity result or an empty entity result
     */
    public static EntityRayCastResult rayCastEntities(LivingEntity entity, double maxDistance, boolean ignoreLiquids, Precision precision) {
        Location starting = entity.getEyeLocation();
        Vector direction = starting.getDirection();
        Location check = starting.clone();
        BoundingBox bb = new BoundingBox(check, getRayTraceLocation(starting, direction, maxDistance + 0.5D));
        List<Entity> entityList = new ArrayList<>(entity.getNearbyEntities(maxDistance + 0.5, maxDistance + 0.5, maxDistance + 0.5)).stream().filter(e -> e != entity).filter(e -> bb.isWithinBoundingBox(e.getLocation())).collect(Collectors.toList());
        Entity hitResult = null;
        double distanceTraveled = 0;
        while (distanceTraveled < maxDistance) {
            check = getRayTraceLocation(check, direction, precision.getAdvance());
            if (check.getBlock().getType() != Material.AIR && !(check.getBlock().isLiquid() && ignoreLiquids)) {
                break;
            }
            List<Entity> results = new ArrayList<>();
            for (Entity e : entityList) {
                if (e.equals(entity)) {
                    continue;
                }
                try {
                    Object entityBoundingBox = e.getClass().getMethod("getBoundingBox").invoke(e);
                    BoundingBox entBB = new BoundingBox(entityBoundingBox);
                    if (entBB.isWithinBoundingBox(check)) {
                        results.add(e);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (results.size() > 0) {
                Entity closest = results.get(0);
                for (int i = 1; i < results.size(); i++) {
                    if (results.get(i).getLocation().distance(check) < closest.getLocation().distance(check)) {
                        closest = results.get(i);
                    }
                }
                hitResult = closest;
                break;
            }
            distanceTraveled += precision.getAdvance();
        }

        if (hitResult == null) {
            return new EntityRayCastResult(ResultType.EMPTY, null);
        }
        return new EntityRayCastResult(ResultType.ENTITY, hitResult);

    }

    /**
     * Ray-casts entities and blocks. Affect performance quite a bit. Using a 1.16.5 server and calling this 1000 times, it took quite a bit of time. (Using the highest preciseness)
     *
     * @param entity        Entity to ray-cast from
     * @param maxDistance   Maximum distance to ray-cast
     * @param ignoreLiquids Whether to factor in liquids. If true, will not stop ray-casting at a liquid.
     * @param precision     How many blocks (or fractions of) to advance before every next check
     * @return A ray-cast result or an empty result
     */
    public static RayCastResult rayCast(LivingEntity entity, double maxDistance, boolean ignoreLiquids, Precision precision) {
        Location starting = entity.getEyeLocation();
        Vector direction = starting.getDirection();
        Location check = starting.clone();
        Location last = starting.clone();
        BoundingBox bb = new BoundingBox(check, getRayTraceLocation(starting, direction, maxDistance + 0.5D));
        List<Entity> entityList = new ArrayList<>(entity.getNearbyEntities(maxDistance + 0.5, maxDistance + 0.5, maxDistance + 0.5)).stream().filter(e -> e != entity).filter(e -> bb.isWithinBoundingBox(e.getLocation())).collect(Collectors.toList());
        Entity hitResult = null;
        double distanceTraveled = 0;
        Block blockResult = null;
        while (distanceTraveled < maxDistance) {
            last = check.clone();
            check = getRayTraceLocation(check, direction, precision.getAdvance());
            if (check.getBlock().getType() != Material.AIR && !(check.getBlock().isLiquid() && ignoreLiquids)) {
                blockResult = check.getBlock();
                break;
            }
            List<Entity> results = new ArrayList<>();
            for (Entity e : entityList) {
                if (e.equals(entity)) {
                    continue;
                }
                try {
                    Object entityBoundingBox = e.getClass().getMethod("getBoundingBox").invoke(e);
                    BoundingBox entBB = new BoundingBox(entityBoundingBox);
                    if (entBB.isWithinBoundingBox(check)) {
                        results.add(e);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (results.size() > 0) {
                Entity closest = results.get(0);
                for (int i = 1; i < results.size(); i++) {
                    if (results.get(i).getLocation().distance(check) < closest.getLocation().distance(check)) {
                        closest = results.get(i);
                    }
                }
                hitResult = closest;
                break;
            }
            distanceTraveled += precision.getAdvance();
        }

        if (hitResult == null) {
            if (blockResult == null) {
                return new RayCastResult(ResultType.EMPTY, null);
            } else {
                return new BlockRayCastResult(ResultType.BLOCK, blockResult, blockResult.getFace(last.getBlock()));
            }
        }
        return new EntityRayCastResult(ResultType.ENTITY, hitResult);
    }

    public static Location getRayTraceLocation(Location starting, Vector direction, double distance) {
        Location ending = starting.clone().add(direction.clone().multiply(distance));
        return ending;
    }

    /**
     * My general advice for entity ray-casting is to just use {@link #PRECISE_ENTITY}, sorry or if you're willing to have some hit or miss.
     * Use {@link #ACCURATE_ENTITY}. I'm pretty sure {@link #ACCURATE_BLOCK} will work pretty well for most cases.
     */
    public enum Precision {

        /**
         * This is kind of pointless since block ray-casts aren't very heavy, performance wise.
         */
        IMPRECISE_BLOCK(1D),
        /**
         * Definitely inaccurate but saves on performance
         */
        IMPRECISE_ENTITY(0.25D),
        SEMI_ACCURATE_BLOCK(0.5D),
        /**
         * Messes up quite a bit not necessarily good for mobs with thin hitboxes.
         */
        SEMI_ACCURATE_ENTITY(0.125D),
        /**
         * Good for general purpose
         */
        ACCURATE_BLOCK(0.25D),
        /**
         * Good for general purpose, messes up a bit
         */
        ACCURATE_ENTITY(0.05D),
        /**
         * You should use this for anti-cheats if you want to be super safe.
         */
        PRECISE_BLOCK(0.1D),
        /**
         * You should use this for anti-cheats if you want to be super safe. Otherwise
         */
        PRECISE_ENTITY(0.01D);

        private double advance;

        Precision(double advance) {
            this.advance = advance;
        }

        public double getAdvance() {
            return advance;
        }
    }

    public enum ResultType {
        ENTITY, BLOCK, EMPTY
    }

    public static class RayCastResult {

        private ResultType type;

        private Object object;

        public RayCastResult(ResultType type, Object object) {
            this.type = type;
            this.object = object;
        }

        public Object get() {
            return object;
        }

        public ResultType getType() {
            return type;
        }

        public boolean isEmpty() {
            return type == ResultType.EMPTY;
        }

        @Override
        public String toString() {
            return "RayTraceResult{" +
                    "type: " + type +
                    ", object: " + object +
                    '}';
        }
    }

    public static class BlockRayCastResult extends RayCastResult {

        private Block block;

        private BlockFace face;

        public BlockRayCastResult(ResultType type, Block block, BlockFace face) {
            super(type, block);
            this.block = block;
            this.face = face;
        }

        public Block getBlock() {
            return block;
        }

        public BlockFace getFace() {
            return face;
        }

        @Override
        public String toString() {
            return "BlockRayTraceResult{" +
                    "block: " + block +
                    ", face: " + face +
                    '}';
        }
    }

    public static class EntityRayCastResult extends RayCastResult {

        private Entity entity;

        public EntityRayCastResult(ResultType type, Entity entity) {
            super(type, entity);
            this.entity = entity;
        }

        public Entity getEntity() {
            return entity;
        }

        @Override
        public String toString() {
            return "EntityRayTraceResult{" +
                    "entity: " + entity +
                    '}';
        }
    }

    public static class BoundingBox {
        private double x1;
        private double y1;
        private double z1;

        private double x2;
        private double y2;
        private double z2;

        public BoundingBox(double x1, double y1, double z1, double x2, double y2, double z2) {
            this.x1 = Math.min(x1, x2);
            this.y1 = Math.min(y1, y2);
            this.z1 = Math.min(z1, z2);
            this.x2 = Math.max(x1, x2);
            this.y2 = Math.max(y1, y2);
            this.z2 = Math.max(z1, z2);
        }

        public BoundingBox(Object nmsObject) throws NoSuchFieldException {
            try {
                this.x1 = (double) getFieldFromNmsObject("a", nmsObject);
                this.y1 = (double) getFieldFromNmsObject("b", nmsObject);
                this.z1 = (double) getFieldFromNmsObject("c", nmsObject);

                this.x2 = (double) getFieldFromNmsObject("d", nmsObject);
                this.y2 = (double) getFieldFromNmsObject("e", nmsObject);
                this.z2 = (double) getFieldFromNmsObject("f", nmsObject);
            } catch (NoSuchFieldException e) {
                this.x1 = (double) getFieldFromNmsObject("minX", nmsObject);
                this.y1 = (double) getFieldFromNmsObject("minY", nmsObject);
                this.z1 = (double) getFieldFromNmsObject("minZ", nmsObject);

                this.x2 = (double) getFieldFromNmsObject("maxX", nmsObject);
                this.y2 = (double) getFieldFromNmsObject("maxY", nmsObject);
                this.z2 = (double) getFieldFromNmsObject("maxZ", nmsObject);
            }
        }

        public BoundingBox(Location loc1, Location loc2) {
            this(loc1.getX(), loc1.getY(), loc1.getZ(), loc2.getX(), loc2.getY(), loc2.getZ());
        }

        @Override
        public String toString() {
            return "BoundingBox{" +
                    "x1: " + x1 +
                    ", y1: " + y1 +
                    ", z1: " + z1 +
                    ", x2: " + x2 +
                    ", y2: " + y2 +
                    ", z2: " + z2 +
                    '}';
        }

        public boolean isWithinBoundingBox(double x, double y, double z) {
            return x >= this.x1 && x <= this.x2 && y >= this.y1 && y <= this.y2 && z >= this.z1 && z <= this.z2;
        }

        public boolean isWithinBoundingBox(Location location) {
            return this.isWithinBoundingBox(location.getX(), location.getY(), location.getZ());
        }
    }

    public static Object getFieldFromNmsObject(String fieldName, Object o) throws NoSuchFieldException {

        try {
            Field field;
            try {
                field = o.getClass().getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                field = o.getClass().getField(fieldName);
            }
            field.setAccessible(true);
            Object value = field.get(o);
            field.setAccessible(false);
            return value;
        } catch (NoSuchFieldException exc) {
            throw exc;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }
}