package me.tormented.farmmancy.abilities.implementations.customs;

import me.tormented.farmmancy.FarmMancy;
import me.tormented.farmmancy.abilities.AbilityFactory;
import me.tormented.farmmancy.abilities.AbilityHeadDisplay;
import me.tormented.farmmancy.abilities.Hook;
import me.tormented.farmmancy.abilities.MobunitionAbility;
import me.tormented.farmmancy.abilities.utils.Wand;
import me.tormented.farmmancy.utils.HeadProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class YuyukoAbility extends MobunitionAbility<Entity> implements Hook.WandSelectable {
    @Override
    public Class<Entity> getEntityClass() {
        return null;
    }

    public YuyukoAbility(@NotNull AbilityFactory abilityFactory, @NotNull UUID id, @NotNull UUID owner) {
        super(abilityFactory, id, owner);
    }

    @Override
    public @NotNull Component getName() {
        return MiniMessage.miniMessage().deserialize("<gradient:aqua:light_purple>Yuyuko Ability</gradient>");
    }

    public static final HeadProvider headProvider = new HeadProvider("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTg4MTM4ZGFlMmYwZmM2NmMwNjU0ZTkyNWJhMWM3MWJhMjNkNjk3NGVjNWExMzA1NWUzYmQ5ODcyNzNmYTc3NCJ9fX0=");

    @Override
    public @NotNull ItemStack getHeadItem(@Nullable Entity entity) {
        return headProvider.getHeadItem();
    }


    @Override
    public void onSelected(@NotNull Wand wand) {

    }

    @Override
    public void onDeselected(@NotNull Wand wand) {

    }

    @Override
    public void onWandUse(@NotNull Wand wand, @NotNull PlayerInteractEvent event) {
        if (pullMob() instanceof AbilityHeadDisplay headDisplay) {
            headDisplay.remove();
            yuyukoThing(event.getPlayer());
        }
    }

    public void yuyukoThing(Player player) {
        Location location = player.getLocation();
        double distance = 20.0;

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_CAST_SPELL, 1.0f, 0.8f);
        new BukkitRunnable() {
            double traveled = 0;

            @Override
            public void run() {
                if (traveled >= distance) {
                    cancel();
                    return;
                }

                Location particleLocation = location.clone().add(location.getDirection().multiply(traveled)).add(0.0, 1.0, 0.0);
                player.getWorld().spawnParticle(Particle.CHERRY_LEAVES, particleLocation, 1, 0.0, 0.0, 0.0);

                for (Entity entity : player.getWorld().getEntities()) {
                    if (entity instanceof LivingEntity mob && !mob.isDead() && mob.getNoDamageTicks() <= 0) {
                        if (mob.getLocation().distance(particleLocation) < 3.0 && !mob.equals(player)) {
                            mob.getWorld().spawnParticle(Particle.CHERRY_LEAVES, entity.getLocation().add(0.0, 1.0, 0.0), 100, 1.0, 1.0, 1.0, null);
                            mob.damage(Float.MAX_VALUE, DamageSource.builder(DamageType.PLAYER_ATTACK)
                                    .withCausingEntity(entity)
                                    .withDirectEntity(entity)
                                    .build()
                            );
                            cancel();
                        }
                    }
                }

                traveled += 1;
            }
        }.runTaskTimer(FarmMancy.getInstance(), 0, 1);
    }
}

