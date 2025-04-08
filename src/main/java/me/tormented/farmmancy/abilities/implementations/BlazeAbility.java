package me.tormented.farmmancy.abilities.implementations;

import me.tormented.farmmancy.abilities.AbilityHeadDisplay;
import me.tormented.farmmancy.abilities.Hook;
import me.tormented.farmmancy.abilities.MobunitionAbility;
import me.tormented.farmmancy.abilities.utils.Wand;
import me.tormented.farmmancy.utils.HeadProvider;
import org.bukkit.Location;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
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

    public BlazeAbility(@NotNull UUID id, @NotNull UUID owner) {
        super(id, owner);
    }

    public static final HeadProvider headProvider = new HeadProvider("http://textures.minecraft.net/texture/b20657e24b56e1b2f8fc219da1de788c0c24f36388b1a409d0cd2d8dba44aa3b");

    @Override
    public @NotNull ItemStack getHeadItem(Blaze entity) {
        return headProvider.getHeadItem();
    }

    @Override
    public void onSelected(Wand wand) {
    }

    @Override
    public void onDeselected(Wand wand) {
    }

    @Override
    public void onWandUse(Wand wand, PlayerInteractEvent event) {
        if (event.getAction().isLeftClick() && getOwnerPlayer() instanceof Player player && !headDisplays.isEmpty()) {
            Location loc = player.getLocation();
            Vector direction = loc.getDirection();
            Location targetLoc = loc.add(direction.multiply(slotRadii[slot]));

            if (pullMob() instanceof AbilityHeadDisplay headDisplay) {
                headDisplay.remove();
                Fireball f = player.launchProjectile(Fireball.class);
            }
        }
    }
}
