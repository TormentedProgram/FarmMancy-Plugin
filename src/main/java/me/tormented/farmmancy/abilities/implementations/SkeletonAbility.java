package me.tormented.farmmancy.abilities.implementations;

import me.tormented.farmmancy.abilities.AbilityFactory;
import me.tormented.farmmancy.abilities.AbilityHeadDisplay;
import me.tormented.farmmancy.abilities.Hook;
import me.tormented.farmmancy.abilities.MobunitionAbility;
import me.tormented.farmmancy.abilities.utils.Wand;
import me.tormented.farmmancy.abilities.utils.headProviders.BlockProvider;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class SkeletonAbility extends MobunitionAbility<Skeleton> implements Hook.WandSelectable {
    public SkeletonAbility(@NotNull AbilityFactory abilityFactory, @NotNull UUID id, @NotNull UUID owner) {
        super(abilityFactory, id, owner);
    }

    @Override
    public Class<Skeleton> getEntityClass() {
        return Skeleton.class;
    }

    public static final BlockProvider BLOCK_PROVIDER = new BlockProvider(Material.SKELETON_SKULL);

    @Override
    public @NotNull ItemStack getHeadItem(@Nullable Skeleton entity) {
        return BLOCK_PROVIDER.getHeadItem();
    }

    @Override
    public void onSelected(@NotNull Wand wand) {

    }

    @Override
    public void onDeselected(@NotNull Wand wand) {

    }

    @Override
    public void onWandUse(@NotNull Wand wand, @NotNull PlayerInteractEvent event) {
        if (event.getAction().isLeftClick() && getOwnerPlayer() instanceof Player player && !headDisplays.isEmpty()) {
            Location loc = player.getLocation();
            Vector direction = loc.getDirection();
            Location targetLoc = loc.add(direction.multiply(slotRadii[slot]));

            if (pullMob() instanceof AbilityHeadDisplay headDisplay) {
                headDisplay.remove();

                loc.getWorld().playSound(player, Sound.ENTITY_SKELETON_SHOOT, 1.0f, 1.0f);

                Arrow arrow = player.launchProjectile(Arrow.class);
                arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
            }
        }
    }
}
