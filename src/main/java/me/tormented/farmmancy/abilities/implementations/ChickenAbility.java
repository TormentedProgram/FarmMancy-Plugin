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
import org.bukkit.entity.Chicken;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ChickenAbility extends MobunitionAbility<Chicken> implements Hook.PlayerInteraction {

    @Override
    public Class<Chicken> getEntityClass() {
        return Chicken.class;
    }

    public ChickenAbility(@NotNull AbilityFactory abilityFactory, @NotNull UUID id, @NotNull UUID owner) {
        super(abilityFactory, id, owner);
    }

    public static final CustomHeadProvider CUSTOM_HEAD_PROVIDER = new CustomHeadProvider("http://textures.minecraft.net/texture/42af6e5847eea099e1b0ab8c20a9e5f3c7190158bda54e28133d9b271ec0cb4b");

    @Override
    public @NotNull ItemStack getHeadItem(@Nullable Chicken entity) {
        return CUSTOM_HEAD_PROVIDER.getHeadItem();
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

                int previousDuration = 0;
                Vector velocityVector = player.getVelocity();
                velocityVector.setY(0.6f);
                if (player.hasPotionEffect(PotionEffectType.SLOW_FALLING)) {
                    PotionEffect potion = player.getPotionEffect(PotionEffectType.SLOW_FALLING);
                    if (potion != null) {
                        previousDuration = potion.getDuration();
                        player.removePotionEffect(PotionEffectType.SLOW_FALLING);
                    }

                }
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, previousDuration + 80, 1));
                player.setVelocity(velocityVector);
            }
        }
    }

    @Override
    public @NotNull Component getName() {
        return Component.text("Chicken Ability", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false);
    }

}
