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
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class BlazeAbility extends MobunitionAbility<Blaze> implements Hook.WandSelectable {
    @Override
    public Class<Blaze> getEntityClass() {
        return Blaze.class;
    }

    public BlazeAbility(@NotNull AbilityFactory abilityFactory, @NotNull UUID id, @NotNull UUID owner) {
        super(abilityFactory, id, owner);
    }

    public static final CustomHeadProvider CUSTOM_HEAD_PROVIDER = new CustomHeadProvider("http://textures.minecraft.net/texture/b20657e24b56e1b2f8fc219da1de788c0c24f36388b1a409d0cd2d8dba44aa3b");

    @Override
    public @NotNull ItemStack getHeadItem(Blaze entity) {
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

                loc.getWorld().playSound(player, Sound.ENTITY_BLAZE_SHOOT, 1.0f, 1.0f);

                for (int i = -1; i < 2; i++) {

                    Vector targetDirection = player.getEyeLocation().getDirection().rotateAroundY(Math.toRadians(i * 20.0f));
                    targetDirection.multiply(1.0f);
                    player.launchProjectile(SmallFireball.class, targetDirection);
                }
            }
        }
    }

    @Override
    public @NotNull Component getName() {
        return Component.text("Blaze Ability", NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false);
    }

}
