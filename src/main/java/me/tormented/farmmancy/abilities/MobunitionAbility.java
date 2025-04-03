package me.tormented.farmmancy.abilities;

import me.tormented.farmmancy.FarmMancy;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class MobunitionAbility<EntityType extends Entity> extends MobAbility<EntityType> {

    protected final List<EntityType> entities = new ArrayList<>();
    public int amount = 0;  // TODO: SYNC WITH LIST

    public float modRingRadius;

    protected MobunitionAbility(@NotNull UUID id, @NotNull UUID owner) {
        super(id, owner);
        modRingRadius = 0;
    }

    @Override
    public void onDeactivate(boolean visual) {
        for (Entity entity : entities) {
            if (visual && entity instanceof LivingEntity livingEntity) {
                livingEntity.setHealth(0f);
            } else {
                entity.remove();
            }
            EventDistributor.getInstance().entityMobunitionAbilityMap.remove(entity);
        }
        entities.clear();
    }

    @Override
    public void onTick(CallerSource callerSource) {
        if (callerSource == CallerSource.PLAYER && getOwnerPlayer() instanceof Player player) {
            float lifetime = FarmMancy.getInstance().getServer().getCurrentTick() - startTick;
            lifetime = (lifetime / 8);

            entities.removeIf(Entity::isDead);

            for (int i = 0; i < entities.size(); i++) {
                float rotation = -(float) (((double) i / entities.size()) * Math.TAU + lifetime);
                EntityType entity = entities.get(i);
                if (entity.isDead()) {
                    entities.remove(entity);
                    continue;
                }
                entity.teleport(player.getLocation().setRotation((float) Math.toDegrees(rotation), 0f).add(
                        Math.cos(rotation) * modRingRadius + mobCenterOffset.getX(),
                        mobCenterOffset.getY(),
                        Math.sin(rotation) * modRingRadius + mobCenterOffset.getZ()
                ));
            }
        }
    }

    public boolean addMob(Entity entity) {
        EventDistributor.getInstance().entityMobunitionAbilityMap.put(entity, this);
        if (getEntityClass().isInstance(entity)) {
            EntityType specialEntity = (EntityType) entity;
            amount += 1;
            entities.add(specialEntity);
            return true;
        }
        return false;
    }

    @Override
    public void onActivate(boolean visual) {
        super.onActivate(visual);

        if (getOwnerPlayer() instanceof Player player) {

            for (int i = 0; i < amount; i++) {
                EntityType specialEntity = player.getLocation().getWorld().spawn(player.getLocation(), getEntityClass());

                if (isBaby && specialEntity instanceof Ageable ageable) {
                    ageable.setBaby();
                }

                EventDistributor.getInstance().entityMobunitionAbilityMap.put(specialEntity, this);

                entities.add(specialEntity);
            }
        }


    }
}
