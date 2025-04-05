package me.tormented.farmmancy.abilities;

import me.tormented.farmmancy.FarmMancy;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class MobunitionAbility<EntityType extends Entity> extends MobAbility<EntityType> {

    protected final List<AbilityHeadDisplay> headDisplays = new ArrayList<>();

    public float modRingRadius;

    protected MobunitionAbility(@NotNull UUID id, @NotNull UUID owner) {
        super(id, owner);
        modRingRadius = 0;
    }

    @Override
    public void onDeactivate(boolean visual) {
        for (AbilityHeadDisplay headDisplay : headDisplays) {
            headDisplay.remove();
            //EventDistributor.getInstance().entityMobunitionAbilityMap.remove(entity);
        }
        headDisplays.clear();
    }

    @Override
    public void onTick(CallerSource callerSource) {
        if (callerSource == CallerSource.PLAYER && getOwnerPlayer() instanceof Player player) {
            float lifetime = FarmMancy.getInstance().getServer().getCurrentTick() - startTick;
            lifetime = (lifetime / 8);

            for (int i = 0; i < headDisplays.size(); i++) {
                float rotation = -(float) (((double) i / headDisplays.size()) * Math.TAU + lifetime);
                AbilityHeadDisplay headDisplay = headDisplays.get(i);


                headDisplay.setLocation(player.getLocation().setRotation((float) Math.toDegrees(rotation), 0f).add(
                        Math.cos(rotation) * modRingRadius + mobCenterOffset.getX(),
                        mobCenterOffset.getY(),
                        Math.sin(rotation) * modRingRadius + mobCenterOffset.getZ()
                ));
            }
        }
    }

    public boolean addMob(@Nullable Entity entity) {
        // EventDistributor.getInstance().entityMobunitionAbilityMap.put(entity, this);

        AbilityHeadDisplay headDisplay = null;

        if (entity == null) {
            headDisplay = new AbilityHeadDisplay(getHeadItem(null));
        } else if (getEntityClass().isInstance(entity)) {
            try {
                headDisplay = new AbilityHeadDisplay(getHeadItem((EntityType) entity));

                entity.remove();
            } catch (ClassCastException ignored) {}
        }

        if (headDisplay != null) {
            headDisplays.add(headDisplay);

            if (isActive()) {
                Location headLocation = null;

                if (entity != null) {
                    headLocation = entity.getLocation();
                } else if (getOwnerPlayer() instanceof Player player) {
                    headLocation = player.getLocation();
                }

                if (headLocation != null)
                    headDisplay.spawn(headLocation);
                else
                    FarmMancy.getInstance().getLogger().warning("Failed to spawn mob head because the ability is in an illegal state");
            }

            return true;
        }
        return false;
    }

    public @Nullable EntityType pullAndSummonMob(@NotNull Location location) {
        if (pullMob() instanceof AbilityHeadDisplay headDisplay) {

            EntityType entity = spawnEntity(location) ;
            applySpawnVariant(entity, headDisplay);
            return entity;

        }
        return null;
    }

    public void applySpawnVariant(EntityType entity, AbilityHeadDisplay headDisplay) {
    }

    public @Nullable AbilityHeadDisplay pullMob() {
        if (headDisplays.isEmpty()) {
            return null;
        } else {
            return headDisplays.removeFirst();
        }
    }

    @Override
    public void onActivate(boolean visual) {
        super.onActivate(visual);

        if (getOwnerPlayer() instanceof Player player) {

            for (AbilityHeadDisplay headDisplay : headDisplays) {
                headDisplay.spawn(player.getLocation());
            }
        }

    }
}
