package me.tormented.farmmancy.abilities.implementations;

import me.tormented.farmmancy.FarmMancy;
import me.tormented.farmmancy.abilities.EventDistributor;
import me.tormented.farmmancy.abilities.Hook;
import me.tormented.farmmancy.abilities.MobunitionAbility;
import me.tormented.farmmancy.utils.HeadProvider;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Strider;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class StriderAbility extends MobunitionAbility<Strider> implements Hook.PlayerInteraction {

    @Override
    public Class<Strider> getEntityClass() {
        return Strider.class;
    }

    public StriderAbility(@NotNull UUID id, @NotNull UUID owner) {
        super(id, owner);
    }

    public static final HeadProvider headProvider = new HeadProvider("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWM0MGZhZDFjMTFkZTllNjQyMmI0MDU0MjZlOWI5NzkwN2YzNWJjZTM0NWUzNzU4NjA0ZDNlN2JlN2RmODg0In19fQ==");

    @Override
    public @NotNull ItemStack getHeadItem(@Nullable Strider entity) {
        return headProvider.getHeadItem();
    }

    @Override
    public void processPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction().isRightClick() && isBeingLookedAt()) {
            Player player = event.getPlayer();

            Location loc = player.getLocation();
            Vector direction = loc.getDirection();
            Location targetLoc = loc.add(direction.multiply(1));

            if (pullAndSummonMob(targetLoc) instanceof LivingEntity livingEntity) {
                EventDistributor.getInstance().entityMobunitionAbilityMap.put(livingEntity, this);
                livingEntity.setMetadata("FarmMancy_Projectile", new FixedMetadataValue(FarmMancy.getInstance(), this));
                livingEntity.setHealth(0f);

                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 200, 1));
            }
        }
    }

}
