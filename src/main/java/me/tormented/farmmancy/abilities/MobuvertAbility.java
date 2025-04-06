package me.tormented.farmmancy.abilities;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public abstract class MobuvertAbility<EntityType extends Entity> extends MobAbility<EntityType> {

    protected AbilityHeadDisplay headDisplay;

    protected MobuvertAbility(@NotNull UUID id, @NotNull UUID owner) {
        super(id, owner);
    }

    @Override
    public void onDeactivate(boolean visual) {
        super.onDeactivate(visual);

        headDisplay.remove();
        mobCenterOffset = new Vector(0.0f, 3.0f, 0.0f);
    }

    @Override
    public void onTick(CallerSource callerSource) {
        if (callerSource == CallerSource.PLAYER && getOwnerPlayer() instanceof Player player) {

            float rotation = player.getLocation().getYaw();

            headDisplay.setLocation(player.getLocation().add(0.0, 3.0, 0.0).setRotation(rotation + 180.0f, 0f));
        }
    }

    @Override
    public void onActivate(boolean visual) {
        super.onActivate(visual);

        if (getOwnerPlayer() instanceof Player player) {
            headDisplay.spawn(player.getLocation());
        }
    }

    public boolean setMob(@Nullable Entity entity) {
        if (registerAddedEntity(entity) instanceof AbilityHeadDisplay addedHeadDisplay) {
            headDisplay = addedHeadDisplay;
            return true;
        }
        return false;
    }

    public @Nullable EntityType removeAndSummonMob(@NotNull Location location) {
        if (removeMob() instanceof AbilityHeadDisplay removingHeadDisplay) {

            EntityType entity = spawnEntity(location) ;
            applySpawnVariant(entity, removingHeadDisplay);
            headDisplay.remove();
            return entity;

        }
        return null;
    }

    public @Nullable AbilityHeadDisplay removeMob() {
        AbilityHeadDisplay removingHeadDisplay = headDisplay;
        headDisplay = null;
        return removingHeadDisplay;
    }

}
