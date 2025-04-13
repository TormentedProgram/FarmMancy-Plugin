package me.tormented.farmmancy.abilities.implementations.customs;

import me.tormented.farmmancy.FarmMancy;
import me.tormented.farmmancy.abilities.AbilityFactory;
import me.tormented.farmmancy.abilities.AbilityHeadDisplay;
import me.tormented.farmmancy.abilities.Hook;
import me.tormented.farmmancy.abilities.MobunitionAbility;
import me.tormented.farmmancy.abilities.utils.Wand;
import me.tormented.farmmancy.abilities.utils.headProviders.CustomHeadProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.UUID;

public class ReisenAbility extends MobunitionAbility<Entity> implements Hook.WandSelectable {
    @Override
    public Class<Entity> getEntityClass() {
        return null;
    }

    @Override
    public @NotNull Component getName() {
        return MiniMessage.miniMessage().deserialize("<gradient:light_purple:red>Reisen Ability</gradient>");
    }

    public ReisenAbility(@NotNull AbilityFactory abilityFactory, @NotNull UUID id, @NotNull UUID owner) {
        super(abilityFactory, id, owner);
    }


    public static final CustomHeadProvider CUSTOM_HEAD_PROVIDER = new CustomHeadProvider("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmU3MjhhOGFmZDU4ZDFkM2M2NWUxY2Y4ZGUxMDVkNzA5Mjg4ZGIyMWJkOTE4MzczODYzNDZjZTE2M2VkNTM4MiJ9fX0=");

    @Override
    public @NotNull ItemStack getHeadItem(@Nullable Entity entity) {
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
        if (pullMob() instanceof AbilityHeadDisplay headDisplay) {
            headDisplay.remove();
            insanity(event.getPlayer());
        }
    }

    public static @NotNull Vector getFungusVector(LivingEntity player) {
        Random random = new Random();

        double x = random.nextDouble() * 2 - 1;
        double z = random.nextDouble() * 2 - 1;
        double y = 0;

        Vector randomVector = new Vector(x, y, z);

        randomVector.normalize();

        return randomVector;
    }

    public void insanity(Player player) {
        Location location = player.getLocation();
        double distance = 20.0;
        new BukkitRunnable() {
            double traveled = 0;

            @Override
            public void run() {
                if (traveled >= distance) {
                    cancel();
                    return;
                }

                Location particleLocation = location.clone().add(location.getDirection().multiply(traveled)).add(0.0, 1.0, 0.0);
                player.getWorld().spawnParticle(Particle.DUST, particleLocation, 1, 0.0, 0.0, 0.0, new Particle.DustOptions(Color.RED, 0.75f));

                for (Entity entity : player.getWorld().getEntities()) {
                    if (entity instanceof LivingEntity mob && !mob.isDead() && mob.getNoDamageTicks() <= 0) {
                        if (mob.getLocation().distance(particleLocation) < 3.0 && !mob.equals(player)) {
                            if (mob instanceof Player player) {
                                player.playSound(player, Sound.ENTITY_CREEPER_PRIMED, 1f, 1f);
                            }
                            mob.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 600, 4, false, false));
                            startRepeatingTask(mob);
                            cancel();
                        }
                    }
                }

                traveled += 1;
            }
        }.runTaskTimer(FarmMancy.getInstance(), 0, 1);
    }

    private void startRepeatingTask(LivingEntity entity) {
        new BukkitRunnable() {
            private int count = 0;

            @Override
            public void run() {
                if (count < 75) {
                    if (entity.isDead()) this.cancel();
                    entity.setVelocity(getFungusVector(entity).multiply(0.2));
                    Random random = new Random();
                    float rotationX = random.nextFloat() * 360;
                    float rotationY = -90 + random.nextFloat() * 180;
                    entity.setRotation(rotationX, rotationY);

                    int jumpChance = random.nextInt(100);
                    int hitChance = random.nextInt(100);

                    if (jumpChance < 10) {
                        entity.setJumping(true);
                    }
                    if (hitChance < 25) {
                        entity.swingMainHand();
                    }

                    if (entity instanceof Player plyer) {
                        plyer.sendMessage(Component.text("AXBBABCBABIBEIAAIIBAEIAAAABCBABABEIAAABBBEIAAABCB", NamedTextColor.WHITE, TextDecoration.OBFUSCATED));
                    }

                    entity.getWorld().spawnParticle(Particle.DUST, entity.getLocation().add(0.0, 1.0, 0.0), 8, 1.0, 1.0, 1.0, new Particle.DustOptions(Color.RED, 0.75f));
                    entity.getWorld().spawnParticle(Particle.DUST, entity.getLocation().add(0.0, 1.0, 0.0), 8, 1.0, 1.0, 1.0, new Particle.DustOptions(Color.BLUE, 0.75f));
                    entity.getWorld().spawnParticle(Particle.DUST, entity.getLocation().add(0.0, 1.0, 0.0), 2, 1.0, 1.0, 1.0, new Particle.DustOptions(Color.GREEN, 0.75f));

                    entity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 1, true, false));

                    count++;
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(FarmMancy.getInstance(), 0L, 5L); // 0L delay before first run, 10L (0.5 seconds) between runs
    }
}

