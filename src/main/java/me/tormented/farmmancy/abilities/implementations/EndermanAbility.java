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
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class EndermanAbility extends MobunitionAbility<Enderman> implements Hook.WandSelectable {
    @Override
    public Class<Enderman> getEntityClass() {
        return Enderman.class;
    }

    public EndermanAbility(@NotNull AbilityFactory abilityFactory, @NotNull UUID id, @NotNull UUID owner) {
        super(abilityFactory, id, owner);
    }

    public static final CustomHeadProvider CUSTOM_HEAD_PROVIDER = new CustomHeadProvider("http://textures.minecraft.net/texture/9689c200980e4c54adcfbbdad492c1d2edbd92366aabf89724ed19930cb5b6e2");

    @Override
    public @NotNull Component getName() {
        return Component.text("Enderman Ability", NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false);
    }

    @Override
    public @NotNull ItemStack getHeadItem(Enderman entity) {
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
                EnderPearl f = player.launchProjectile(EnderPearl.class);
            }
        }
    }
}
