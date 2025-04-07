package me.tormented.farmmancy.abilities;

import me.tormented.farmmancy.abilities.utils.WandUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

import static me.tormented.farmmancy.abilities.utils.WandUtils.isHoldingCowWand;

public abstract class MobuvertAbility<EntityType extends Entity> extends MobAbility<EntityType> implements Hook.PlayerSneak, Hook.PlayerSwapItem {

    protected AbilityHeadDisplay headDisplay;

    protected MobuvertAbility(@NotNull UUID id, @NotNull UUID owner) {
        super(id, owner);
    }

    @Override
    public void onDeactivate(boolean visual) {
        super.onDeactivate(visual);

        if (headDisplay != null) headDisplay.remove();
        mobCenterOffset = new Vector(0.0f, 3.0f, 0.0f);
    }

    @Override
    public void processSneakToggle(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (headDisplay == null) return;
        if (event.isSneaking()) {
            headDisplay.remove();
        } else {
            if (isActive() && isHoldingCowWand(player)) {
                headDisplay.spawn(player.getLocation());
            }
        }
    }

    @Override
    public void processSwapItem(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        if (headDisplay == null) return;
        if (player.isSneaking()) return;
        if (isActive() && WandUtils.isHoldingCowWand(player, event.getNewSlot())) {
            headDisplay.spawn(player.getLocation());
        }else{
            headDisplay.remove();
        }
    }

    @Override
    public void onTick(CallerSource callerSource) {
        if (callerSource == CallerSource.PLAYER && getOwnerPlayer() instanceof Player player) {

            float rotation = player.getLocation().getYaw();

            if (headDisplay != null) {
                headDisplay.setLocation(player.getLocation().add(0.0, 3.0, 0.0).setRotation(rotation + 180.0f, 0f));
            }
        }
    }

    @Override
    public void onActivate(boolean visual) {
        super.onActivate(visual);

        if (getOwnerPlayer() instanceof Player player) {
            ItemStack item = player.getInventory().getItemInMainHand();
            ItemMeta meta = item.getItemMeta();
            if (isActive() && isHoldingCowWand(player)) {
                headDisplay.spawn(player.getLocation());
            }
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

            EntityType entity = spawnEntity(location);
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
