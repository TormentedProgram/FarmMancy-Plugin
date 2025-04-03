package me.tormented.farmmancy.abilities;

import me.tormented.farmmancy.FarmMancy;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public abstract class MobuvertAbility<EntityType extends Entity> extends MobAbility<EntityType> {

    protected EntityType entity;

    protected MobuvertAbility(@NotNull UUID id, @NotNull UUID owner) {
        super(id, owner);
    }

    @Override
    public void onDeactivate(boolean visual) {
        if (visual && entity instanceof LivingEntity livingEntity) {
            livingEntity.setHealth(0f);
        } else {
            entity.remove();
        }
    }

    @Override
    public void onTick(CallerSource callerSource) {
        if (callerSource == CallerSource.PLAYER && getOwnerPlayer() instanceof Player player) {

            float rotation = player.getLocation().getYaw();
            if (entity.isDead()) return;

            entity.teleport(player.getLocation().setRotation((float) Math.toDegrees(rotation), 0f));
        }
    }

    @Override
    public void onActivate(boolean visual) {
        super.onActivate(visual);

        if (getOwnerPlayer() instanceof Player player) {
            entity = player.getLocation().getWorld().spawn(player.getLocation(), getEntityClass());
            entity.setMetadata("FarmMancy_OwnedMob", new FixedMetadataValue(FarmMancy.getInstance(), this));
        }

    }
}
