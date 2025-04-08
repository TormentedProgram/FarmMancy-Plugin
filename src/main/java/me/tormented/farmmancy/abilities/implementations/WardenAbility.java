package me.tormented.farmmancy.abilities.implementations;

import me.tormented.farmmancy.FarmMancy;
import me.tormented.farmmancy.abilities.AbilityHeadDisplay;
import me.tormented.farmmancy.abilities.Hook;
import me.tormented.farmmancy.abilities.MobunitionAbility;
import me.tormented.farmmancy.abilities.utils.Wand;
import me.tormented.farmmancy.utils.HeadProvider;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Warden;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class WardenAbility extends MobunitionAbility<Warden> implements Hook.WandSelectable {
    @Override
    public Class<Warden> getEntityClass() {
        return Warden.class;
    }

    public WardenAbility(@NotNull UUID id, @NotNull UUID owner) {
        super(id, owner);
    }

    public static final HeadProvider headProvider = new HeadProvider("http://textures.minecraft.net/texture/f8c211d66c803aac15ab86f79c7edfd6c3b2034d23355a92f6bd42e835260be0");

    @Override
    public @NotNull ItemStack getHeadItem(Warden entity) {
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
                createSonicBoom(player);
            }
        }
    }

    public void createSonicBoom(Player player) {
        Location location = player.getLocation();
        double distance = 20.0;
        double damage = 20;

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 1.0f, 1.0f);

        new BukkitRunnable() {
            double traveled = 0;

            @Override
            public void run() {
                if (traveled >= distance) {
                    cancel();
                    return;
                }

                Location particleLocation = location.clone().add(location.getDirection().multiply(traveled)).add(0.0, 1.0, 0.0);
                player.getWorld().spawnParticle(Particle.SONIC_BOOM, particleLocation, 1);

                for (Entity entity : player.getWorld().getEntities()) {
                    if (entity instanceof LivingEntity mob) {
                        if (mob.getLocation().distance(particleLocation) < 3.0 && !mob.equals(player)) {
                            mob.damage(damage);
                        }
                    }
                }

                traveled += 1;
            }
        }.runTaskTimer(FarmMancy.getInstance(), 0, 1);
    }
}
