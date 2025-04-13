package me.tormented.farmmancy.abilities.implementations;

import me.tormented.farmmancy.FarmConfig;
import me.tormented.farmmancy.FarmMancy;
import me.tormented.farmmancy.abilities.AbilityFactory;
import me.tormented.farmmancy.abilities.EventDistributor;
import me.tormented.farmmancy.abilities.Hook;
import me.tormented.farmmancy.abilities.MobunitionAbility;
import me.tormented.farmmancy.abilities.utils.headProviders.CustomHeadProvider;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class SheepAbility extends MobunitionAbility<Sheep> implements Hook.PlayerInteraction {

    @Override
    public Class<Sheep> getEntityClass() {
        return Sheep.class;
    }

    public SheepAbility(@NotNull AbilityFactory abilityFactory, @NotNull UUID id, @NotNull UUID owner) {
        super(abilityFactory, id, owner);
    }

    private final float healingValue = FarmConfig.getInstance().getPigHealingAmount();

    @Override
    public void onDeactivate(boolean visual) {
        super.onDeactivate(visual);
        if (getOwnerPlayer() instanceof Player player) {
            AttributeInstance attribute = player.getAttribute(Attribute.MAX_HEALTH);
            if (attribute != null) {
                attribute.setBaseValue(attribute.getDefaultValue());
            }
        }
    }

    @Override
    public void onTick(@NotNull CallerSource callerSource) {
        super.onTick(callerSource);

        if (callerSource == CallerSource.PLAYER && getOwnerPlayer() instanceof Player player) {
            AttributeInstance attribute = player.getAttribute(Attribute.MAX_HEALTH);
            if (attribute != null) {
                attribute.setBaseValue(attribute.getDefaultValue() + (headDisplays.size() * 2.5));
            }
        }
    }

    public static final CustomHeadProvider CUSTOM_HEAD_PROVIDER = new CustomHeadProvider("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjkyZGYyMTZlY2QyNzYyNGFjNzcxYmFjZmJmZTAwNmUxZWQ4NGE3OWU5MjcwYmUwZjg4ZTljODc5MWQxZWNlNCJ9fX0=");

    @Override
    public @NotNull ItemStack getHeadItem(@Nullable Sheep entity) {
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

                player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 200, 1));
            }
        }
    }
}
