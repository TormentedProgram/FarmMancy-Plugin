package me.tormented.farmmancy.abilities.implementations.customs;

import me.tormented.farmmancy.abilities.AbilityHeadDisplay;
import me.tormented.farmmancy.abilities.MobunitionAbility;
import me.tormented.farmmancy.abilities.utils.WandUtils;
import me.tormented.farmmancy.utils.HeadProvider;
import org.bukkit.Particle;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class YuyukoAbility extends MobunitionAbility<Entity> {
    @Override
    public Class<Entity> getEntityClass() {
        return null;
    }

    public YuyukoAbility(@NotNull UUID id, @NotNull UUID owner) {
        super(id, owner);
    }

    public static final HeadProvider headProvider = new HeadProvider("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTg4MTM4ZGFlMmYwZmM2NmMwNjU0ZTkyNWJhMWM3MWJhMjNkNjk3NGVjNWExMzA1NWUzYmQ5ODcyNzNmYTc3NCJ9fX0=");

    @Override
    public @NotNull ItemStack getHeadItem(@Nullable Entity entity) {
        return headProvider.getHeadItem();
    }

    @Override
    public void processPlayerInteractEntity(@NotNull PlayerInteractEntityEvent event, CallerSource callerSource) {
        super.processPlayerInteractEntity(event, callerSource);

        if (callerSource == CallerSource.PLAYER && WandUtils.isHoldingWand(event.getPlayer())) {
            if (event.getRightClicked() instanceof LivingEntity targetEntity && !targetEntity.isDead() && targetEntity.getNoDamageTicks() <= 0 && pullMob() instanceof AbilityHeadDisplay headDisplay) {
                headDisplay.remove();
                targetEntity.getWorld().spawnParticle(Particle.CHERRY_LEAVES, targetEntity.getLocation().add(0.0, 1.0, 0.0), 100, 1.0, 1.0, 1.0, null);
                targetEntity.damage(Float.MAX_VALUE, DamageSource.builder(DamageType.PLAYER_ATTACK)
                        .withCausingEntity(event.getPlayer())
                        .withDirectEntity(event.getPlayer())
                        .build()
                );
            }
        }

    }
}

