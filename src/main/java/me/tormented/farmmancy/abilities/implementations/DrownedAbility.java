package me.tormented.farmmancy.abilities.implementations;

import me.tormented.farmmancy.abilities.AbilityFactory;
import me.tormented.farmmancy.abilities.AbilityHeadDisplay;
import me.tormented.farmmancy.abilities.Hook;
import me.tormented.farmmancy.abilities.MobunitionAbility;
import me.tormented.farmmancy.abilities.utils.Wand;
import me.tormented.farmmancy.abilities.utils.headProviders.CustomHeadProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class DrownedAbility extends MobunitionAbility<Drowned> implements Hook.WandSelectable {
    public DrownedAbility(@NotNull AbilityFactory abilityFactory, @NotNull UUID id, @NotNull UUID owner) {
        super(abilityFactory, id, owner);
    }

    @Override
    public Class<Drowned> getEntityClass() {
        return Drowned.class;
    }

    public static final CustomHeadProvider CUSTOM_HEAD_PROVIDER = new CustomHeadProvider("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzNmN2NjZjYxZGJjM2Y5ZmU5YTYzMzNjZGUwYzBlMTQzOTllYjJlZWE3MWQzNGNmMjIzYjNhY2UyMjA1MSJ9fX0=");

    @Override
    public @NotNull ItemStack getHeadItem(@Nullable Drowned entity) {
        return CUSTOM_HEAD_PROVIDER.getHeadItem();
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

                loc.getWorld().playSound(player, Sound.ENTITY_DROWNED_SHOOT, 1.0f, 1.0f);

                Trident trident = player.launchProjectile(Trident.class);
                trident.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
            }
        }
    }

    @Override
    public @NotNull Component getName() {
        return Component.text("Drowned Ability", NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false);
    }
}
