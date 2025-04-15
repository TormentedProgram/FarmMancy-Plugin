package me.tormented.farmmancy.abilities;

import me.tormented.farmmancy.FarmMancy;
import me.tormented.farmmancy.abilities.utils.WandUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public abstract class ItemunitionAbility<EntityType extends Entity> extends MobunitionAbility<EntityType> implements Hook.EntityInteractedByPlayer {

    protected ItemunitionAbility(@NotNull AbilityFactory abilityFactory, @NotNull UUID id, @NotNull UUID owner) {
        super(abilityFactory, id, owner);
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
                    headDisplay.setLocation(player.getLocation().setRotation((float) Math.toDegrees(-rotation) + (180.0f + 90), 0f).add(
                            Math.cos(rotation) * slotRadii[slot] + mobCenterOffset.getX(),
                            mobCenterOffset.getY() + slotPositions[slot],
                            Math.sin(-rotation) * slotRadii[slot] + mobCenterOffset.getZ()
                    ));
                } else {
                    headDisplay.setLocation(player.getLocation().setRotation((float) Math.toDegrees(rotation) + 90, 0f).add(
                            Math.cos(rotation) * slotRadii[slot] + mobCenterOffset.getX(),
                            mobCenterOffset.getY() + slotPositions[slot],
                            Math.sin(rotation) * slotRadii[slot] + mobCenterOffset.getZ()
                    ));
                }
            }
        }
    }
}
