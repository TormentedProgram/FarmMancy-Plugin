package me.tormented.farmmancy.abilities.implementations;

import me.tormented.farmmancy.FarmMancy;
import me.tormented.farmmancy.abilities.AbilityHeadDisplay;
import me.tormented.farmmancy.abilities.Hook;
import me.tormented.farmmancy.abilities.MobunitionAbility;
import me.tormented.farmmancy.abilities.utils.Wand;
import me.tormented.farmmancy.utils.HeadProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class EvokerAbility extends MobunitionAbility<Evoker> implements Hook.WandSelectable {
    @Override
    public Class<Evoker> getEntityClass() {
        return Evoker.class;
    }

    public EvokerAbility(@NotNull UUID id, @NotNull UUID owner) {
        super(id, owner);
    }

    public static final HeadProvider headProvider = new HeadProvider("http://textures.minecraft.net/texture/630ce775edb65db8c2741bdfae84f3c0d0285aba93afadc74900d55dfd9504a5");

    @Override
    public @NotNull ItemStack getHeadItem(Evoker entity) {
        return headProvider.getHeadItem();
    }

    @Override
    public @NotNull TextComponent getName() {
        return Component.text("EVOKER ABILITY")
                .color(NamedTextColor.DARK_GREEN)
                .decoration(TextDecoration.ITALIC, false);
    };

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
                createFangs(player);
            }
        }
    }

    public void createFangs(Player player) {
        Location location = player.getLocation();
        double distance = 20.0;

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL, 1.0f, 1.0f);

        new BukkitRunnable() {
            double traveled = 0;

            @Override
            public void run() {
                if (traveled >= distance) {
                    cancel();
                    return;
                }

                Location particleLocation = location.clone().add(location.getDirection().multiply(traveled)).add(0.0, 1.0, 0.0);
                player.getWorld().spawnParticle(Particle.DUST, particleLocation, 1, 0.0, 0.0, 0.0, new Particle.DustOptions(Color.GREEN, 1.0f));

                for (Entity entity : player.getWorld().getEntities()) {
                    if (entity instanceof LivingEntity mob) {
                        if (mob.getLocation().distance(particleLocation) < 3.0 && !mob.equals(player)) {
                            EvokerFangs fangs = player.getWorld().spawn(mob.getLocation(), EvokerFangs.class);
                            cancel();
                        }
                    }
                }

                traveled += 1;
            }
        }.runTaskTimer(FarmMancy.getInstance(), 0, 1);
    }
}
