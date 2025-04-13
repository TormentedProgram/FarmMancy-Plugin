package me.tormented.farmmancy.abilities;

import me.tormented.farmmancy.FarmMancy;
import me.tormented.farmmancy.abilities.utils.WandUtils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public abstract class MobunitionAbility<EntityType extends Entity> extends MobAbility<EntityType> implements Hook.EntityInteractedByPlayer {

    protected final List<AbilityHeadDisplay> headDisplays = new ArrayList<>();

    protected static final float[] slotPositions = {0.5f, 1.75f, 3f};
    protected static final float[] slotRadii = {2.3f, 3.0f, 2.3f};
    protected static final float headBoxRadius = 0.25f;
    protected static final float headVerticalCenterOffset = -0.25f;

    protected MobunitionAbility(@NotNull AbilityFactory abilityFactory, @NotNull UUID id, @NotNull UUID owner) {
        super(abilityFactory, id, owner);
    }

    @Override
    public void onDeactivate(boolean visual) {
        super.onDeactivate(visual);

        for (AbilityHeadDisplay headDisplay : headDisplays) {
            headDisplay.remove();
        }

        if (!visual)
            headDisplays.clear();
    }

    @Override
    public boolean isBeingLookedAt() {
        if (isHeadRingVisible() && getOwnerPlayer() instanceof Player player) {

            double eyeHeight = player.getEyeHeight();

            double tanPitch = Math.tan(Math.toRadians(-player.getPitch()));
            double heightOffset = eyeHeight - slotPositions[slot] - headVerticalCenterOffset;
            double gInner = tanPitch * (slotRadii[slot] - headBoxRadius) + heightOffset;
            double gOuter = tanPitch * (slotRadii[slot] + headBoxRadius) + heightOffset;

            return (-headBoxRadius < gInner && gInner < headBoxRadius) || (-headBoxRadius < gOuter && gOuter < headBoxRadius);
        }
        return false;
    }

    @Override
    public boolean isHeadRingVisible() {
        return !headDisplays.isEmpty() && headDisplays.getFirst().isSpawned();
    }

    @Override
    public void onTick(@NotNull CallerSource callerSource) {
        if (callerSource == CallerSource.PLAYER && getOwnerPlayer() instanceof Player player) {
            if (player.isSneaking()) {
                for (AbilityHeadDisplay headDisplay : headDisplays) {
                    headDisplay.remove();
                }
            } else {
                ItemStack item = player.getInventory().getItemInMainHand();
                if (isActive() && WandUtils.isHoldingWand(player)) {
                    boolean beingLookedAt = isBeingLookedAt();
                    for (AbilityHeadDisplay headDisplay : headDisplays) {
                        headDisplay.spawn(player.getLocation());
                        if (headDisplay.getItemDisplay() instanceof ItemDisplay itemDisplay) {
                            itemDisplay.setGlowing(beingLookedAt);
                        }
                    }
                }
            }
            if (!player.isSneaking()) {
                if (isActive() && WandUtils.isHoldingWand(player)) {
                    for (AbilityHeadDisplay headDisplay : headDisplays) {
                        headDisplay.spawn(player.getLocation());
                    }
                } else {
                    for (AbilityHeadDisplay headDisplay : headDisplays) {
                        headDisplay.remove();
                    }
                }
            }

            float lifetime = FarmMancy.getInstance().getServer().getCurrentTick() - startTick;
            lifetime = (lifetime / 8);

            for (int i = 0; i < headDisplays.size(); i++) {
                float rotation = -(float) (((double) i / headDisplays.size()) * Math.TAU + lifetime);


                AbilityHeadDisplay headDisplay = headDisplays.get(i);

                if (slot % 2 == 1) {
                    headDisplay.setLocation(player.getLocation().setRotation((float) Math.toDegrees(-rotation) + 180.0f, 0f).add(
                            Math.cos(rotation) * slotRadii[slot] + mobCenterOffset.getX(),
                            mobCenterOffset.getY() + slotPositions[slot],
                            Math.sin(-rotation) * slotRadii[slot] + mobCenterOffset.getZ()
                    ));
                } else {
                    headDisplay.setLocation(player.getLocation().setRotation((float) Math.toDegrees(rotation), 0f).add(
                            Math.cos(rotation) * slotRadii[slot] + mobCenterOffset.getX(),
                            mobCenterOffset.getY() + slotPositions[slot],
                            Math.sin(rotation) * slotRadii[slot] + mobCenterOffset.getZ()
                    ));
                }
            }
        }
    }

    public boolean addMob(@Nullable Entity entity) {
        if (registerAddedEntity(entity) instanceof AbilityHeadDisplay addedHeadDisplay) {
            headDisplays.add(addedHeadDisplay);
            return true;
        }
        return false;
    }

    public @Nullable EntityType pullAndSummonMob(@NotNull Location location) {
        if (pullMob() instanceof AbilityHeadDisplay headDisplay) {

            EntityType entity = spawnEntity(location);
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
            ItemStack item = player.getInventory().getItemInMainHand();
            ItemMeta meta = item.getItemMeta();
            if (isActive() && WandUtils.isHoldingWand(player)) {
                for (AbilityHeadDisplay headDisplay : headDisplays) {
                    headDisplay.spawn(player.getLocation());
                }
            }
        }
    }

    @Override
    public void processPlayerInteractEntity(@NotNull PlayerInteractEntityEvent event, CallerSource callerSource) {
        if (callerSource == CallerSource.PLAYER) {
            if (WandUtils.isHoldingWand(Objects.requireNonNull(getOwnerPlayer()))) return;
            if (event.getRightClicked().hasMetadata("FarmMancy_Projectile")) return;
            if (event.getRightClicked() instanceof LivingEntity entity) {
                AttributeInstance maxHealthAttribute = entity.getAttribute(Attribute.MAX_HEALTH);
                if (maxHealthAttribute != null && !entity.isDead() && !entity.hasMetadata("FarmMancy_Projectile")) {
                    if (entity.getHealth() <= (maxHealthAttribute.getValue() / 5)) {
                        Player player = event.getPlayer();
                        if (addMob(entity)) {
                            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);
                        }
                    }
                }
            }
        }

    }
}
