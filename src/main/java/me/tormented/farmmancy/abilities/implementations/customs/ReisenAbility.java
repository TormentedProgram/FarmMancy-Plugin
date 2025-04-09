package me.tormented.farmmancy.abilities.implementations.customs;

import me.tormented.farmmancy.FarmMancy;
import me.tormented.farmmancy.abilities.AbilityFactory;
import me.tormented.farmmancy.abilities.AbilityHeadDisplay;
import me.tormented.farmmancy.abilities.MobunitionAbility;
import me.tormented.farmmancy.abilities.utils.WandUtils;
import me.tormented.farmmancy.utils.HeadProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.UUID;

public class ReisenAbility extends MobunitionAbility<Entity> {
    @Override
    public Class<Entity> getEntityClass() {
        return null;
    }

    public ReisenAbility(@NotNull AbilityFactory abilityFactory, @NotNull UUID id, @NotNull UUID owner) {
        super(abilityFactory, id, owner);
    }

    public static final HeadProvider headProvider = new HeadProvider("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmU3MjhhOGFmZDU4ZDFkM2M2NWUxY2Y4ZGUxMDVkNzA5Mjg4ZGIyMWJkOTE4MzczODYzNDZjZTE2M2VkNTM4MiJ9fX0=");

    @Override
    public @NotNull ItemStack getHeadItem(@Nullable Entity entity) {
        return headProvider.getHeadItem();
    }

    @Override
    public void processPlayerInteractEntity(@NotNull PlayerInteractEntityEvent event, CallerSource callerSource) {
        super.processPlayerInteractEntity(event, callerSource);

        if (callerSource == CallerSource.PLAYER && WandUtils.isHoldingWand(event.getPlayer())) {
            if (event.getRightClicked() instanceof LivingEntity entity && !entity.isDead() && entity.getNoDamageTicks() <= 0 && pullMob() instanceof AbilityHeadDisplay headDisplay) {
                headDisplay.remove();
                if (entity instanceof Player player) {
                    player.playSound(player, Sound.ENTITY_CREEPER_PRIMED, 1f, 1f);
                }
                startRepeatingTask(entity);
            }
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

    private void startRepeatingTask(LivingEntity entity) {
        new BukkitRunnable() {
            private int count = 0;

            @Override
            public void run() {
                if (count < 75) {
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

