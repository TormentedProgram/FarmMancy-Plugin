package me.tormented.farmmancy.abilities.implementations;

import me.tormented.farmmancy.FarmMancy;
import me.tormented.farmmancy.abilities.AbilityFactory;
import me.tormented.farmmancy.abilities.EventDistributor;
import me.tormented.farmmancy.abilities.Hook;
import me.tormented.farmmancy.abilities.MobunitionAbility;
import me.tormented.farmmancy.abilities.utils.headProviders.CustomHeadProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Strider;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class StriderAbility extends MobunitionAbility<Strider> implements Hook.PlayerInteraction, Hook.playerMove {

    @Override
    public Class<Strider> getEntityClass() {
        return Strider.class;
    }

    public StriderAbility(@NotNull AbilityFactory abilityFactory, @NotNull UUID id, @NotNull UUID owner) {
        super(abilityFactory, id, owner);
    }

    public static final CustomHeadProvider CUSTOM_HEAD_PROVIDER = new CustomHeadProvider("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWM0MGZhZDFjMTFkZTllNjQyMmI0MDU0MjZlOWI5NzkwN2YzNWJjZTM0NWUzNzU4NjA0ZDNlN2JlN2RmODg0In19fQ==");

    @Override
    public @NotNull ItemStack getHeadItem(@Nullable Strider entity) {
        return CUSTOM_HEAD_PROVIDER.getHeadItem();
    }

    @Override
    public void processPlayerMove(@NotNull PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location loc = player.getLocation();
        Block blockUnder = loc.clone().subtract(0.0D, 0.5D, 0.0D).getBlock();
        if (blockUnder.getType() == Material.LAVA) {
            Vector velocity = player.getVelocity();
            if (velocity.getY() < 0.0D) {
                velocity.setY(0.2D);
                player.setVelocity(velocity);
            }
            player.setFireTicks(20);
            player.setFallDistance(0.0F);
        }
    }

    @Override
    public void processPlayerInteract(@NotNull PlayerInteractEvent event) {
        super.processPlayerInteract(event);

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

    @Override
    public @NotNull Component getName() {
        return Component.text("Strider Ability", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false);
    }

}
