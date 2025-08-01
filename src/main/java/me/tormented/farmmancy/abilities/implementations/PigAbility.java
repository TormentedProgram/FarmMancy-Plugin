package me.tormented.farmmancy.abilities.implementations;

import me.tormented.farmmancy.FarmConfig;
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
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PigAbility extends MobunitionAbility<Pig> implements Hook.PlayerInteraction, Hook.PlayerJoining {

    @Override
    public Class<Pig> getEntityClass() {
        return Pig.class;
    }

    public PigAbility(@NotNull AbilityFactory abilityFactory, @NotNull UUID id, @NotNull UUID owner) {
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

    public static final CustomHeadProvider CUSTOM_HEAD_PROVIDER = new CustomHeadProvider("http://textures.minecraft.net/texture/41ee7681adf00067f04bf42611c97641075a44ae2b1c0381d5ac6b3246211bfe");

    @Override
    public @NotNull ItemStack getHeadItem(@Nullable Pig entity) {
        return CUSTOM_HEAD_PROVIDER.getHeadItem();
    }

    @Override
    public void processPlayerJoin(@NotNull PlayerJoinEvent event) {
        Player player = event.getPlayer();
        AttributeInstance attribute = player.getAttribute(Attribute.MAX_HEALTH);
        if (attribute != null) {
            attribute.setBaseValue(attribute.getDefaultValue());
        }
    }

    @Override
    public void processPlayerQuit(@NotNull PlayerQuitEvent event) {

    }

    @Override
    public void processPlayerInteract(@NotNull PlayerInteractEvent event) {
        super.processPlayerInteract(event);

        if (event.getAction().isRightClick() && isBeingLookedAt()) {
            Player player = event.getPlayer();

            if (player.getAttribute(Attribute.MAX_HEALTH) instanceof AttributeInstance healthAttribute) {
                if (player.getHealth() < healthAttribute.getValue()) {

                    Location loc = player.getLocation();
                    Vector direction = loc.getDirection();
                    Location targetLoc = loc.add(direction.multiply(1));

                    if (pullAndSummonMob(targetLoc) instanceof LivingEntity livingEntity) {
                        EventDistributor.getInstance().entityMobunitionAbilityMap.put(livingEntity, this);
                        livingEntity.setMetadata("FarmMancy_Projectile", new FixedMetadataValue(FarmMancy.getInstance(), this));
                        livingEntity.setHealth(0f);

                        player.heal(healingValue);
                    }
                }
            }
        }
    }

    @Override
    public @NotNull Component getName() {
        return Component.text("Pig Ability", NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false);
    }

}
