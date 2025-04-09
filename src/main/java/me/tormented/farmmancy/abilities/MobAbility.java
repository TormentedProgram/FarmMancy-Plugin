package me.tormented.farmmancy.abilities;


import me.tormented.farmmancy.FarmMancy;
import me.tormented.farmmancy.abilities.utils.Wand;
import me.tormented.farmmancy.abilities.utils.WandUtils;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public abstract class MobAbility<EntityType extends Entity> extends Ability implements Hook.Activation, Hook.Ticking, Hook.EntityDamaged, Hook.EntityDeath, Hook.PlayerInteraction {

    public static final NamespacedKey MobunitionEntityKey = new NamespacedKey(FarmMancy.getInstance(), "mobunition_entity");

    public abstract Class<EntityType> getEntityClass();

    public Vector mobCenterOffset;
    protected int startTick;
    public boolean isBaby = false;

    protected MobAbility(@NotNull UUID id, @NotNull UUID owner) {
        super(id, owner);
        mobCenterOffset = new Vector(0.0f, 0.0f, 0.0f);
    }

    @Override
    public void processEntityDamage(@NotNull EntityDamageEvent event) {
        if (event.getEntity().hasMetadata("FarmMancy_Projectile")) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK
                && event.getCause() != EntityDamageEvent.DamageCause.KILL) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onActivate(boolean visual) {
        startTick = FarmMancy.getInstance().getServer().getCurrentTick();
        active = true;
    }

    @Override
    public void onDeactivate(boolean visual) {
        active = false;
    }

    private boolean active = false;

    @Override
    public boolean isActive() {
        return active;
    }

    public abstract boolean isHeadRingVisible();

    @Override
    public abstract void onTick(@NotNull CallerSource callerSource);

    @Override
    public void processEntityDeath(@NotNull EntityDeathEvent event) {
        event.getDrops().clear();
    }

    public EntityType spawnEntity(@NotNull Location location) {
        return location.getWorld().spawn(location, getEntityClass());
    }

    public abstract @NotNull ItemStack getHeadItem(@Nullable EntityType entity);

    public void applySpawnVariant(@NotNull EntityType entity, @NotNull AbilityHeadDisplay headDisplay) {
    }

    protected @Nullable AbilityHeadDisplay registerAddedEntity(@Nullable Entity entity) {
        AbilityHeadDisplay headDisplay = null;

        if (entity == null) {
            headDisplay = new AbilityHeadDisplay(getHeadItem(null));
        } else if (getEntityClass() instanceof Class<?> clazz && clazz.isInstance(entity)) {
            try {
                headDisplay = new AbilityHeadDisplay(getHeadItem((EntityType) entity));

                entity.remove();
            } catch (ClassCastException ignored) {
            }
        }

        if (getOwnerPlayer() instanceof Player player) {
            if (!WandUtils.isHoldingWand(player)) {
                return headDisplay;
            }
        }

        if (headDisplay != null) {
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

        }
        return headDisplay;
    }

    public abstract boolean isBeingLookedAt();

    @Override
    public void processPlayerInteract(@NotNull PlayerInteractEvent event) {
        if (this instanceof Hook.WandSelectable wandSelectable && event.getItem() != null) {
            Wand checkingWand = new Wand(event.getItem());
            if (checkingWand.isWand()) {
                Ability checkingAbility = checkingWand.getBoundAbility();
                if (isBeingLookedAt() && checkingAbility == null) {
                    checkingWand.setBoundAbility(this);
                } else if (checkingAbility == this) {
                    wandSelectable.onWandUse(checkingWand, event);
                }
            }
        }

    }
}
