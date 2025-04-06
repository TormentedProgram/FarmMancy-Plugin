package me.tormented.farmmancy.abilities;

import me.tormented.farmmancy.FarmMancy;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class MobunitionAbility<EntityType extends Entity> extends MobAbility<EntityType> implements Hook.EntityInteractedByPlayer {

    protected final List<AbilityHeadDisplay> headDisplays = new ArrayList<>();

    public float modRingRadius;

    protected MobunitionAbility(@NotNull UUID id, @NotNull UUID owner) {
        super(id, owner);
        modRingRadius = 0;
    }

    @Override
    public void onDeactivate(boolean visual) {
        super.onDeactivate(visual);

        for (AbilityHeadDisplay headDisplay : headDisplays) {
            headDisplay.remove();
        }
    }

    @Override
    public void onTick(CallerSource callerSource) {
        if (callerSource == CallerSource.PLAYER && getOwnerPlayer() instanceof Player player) {
            float lifetime = FarmMancy.getInstance().getServer().getCurrentTick() - startTick;
            lifetime = (lifetime / 8);

            for (int i = 0; i < headDisplays.size(); i++) {
                float rotation = -(float) (((double) i / headDisplays.size()) * Math.TAU + lifetime);


                AbilityHeadDisplay headDisplay = headDisplays.get(i);

                if (slot % 2 == 1) {
                    headDisplay.setLocation(player.getLocation().setRotation((float) Math.toDegrees(-rotation) + 180.0f, 0f).add(
                            Math.cos(rotation) * modRingRadius + mobCenterOffset.getX(),
                            mobCenterOffset.getY() + slotPositions[slot],
                            Math.sin(-rotation) * modRingRadius + mobCenterOffset.getZ()
                    ));
                } else {
                    headDisplay.setLocation(player.getLocation().setRotation((float) Math.toDegrees(rotation), 0f).add(
                            Math.cos(rotation) * modRingRadius + mobCenterOffset.getX(),
                            mobCenterOffset.getY() + slotPositions[slot],
                            Math.sin(rotation) * modRingRadius + mobCenterOffset.getZ()
                    ));
                }

            }
        }
    }

    public boolean addMob(@Nullable Entity entity) {
        if (registerAddedEntity(entity) instanceof AbilityHeadDisplay addedHeadDisplay){
            headDisplays.add(addedHeadDisplay);
            return true;
        }
        return false;
    }

    public @Nullable EntityType pullAndSummonMob(@NotNull Location location) {
        if (pullMob() instanceof AbilityHeadDisplay headDisplay) {

            EntityType entity = spawnEntity(location) ;
            applySpawnVariant(entity, headDisplay);
            headDisplay.remove();
            return entity;

        }
        return null;
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

    @Override
    public void processPlayerInteractEntity(PlayerInteractEntityEvent event, CallerSource callerSource) {
        if (callerSource == CallerSource.PLAYER) {
            if (event.getRightClicked() instanceof LivingEntity entity) {
                Player player = event.getPlayer();
                if (addMob(entity)) {
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);
                }
            }
        }

    }
}
